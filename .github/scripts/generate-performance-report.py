#!/usr/bin/env python3
import json
import os
import sys
from html import escape
from datetime import datetime, timezone


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


def build_summary(results):
    rows = []
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        bench = safe_get(r, 'benchmarks', 0, default={})
        metrics = safe_get(bench, 'metrics', default={})
        throughput = safe_get(metrics, 'throughputMBpsAvg', default=0.0)
        elapsed = safe_get(metrics, 'elapsedMsAvg', default=0.0)
        rows.append({
            'platform': platform,
            'throughput': float(throughput or 0.0),
            'elapsed': float(elapsed or 0.0),
            'result': r
        })
    rows.sort(key=lambda x: x['throughput'], reverse=True)
    return rows


def generate_html(results, output_file, native_version, commit_sha):
    summary_rows = build_summary(results)
    best = summary_rows[0] if summary_rows else None

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
    html.append(f'      <span><strong>Platforms:</strong> {len(results)}</span>')
    html.append('    </div>')

    # Executive summary
    html.append('    <h2>Executive Summary</h2>')
    html.append('    <div class="summary-grid">')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Best throughput</div>')
    best_platform = best['platform'] if best else 'N/A'
    best_throughput = format_num(best['throughput'] if best else None)
    html.append(f'        <div class="value">{escape(best_platform)}</div>')
    html.append(f'        <div class="label">{best_throughput} MB/s</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Worst throughput</div>')
    worst = summary_rows[-1] if summary_rows else None
    worst_platform = worst['platform'] if worst else 'N/A'
    worst_throughput = format_num(worst['throughput'] if worst else None)
    html.append(f'        <div class="value">{escape(worst_platform)}</div>')
    html.append(f'        <div class="label">{worst_throughput} MB/s</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Performance range</div>')
    if best and worst and worst['throughput'] > 0:
        ratio = best['throughput'] / worst['throughput']
    else:
        ratio = None
    html.append(f'        <div class="value">{format_num(ratio, 2)}x</div>')
    html.append('        <div class="label">best vs worst</div>')
    html.append('      </div>')
    html.append('      <div class="summary-item">')
    html.append('        <div class="label">Benchmark workload</div>')
    workload = 'N/A'
    if results:
        bench = safe_get(results[0], 'benchmarks', 0, default={})
        metrics = safe_get(bench, 'metrics', default={})
        patterns = safe_get(metrics, 'patterns', default='N/A')
        input_bytes = safe_get(metrics, 'inputBytes', default='N/A')
        workload = f'{patterns} patterns / {input_bytes} bytes'
    html.append(f'        <div class="value">{escape(workload)}</div>')
    html.append('        <div class="label">per iteration</div>')
    html.append('      </div>')
    html.append('    </div>')

    # Cross-platform comparison table
    html.append('    <h2>Cross-Platform Comparison</h2>')
    html.append('    <table>')
    html.append('      <tr>')
    html.append('        <th>Rank</th>')
    html.append('        <th>Platform</th>')
    html.append('        <th>Runner OS / Arch</th>')
    html.append('        <th>CPU</th>')
    html.append('        <th>Throughput (MB/s)</th>')
    html.append('        <th>Elapsed (ms)</th>')
    html.append('        <th>Relative</th>')
    html.append('      </tr>')
    for idx, row in enumerate(summary_rows, start=1):
        r = row['result']
        platform = row['platform']
        bench = safe_get(r, 'benchmarks', 0, default={})
        metrics = safe_get(bench, 'metrics', default={})
        runner_os = safe_get(r, 'runnerOs', default='-')
        runner_arch = safe_get(r, 'runnerArch', default='-')
        cpu_model = safe_get(r, 'cpuModel', default='-')
        cpu_flags = safe_get(r, 'cpuFlags', default='-')
        cpu_display = cpu_model if cpu_model != '-' else cpu_flags
        if cpu_display == '-' or cpu_display == 'unknown':
            cpu_display = '-'
        elapsed = format_num(safe_get(metrics, 'elapsedMsAvg', default=0.0))
        throughput = format_num(safe_get(metrics, 'throughputMBpsAvg', default=0.0))
        relative = ''
        if best and best['throughput'] > 0 and row['throughput'] > 0:
            pct = row['throughput'] / best['throughput'] * 100
            relative = f'{format_num(pct, 1)}%'
        cls = 'best' if idx == 1 else ''
        html.append(f'      <tr class="{cls}">')
        html.append(f'        <td>{idx}</td>')
        html.append(f'        <td>{escape(platform)}</td>')
        html.append(f'        <td>{escape(runner_os)} / {escape(runner_arch)}</td>')
        html.append(f'        <td title="{escape(cpu_flags)}">{escape(cpu_display[:60])}</td>')
        html.append(f'        <td>{escape(throughput)}</td>')
        html.append(f'        <td>{escape(elapsed)}</td>')
        html.append(f'        <td>{escape(relative)}</td>')
        html.append('      </tr>')
    html.append('    </table>')

    # Throughput chart
    html.append('    <h2>Throughput Comparison</h2>')
    if best and best['throughput'] > 0:
        max_tp = best['throughput']
        for row in summary_rows:
            width_pct = row['throughput'] / max_tp * 100 if max_tp > 0 else 0
            html.append('    <div style="margin-bottom: 0.75rem;">')
            html.append(f'      <div style="margin-bottom: 0.25rem; font-size: 0.9rem;">{escape(row["platform"])} <span style="color:#656d76;">({format_num(row["throughput"])} MB/s)</span></div>')
            html.append('      <div class="bar-bg">')
            html.append(f'        <div class="bar-fill" style="width: {width_pct:.1f}%;"></div>')
            html.append('      </div>')
            html.append('    </div>')

    # Per-platform details
    html.append('    <h2>Per-Platform Details</h2>')
    for row in summary_rows:
        r = row['result']
        platform = row['platform']
        bench = safe_get(r, 'benchmarks', 0, default={})
        metrics = safe_get(bench, 'metrics', default={})
        html.append('    <div class="card">')
        html.append(f'      <h3>{escape(platform)}</h3>')
        html.append('      <div class="metrics">')
        for key, value in metrics.items():
            html.append('        <div class="metric">')
            html.append(f'          <div class="metric-label">{escape(key)}</div>')
            html.append(f'          <div class="metric-value">{escape(format_num(value))}</div>')
            html.append('        </div>')
        html.append('      </div>')
        html.append('    </div>')

    # Raw data section
    html.append('    <h2>Raw Data</h2>')
    html.append('    <p>The following raw JSON files were aggregated to produce this report. They are also available as individual CI artifacts.</p>')
    html.append('    <ul>')
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        html.append(f'      <li>{escape(platform)}</li>')
    html.append('    </ul>')

    html.append('  </div>')
    html.append('</body>')
    html.append('</html>')

    os.makedirs(os.path.dirname(output_file) or '.', exist_ok=True)
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(html))


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
