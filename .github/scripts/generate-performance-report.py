#!/usr/bin/env python3
import json
import os
import re
import sys
from html import escape
from datetime import datetime, timezone


FIXED_WORKLOAD_SCENARIO = 'ISA fixed workload (direct buffer)'


def load_results(input_dir):
    results = []
    for root, _, files in os.walk(input_dir):
        for name in files:
            if name.endswith('.json'):
                path = os.path.join(root, name)
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        data = json.load(f)
                        data['_source'] = path
                        results.append(data)
                except Exception as e:
                    print(f"Warning: failed to parse {path}: {e}", file=sys.stderr)
    return results


def safe_get(d, *keys, default=None):
    for key in keys:
        if isinstance(d, dict) and key in d:
            d = d[key]
        elif isinstance(d, list) and isinstance(key, int) and -len(d) <= key < len(d):
            d = d[key]
        else:
            return default
    return d


def format_num(value, decimals=2):
    if value is None:
        return 'N/A'
    try:
        return f'{float(value):,.{decimals}f}'
    except (ValueError, TypeError):
        return str(value)


PLATFORM_ORDER = [
    'linux-x86_64',
    'linux-x86_64-avx2',
    'linux-x86_64-baseline',
    'linux-x86_64-upstream-auto',
    'linux-arm64',
    'linux-arm64-baseline',
    'linux-arm64-upstream-auto',
    'windows-x86_64',
    'windows-x86_64-baseline',
    'windows-x86_64-upstream-auto',
]


def platform_sort_key(platform):
    try:
        return PLATFORM_ORDER.index(platform)
    except ValueError:
        return len(PLATFORM_ORDER)


def sanitize_filename(name):
    return re.sub(r'[^A-Za-z0-9_-]+', '_', name)


def implementation_for(result):
    impl = safe_get(result, 'implementation')
    if impl:
        return impl
    source = result.get('_source', '')
    lower = source.lower()
    if 'upstream' in lower:
        return 'upstream'
    if 'panama' in lower:
        return 'panama'
    if 'javacpp' in lower:
        return 'javacpp'
    return 'javacpp'


def is_unsupported(result):
    return bool(safe_get(result, 'unsupported', default=False))


def is_stale(result):
    return bool(safe_get(result, 'stale', default=False))


def stale_mark(flag):
    return '<sup title="data from an earlier run">†</sup>' if flag else ''


def stale_note_for(result):
    ts = safe_get(result, 'timestamp', default=None) or 'earlier run'
    version = safe_get(result, 'artifactVersion', default=None)
    commit = safe_get(result, 'commitSha', default=None)
    parts = [str(ts)]
    if version:
        parts.append(str(version))
    if commit:
        parts.append('commit ' + str(commit)[:7])
    return ' · '.join(parts)


def scenario_names(results):
    names = set()
    for r in results:
        for bench in safe_get(r, 'benchmarks', default=[]):
            name = safe_get(bench, 'name')
            if name:
                names.add(name)
    return sorted(names)


def find_benchmark(result, scenario_name):
    for bench in safe_get(result, 'benchmarks', default=[]):
        if safe_get(bench, 'name') == scenario_name:
            return bench
    return None


def metric_for(result, scenario_name, key):
    bench = find_benchmark(result, scenario_name)
    if bench:
        return safe_get(bench, 'metrics', key, default=None)
    return None


def throughput_for(result, scenario_name=FIXED_WORKLOAD_SCENARIO):
    value = metric_for(result, scenario_name, 'throughputMiBpsAvg')
    if value is None:
        value = metric_for(result, scenario_name, 'throughputMiBps')
    return float(value) if value is not None else None


def elapsed_for(result, scenario_name=FIXED_WORKLOAD_SCENARIO):
    value = metric_for(result, scenario_name, 'elapsedMsAvg')
    if value is None:
        value = metric_for(result, scenario_name, 'elapsedMsPerOperation')
    return float(value) if value is not None else None


def matches_for(result, scenario_name=FIXED_WORKLOAD_SCENARIO):
    return metric_for(result, scenario_name, 'matchesPerOperation')


def iterations_for(result, scenario_name):
    return metric_for(result, scenario_name, 'measurementSamples')


def ops_per_second_for(result, scenario_name):
    return metric_for(result, scenario_name, 'opsPerSecond')


def ns_per_op_for(result, scenario_name):
    return metric_for(result, scenario_name, 'nsPerOp')


def fixed_workload_scenario(results):
    names = scenario_names(results)
    if FIXED_WORKLOAD_SCENARIO in names:
        return FIXED_WORKLOAD_SCENARIO
    # Fallback: first scenario with throughput data
    for name in names:
        for r in results:
            if metric_for(r, name, 'throughputMiBpsAvg'):
                return name
    return names[0] if names else None


def build_platform_summary(results, scenario_name):
    by_platform = {}
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        impl = implementation_for(r)
        by_platform.setdefault(platform, {})[impl] = r

    rows = []
    for platform, impls in by_platform.items():
        javacpp = impls.get('javacpp')
        panama = impls.get('panama')
        upstream = impls.get('upstream')
        if javacpp and not find_benchmark(javacpp, scenario_name):
            javacpp = None
        if panama and not find_benchmark(panama, scenario_name):
            panama = None
        if upstream and not is_unsupported(upstream) and not find_benchmark(upstream, scenario_name):
            upstream = None
        javacpp_tp = (throughput_for(javacpp, scenario_name) or 0.0) if javacpp else 0.0
        panama_tp = (throughput_for(panama, scenario_name) or 0.0) if panama else 0.0
        upstream_tp = (throughput_for(upstream, scenario_name) or 0.0) if upstream else 0.0
        upstream_unsupported = is_unsupported(upstream) if upstream else False

        best_impl = None
        best_tp = 0.0
        if (javacpp and panama and not is_stale(javacpp) and not is_stale(panama)
                and javacpp_tp > 0 and panama_tp > 0):
            best_impl, best_tp = max(
                (('javacpp', javacpp_tp), ('panama', panama_tp)),
                key=lambda item: item[1])

        rows.append({
            'platform': platform,
            'javacpp': javacpp,
            'panama': panama,
            'upstream': upstream,
            'javacppThroughput': javacpp_tp,
            'panamaThroughput': panama_tp,
            'upstreamThroughput': upstream_tp,
            'upstreamUnsupported': upstream_unsupported,
            'javacppStale': is_stale(javacpp) if javacpp else False,
            'panamaStale': is_stale(panama) if panama else False,
            'upstreamStale': is_stale(upstream) if upstream else False,
            'bestImplementation': best_impl,
            'bestThroughput': best_tp,
            'results': list(impls.values())
        })

    rows.sort(key=lambda x: platform_sort_key(x['platform']))
    return rows


def build_scenario_rows(results, scenario_name):
    rows = []
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        impl = implementation_for(r)
        runner_os = safe_get(r, 'runnerOs', default='-')
        runner_arch = safe_get(r, 'runnerArch', default='-')
        cpu_model = safe_get(r, 'cpuModel', default='-')
        cpu_flags = safe_get(r, 'cpuFlags', default='-')
        cpu_display = cpu_model if cpu_model != '-' else cpu_flags
        if cpu_display == '-' or cpu_display == 'unknown':
            cpu_display = '-'
        benchmark = find_benchmark(r, scenario_name)
        if not is_unsupported(r) and benchmark is None:
            continue
        rows.append({
            'platform': platform,
            'implementation': impl,
            'unsupported': is_unsupported(r),
            'stale': is_stale(r),
            'runner_os': runner_os,
            'runner_arch': runner_arch,
            'cpu_display': cpu_display,
            'cpu_flags': cpu_flags,
            'result': r,
            'throughput': throughput_for(r, scenario_name),
            'elapsed': elapsed_for(r, scenario_name),
            'matches': matches_for(r, scenario_name),
            'iterations': iterations_for(r, scenario_name),
            'ops_per_second': ops_per_second_for(r, scenario_name),
            'ns_per_op': ns_per_op_for(r, scenario_name)
        })
    rows.sort(key=lambda x: (platform_sort_key(x['platform']), 0 if x['implementation'] == 'javacpp' else 1))
    return rows


def build_scenario_chart_rows(results, scenario_name):
    by_platform = {}
    for r in results:
        if is_stale(r):
            continue
        platform = safe_get(r, 'platform', default='unknown')
        impl = implementation_for(r)
        by_platform.setdefault(platform, {})[impl] = r

    rows = []
    for platform, impls in by_platform.items():
        javacpp = impls.get('javacpp')
        panama = impls.get('panama')
        upstream = impls.get('upstream')
        upstream_unsupported = is_unsupported(upstream) if upstream else False
        javacpp_benchmark = find_benchmark(javacpp, scenario_name) if javacpp else None
        panama_benchmark = find_benchmark(panama, scenario_name) if panama else None
        upstream_benchmark = find_benchmark(upstream, scenario_name) if upstream else None
        javacpp_tp = throughput_for(javacpp, scenario_name) if javacpp_benchmark else None
        panama_tp = throughput_for(panama, scenario_name) if panama_benchmark else None
        upstream_tp = (throughput_for(upstream, scenario_name)
                       if upstream_benchmark and not upstream_unsupported else None)
        javacpp_ops = ops_per_second_for(javacpp, scenario_name) if javacpp_benchmark else None
        panama_ops = ops_per_second_for(panama, scenario_name) if panama_benchmark else None
        upstream_ops = (ops_per_second_for(upstream, scenario_name)
                        if upstream_benchmark and not upstream_unsupported else None)

        metric_type = None
        javacpp_value = 0.0
        panama_value = 0.0
        upstream_value = 0.0
        if any(value is not None and value > 0 for value in (javacpp_tp, panama_tp, upstream_tp)):
            metric_type = 'throughput'
            javacpp_value = javacpp_tp
            panama_value = panama_tp
            upstream_value = upstream_tp
        elif any(value is not None and value > 0 for value in (javacpp_ops, panama_ops, upstream_ops)):
            metric_type = 'ops'
            javacpp_value = javacpp_ops
            panama_value = panama_ops
            upstream_value = upstream_ops

        if metric_type is None:
            continue

        rows.append({
            'platform': platform,
            'javacpp': javacpp_value,
            'panama': panama_value,
            'upstream': upstream_value,
            'upstream_unsupported': upstream_unsupported,
            'metric_type': metric_type
        })

    rows.sort(key=lambda x: platform_sort_key(x['platform']))
    return rows


def render_scenario_chart(html, rows, scenario_name, svg_link=None):
    if not rows:
        return
    max_value = max((value for row in rows for value in
                     (row['javacpp'], row['panama'], row['upstream']) if value is not None), default=0)
    if max_value <= 0:
        return

    metric_type = rows[0]['metric_type']
    unit = 'MiB/s' if metric_type == 'throughput' else 'ops/s'
    label = 'Throughput' if metric_type == 'throughput' else 'Ops/Second'

    html.append(f'    <h4>{escape(label)} Comparison</h4>')
    if svg_link:
        html.append(f'    <p style="font-size: 0.85rem;"><a href="{escape(svg_link)}">View as SVG</a></p>')

    for row in rows:
        javacpp_width = (row['javacpp'] or 0) / max_value * 100 if max_value > 0 else 0
        panama_width = (row['panama'] or 0) / max_value * 100 if max_value > 0 else 0
        upstream_width = (row['upstream'] or 0) / max_value * 100 if max_value > 0 else 0

        html.append('    <div style="margin-bottom: 0.75rem;">')
        html.append(f'      <div style="margin-bottom: 0.25rem; font-size: 0.9rem;">{escape(row["platform"])}</div>')
        html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">')
        html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">JavaCPP</div>')
        html.append('        <div class="bar-bg" style="flex: 1;">')
        html.append(f'          <div class="bar-fill" style="width: {javacpp_width:.1f}%;"></div>')
        html.append('        </div>')
        html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(row["javacpp"]))} {unit}</div>')
        html.append('      </div>')
        html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">')
        html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">Panama</div>')
        html.append('        <div class="bar-bg" style="flex: 1;">')
        html.append(f'          <div class="bar-fill" style="width: {panama_width:.1f}%; background: #0969da;"></div>')
        html.append('        </div>')
        html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(row["panama"]))} {unit}</div>')
        html.append('      </div>')
        html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem;">')
        html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">Upstream</div>')
        if row['upstream_unsupported']:
            html.append('        <div style="flex: 1; font-size: 0.85rem; color: #6a737d;">unsupported</div>')
        else:
            html.append('        <div class="bar-bg" style="flex: 1;">')
            html.append(f'          <div class="bar-fill" style="width: {upstream_width:.1f}%; background: #6a737d;"></div>')
            html.append('        </div>')
            html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(row["upstream"]))} {unit}</div>')
        html.append('      </div>')
        html.append('    </div>')


def generate_html(results, output_file, native_version, commit_sha):
    fixed_scenario = fixed_workload_scenario(results)
    platform_rows = build_platform_summary(results, fixed_scenario) if fixed_scenario else []
    scenarios = scenario_names(results)

    current_count = sum(1 for result in results if not is_stale(result) and not is_unsupported(result))
    stale_count = sum(1 for result in results if is_stale(result))
    unsupported_count = sum(1 for result in results if is_unsupported(result))

    title = 'Hyperscan Java Native Performance Report'
    generated_at = datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S UTC')

    html = []
    html.append('<!DOCTYPE html>')
    html.append('<html lang="en">')
    html.append('<head>')
    html.append('  <meta charset="UTF-8">')
    html.append('  <meta name="viewport" content="width=device-width, initial-scale=1.0">')
    html.append(f'  <title>{escape(title)}</title>')
    html.append('  <style>')
    html.append('''
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif; margin: 0; padding: 2rem; background: #f6f8fa; color: #24292f; }
    .container { max-width: 1200px; margin: 0 auto; background: #fff; border-radius: 8px; padding: 2rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
    h1 { margin-top: 0; color: #1f2328; }
    h2 { border-bottom: 1px solid #d0d7de; padding-bottom: 0.5rem; margin-top: 2rem; }
    h3 { margin-top: 1.5rem; }
    .meta { color: #656d76; margin-bottom: 1.5rem; }
    .meta span { margin-right: 1.5rem; }
    table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
    th, td { padding: 0.75rem; text-align: left; border: 1px solid #d0d7de; }
    th { background: #f6f8fa; font-weight: 600; }
    tr:nth-child(even) { background: #f9f9f9; }
    .best { background: #dafbe1 !important; font-weight: 600; }
    .bar-bg { width: 100%; background: #e1e4e8; border-radius: 4px; height: 1rem; }
    .bar-fill { background: #2ea043; height: 100%; border-radius: 4px; }
    .card { border: 1px solid #d0d7de; border-radius: 6px; padding: 1rem; margin-bottom: 1rem; background: #fff; }
    .card h3 { margin-top: 0; }
    .metrics { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 0.5rem; }
    .metric { padding: 0.5rem; background: #f6f8fa; border-radius: 4px; }
    .metric-label { font-size: 0.85rem; color: #656d76; }
    .metric-value { font-size: 1.1rem; font-weight: 600; }
    .summary-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem; margin-bottom: 1.5rem; }
    .summary-item { background: #f6f8fa; border-radius: 6px; padding: 1rem; }
    .summary-item .label { font-size: 0.85rem; color: #656d76; }
    .summary-item .value { font-size: 1.4rem; font-weight: 700; margin-top: 0.25rem; }
    .impl-row { display: flex; justify-content: space-between; align-items: center; padding: 0.5rem 0; border-bottom: 1px solid #f0f0f0; }
    .impl-row:last-child { border-bottom: none; }
    .impl-name { font-weight: 600; }
    .impl-badge { font-size: 0.75rem; padding: 0.15rem 0.4rem; border-radius: 10px; background: #ffdcd7; color: #cf222e; font-weight: 600; }
    .impl-badge.fastest { background: #dafbe1; color: #1a7f37; }
    .sub-table { width: 100%; margin-top: 0.5rem; }
    .sub-table th, .sub-table td { padding: 0.5rem; font-size: 0.9rem; }
    .scenario-table { margin-bottom: 2rem; }
    ''')
    html.append('  </style>')
    html.append('</head>')
    html.append('<body>')
    html.append('  <div class="container">')
    html.append(f'    <h1>{escape(title)}</h1>')
    html.append('    <div class="meta">')
    html.append(f'      <span><strong>Generated:</strong> {escape(generated_at)}</span>')
    html.append(f'      <span><strong>Native version:</strong> {escape(native_version or "unknown")}</span>')
    html.append(f'      <span><strong>Commit:</strong> {escape(commit_sha or "unknown")}</span>')
    html.append(f'      <span><strong>Platforms:</strong> {len(platform_rows)}</span>')
    html.append('    </div>')

    # Executive summary
    html.append('    <h2>Executive Summary</h2>')
    html.append('    <div class="summary-grid">')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Current result sets</div>')
    html.append(f'        <div class="value">{current_count}</div>')
    html.append('        <div class="label">compatible with this run</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Compatible stale sets</div>')
    html.append(f'        <div class="value">{stale_count}</div>')
    html.append('        <div class="label">excluded from winners</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Unsupported sets</div>')
    html.append(f'        <div class="value">{unsupported_count}</div>')
    html.append('        <div class="label">reported without synthetic zeros</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Benchmark workload</div>')
    workload = 'N/A'
    patterns = 'N/A'
    input_bytes = 'N/A'
    measured_samples = 'N/A'
    workload_result = next((result for result in results
                            if find_benchmark(result, fixed_scenario)), None) if fixed_scenario else None
    if workload_result:
        metrics = safe_get(find_benchmark(workload_result, fixed_scenario), 'metrics', default={})
        patterns = safe_get(metrics, 'patterns', default='N/A')
        input_bytes = safe_get(metrics, 'inputBytes', default='N/A')
        measured_samples = safe_get(metrics, 'measurementSamples', default='N/A')
        workload = f'{patterns} patterns / {input_bytes} bytes'
    html.append(f'        <div class="value">{escape(workload)}</div>')
    html.append('        <div class="label">per iteration</div>')
    html.append('      </div>')
    html.append('    </div>')

    # Fixed workload cross-platform comparison table
    html.append(f'    <h2>Fixed Workload Cross-Platform Comparison</h2>')
    html.append(f'    <p>Same workload used by <a href="https://xenoamess.github.io/hyperscan-java-panama/">hyperscan-java-panama</a>: {escape(str(patterns))} patterns over {escape(str(input_bytes))} bytes, {escape(str(measured_samples))} aggregate measured samples.</p>')
    html.append('    <p>Rows may come from different hosted VMs and are not ranked against each other. “Faster” and “Speedup” compare only fresh JavaCPP and Panama results from the same matrix job.</p>')
    html.append('    <table>')
    html.append('      <tr>')
    html.append('        <th>Platform</th>')
    html.append('        <th>Runner OS / Arch</th>')
    html.append('        <th>CPU</th>')
    html.append('        <th>JavaCPP (MiB/s)</th>')
    html.append('        <th>Panama (MiB/s)</th>')
    html.append('        <th>Upstream (MiB/s)</th>')
    html.append('        <th>Faster</th>')
    html.append('        <th>Speedup</th>')
    html.append('      </tr>')
    for row in platform_rows:
        r = row['results'][0]
        platform = row['platform']
        runner_os = safe_get(r, 'runnerOs', default='-')
        runner_arch = safe_get(r, 'runnerArch', default='-')
        cpu_model = safe_get(r, 'cpuModel', default='-')
        cpu_flags = safe_get(r, 'cpuFlags', default='-')
        cpu_display = cpu_model if cpu_model != '-' else cpu_flags
        if cpu_display == '-' or cpu_display == 'unknown':
            cpu_display = '-'

        javacpp_tp = row['javacppThroughput']
        panama_tp = row['panamaThroughput']
        upstream_tp = row['upstreamThroughput']
        faster = row['bestImplementation'] or '-'

        comparable = (row['javacpp'] and row['panama']
                      and not row['javacppStale'] and not row['panamaStale']
                      and javacpp_tp > 0 and panama_tp > 0)
        speedup = (f'{format_num(max(javacpp_tp, panama_tp) / min(javacpp_tp, panama_tp), 2)}x'
                   if comparable else '')
        faster = row['bestImplementation'] if comparable else '-'

        upstream_cell = ('<span style="color:#6a737d;">unsupported</span>' if row['upstreamUnsupported']
                         else escape(format_num(upstream_tp)) + stale_mark(row['upstreamStale']) if row['upstream'] else '-')

        html.append('      <tr>')
        html.append(f'        <td>{escape(platform)}</td>')
        html.append(f'        <td>{escape(runner_os)} / {escape(runner_arch)}</td>')
        html.append(f'        <td title="{escape(cpu_flags)}">{escape(cpu_display[:60])}</td>')
        javacpp_cell = escape(format_num(javacpp_tp)) + stale_mark(row['javacppStale']) if row['javacpp'] else '-'
        panama_cell = escape(format_num(panama_tp)) + stale_mark(row['panamaStale']) if row['panama'] else '-'
        html.append(f'        <td>{javacpp_cell}</td>')
        html.append(f'        <td>{panama_cell}</td>')
        html.append(f'        <td>{upstream_cell}</td>')
        html.append(f'        <td>{escape(faster)}</td>')
        html.append(f'        <td>{escape(speedup)}</td>')
        html.append('      </tr>')
    html.append('    </table>')

    stale_cells = []
    for row in platform_rows:
        for impl_name, flag, res in (('javacpp', row['javacppStale'], row['javacpp']),
                                     ('panama', row['panamaStale'], row['panama']),
                                     ('upstream', row['upstreamStale'], row['upstream'])):
            if flag and res:
                stale_cells.append(f"{row['platform']} / {impl_name} ({stale_note_for(res)})")
    if stale_cells:
        html.append(f'    <p style="font-size: 0.85rem; color: #6a737d;">† data from an earlier run: {escape("; ".join(stale_cells))}</p>')

    # Throughput chart
    html.append('    <h2>Throughput Comparison</h2>')
    current_throughputs = [tp for row in platform_rows for tp, stale in (
        (row['javacppThroughput'], row['javacppStale']),
        (row['panamaThroughput'], row['panamaStale']),
        (row['upstreamThroughput'], row['upstreamStale'])) if tp > 0 and not stale]
    if current_throughputs:
        max_tp = max(current_throughputs)
        for row in platform_rows:
            javacpp_value = None if not row['javacpp'] or row['javacppStale'] else row['javacppThroughput']
            panama_value = None if not row['panama'] or row['panamaStale'] else row['panamaThroughput']
            upstream_value = None if not row['upstream'] or row['upstreamStale'] else row['upstreamThroughput']
            html.append('    <div style="margin-bottom: 0.75rem;">')
            html.append(f'      <div style="margin-bottom: 0.25rem; font-size: 0.9rem;">{escape(row["platform"])}</div>')
            javacpp_width = (javacpp_value or 0) / max_tp * 100 if max_tp > 0 else 0
            panama_width = (panama_value or 0) / max_tp * 100 if max_tp > 0 else 0
            upstream_width = (upstream_value or 0) / max_tp * 100 if max_tp > 0 else 0
            html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">')
            html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">JavaCPP</div>')
            html.append('        <div class="bar-bg" style="flex: 1;">')
            html.append(f'          <div class="bar-fill" style="width: {javacpp_width:.1f}%;"></div>')
            html.append('        </div>')
            html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(javacpp_value))} MiB/s</div>')
            html.append('      </div>')
            html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">')
            html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">Panama</div>')
            html.append('        <div class="bar-bg" style="flex: 1;">')
            html.append(f'          <div class="bar-fill" style="width: {panama_width:.1f}%; background: #0969da;"></div>')
            html.append('        </div>')
            html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(panama_value))} MiB/s</div>')
            html.append('      </div>')
            html.append(f'      <div style="display: flex; align-items: center; gap: 0.5rem;">')
            html.append(f'        <div style="width: 5rem; font-size: 0.85rem;">Upstream</div>')
            if row['upstreamUnsupported']:
                html.append('        <div style="flex: 1; font-size: 0.85rem; color: #6a737d;">unsupported</div>')
            elif row['upstream']:
                html.append('        <div class="bar-bg" style="flex: 1;">')
                html.append(f'          <div class="bar-fill" style="width: {upstream_width:.1f}%; background: #6a737d;"></div>')
                html.append('        </div>')
                html.append(f'        <div style="width: 6rem; text-align: right; font-size: 0.85rem;">{escape(format_num(upstream_value))} MiB/s</div>')
            else:
                html.append('        <div style="flex: 1; font-size: 0.85rem; color: #6a737d;">-</div>')
            html.append('      </div>')
            html.append('    </div>')

    # Per-benchmark cross-platform comparison
    html.append('    <h2>Per-Benchmark Cross-Platform Comparison</h2>')
    for scenario in scenarios:
        html.append(f'    <h3>{escape(scenario)}</h3>')
        scenario_chart_rows = build_scenario_chart_rows(results, scenario)
        scenario_svg_link = f"scenarios/{sanitize_filename(scenario)}.svg"
        render_scenario_chart(html, scenario_chart_rows, scenario, scenario_svg_link)
        scenario_rows = build_scenario_rows(results, scenario)
        html.append('    <table class="scenario-table">')
        html.append('      <tr>')
        html.append('        <th>Platform</th>')
        html.append('        <th>Runner OS / Arch</th>')
        html.append('        <th>CPU</th>')
        html.append('        <th>Implementation</th>')
        html.append('        <th>Measurement Samples</th>')
        html.append('        <th>Elapsed (ms/op)</th>')
        html.append('        <th>Throughput (MiB/s)</th>')
        html.append('        <th>Ops/Second</th>')
        html.append('        <th>ns/Op</th>')
        html.append('        <th>Matches/Op</th>')
        html.append('      </tr>')
        for srow in scenario_rows:
            cls = 'best' if is_scenario_best(srow, scenario_rows) else ''
            html.append(f'      <tr class="{cls}">')
            html.append(f'        <td>{escape(srow["platform"])}</td>')
            html.append(f'        <td>{escape(srow["runner_os"])} / {escape(srow["runner_arch"])}</td>')
            html.append(f'        <td title="{escape(srow["cpu_flags"])}">{escape(srow["cpu_display"][:60])}</td>')
            if srow['unsupported']:
                html.append(f'        <td>{escape(srow["implementation"])}</td>')
                html.append(f'        <td colspan="6" style="color:#6a737d;">unsupported</td>')
            else:
                html.append(f'        <td>{escape(srow["implementation"])}{stale_mark(srow["stale"])}</td>')
                html.append(f'        <td>{escape(format_num(srow["iterations"], 0))}</td>')
                html.append(f'        <td>{escape(format_num(srow["elapsed"]))}</td>')
                html.append(f'        <td>{escape(format_num(srow["throughput"]))}</td>')
                html.append(f'        <td>{escape(format_num(srow["ops_per_second"]))}</td>')
                html.append(f'        <td>{escape(format_num(srow["ns_per_op"]))}</td>')
                html.append(f'        <td>{escape(format_num(srow["matches"], 0))}</td>')
            html.append('      </tr>')
        html.append('    </table>')

    # Per-platform details
    html.append('    <h2>Per-Platform Details</h2>')
    for row in platform_rows:
        platform = row['platform']
        html.append('    <div class="card">')
        html.append(f'      <h3>{escape(platform)}</h3>')
        html.append('      <table class="sub-table">')
        html.append('        <tr>')
        html.append('          <th>Implementation</th>')
        html.append('          <th>Throughput (MiB/s)</th>')
        html.append('          <th>Elapsed (ms)</th>')
        html.append('          <th>Matches/Op</th>')
        html.append('        </tr>')
        for impl_name in ['javacpp', 'panama', 'upstream']:
            r = row[impl_name] if impl_name in row else None
            if not r:
                continue
            if impl_name == 'upstream' and row['upstreamUnsupported']:
                reason = safe_get(r, 'reason', default='no native build for this platform')
                html.append(f'        <tr>')
                html.append(f'          <td><span class="impl-name">upstream</span></td>')
                html.append(f'          <td colspan="3" style="color:#6a737d;">unsupported — {escape(reason)}</td>')
                html.append('        </tr>')
                continue
            tp = throughput_for(r, fixed_scenario)
            elapsed = elapsed_for(r, fixed_scenario)
            matches = matches_for(r, fixed_scenario)
            fastest = row['bestImplementation'] == impl_name
            impl_stale = bool(row.get(impl_name + 'Stale'))
            cls = 'best' if fastest else ''
            badge = ' <span class="impl-badge fastest">fastest</span>' if fastest else ''
            html.append(f'        <tr class="{cls}">')
            html.append(f'          <td><span class="impl-name">{escape(impl_name)}</span>{stale_mark(impl_stale)}{badge}</td>')
            html.append(f'          <td>{escape(format_num(tp))}</td>')
            html.append(f'          <td>{escape(format_num(elapsed))}</td>')
            html.append(f'          <td>{escape(format_num(matches, 0))}</td>')
            html.append('        </tr>')
            html.append('        <tr>')
            html.append('          <td colspan="4">')
            for bench in safe_get(r, 'benchmarks', default=[]):
                html.append(f'            <h4>{escape(safe_get(bench, "name", default=""))}</h4>')
                html.append('            <div class="metrics">')
                metrics = safe_get(bench, 'metrics', default={})
                for key, value in metrics.items():
                    html.append('              <div class="metric">')
                    html.append(f'                <div class="metric-label">{escape(key)}</div>')
                    html.append(f'                <div class="metric-value">{escape(format_num(value))}</div>')
                    html.append('              </div>')
                html.append('            </div>')
            html.append('          </td>')
            html.append('        </tr>')
        html.append('      </table>')
        html.append('    </div>')

    # Raw data section
    html.append('    <h2>Raw Data</h2>')
    html.append('    <p>The following raw JSON files were aggregated to produce this report. They are also available as individual CI artifacts.</p>')
    html.append('    <ul>')
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        impl = implementation_for(r)
        detail = f'{platform} / {impl}'
        version = safe_get(r, 'artifactVersion', default=None)
        timestamp = safe_get(r, 'timestamp', default=None)
        extras = [str(x) for x in (version, timestamp) if x]
        if extras:
            detail += ' — ' + ' — '.join(extras)
        html.append(f'      <li>{escape(detail)}</li>')
    html.append('    </ul>')

    html.append('  </div>')
    html.append('</body>')
    html.append('</html>')

    os.makedirs(os.path.dirname(output_file) or '.', exist_ok=True)
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(html))


def is_scenario_best(srow, scenario_rows):
    if srow['unsupported'] or srow['stale'] or srow['implementation'] == 'upstream':
        return False
    peers = [row for row in scenario_rows
             if row['platform'] == srow['platform']
             and row['implementation'] in ('javacpp', 'panama')
             and not row['unsupported'] and not row['stale']]
    if len(peers) < 2:
        return False
    if srow['throughput'] and srow['throughput'] > 0:
        best = max((r['throughput'] for r in peers if r['throughput']), default=0)
        return srow['throughput'] == best
    if srow['ops_per_second'] and srow['ops_per_second'] > 0:
        best = max((r['ops_per_second'] for r in peers if r['ops_per_second']), default=0)
        return srow['ops_per_second'] == best
    return False


def main():
    if len(sys.argv) < 3:
        print('Usage: generate-performance-report.py <input-dir> <output-html> [native-version] [commit-sha]', file=sys.stderr)
        sys.exit(1)
    input_dir = sys.argv[1]
    output_file = sys.argv[2]
    native_version = sys.argv[3] if len(sys.argv) > 3 else os.environ.get('NATIVE_VERSION', 'unknown')
    commit_sha = sys.argv[4] if len(sys.argv) > 4 else os.environ.get('GITHUB_SHA', 'unknown')

    results = load_results(input_dir)
    if not results:
        print('No benchmark results found in input directory.', file=sys.stderr)
        sys.exit(1)

    generate_html(results, output_file, native_version, commit_sha)
    print(f'Performance report generated: {output_file}')


if __name__ == '__main__':
    main()
