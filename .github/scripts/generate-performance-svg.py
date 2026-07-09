#!/usr/bin/env python3
import json
import os
import sys
from html import escape
from datetime import datetime, timezone


FIXED_WORKLOAD_SCENARIO = 'ISA granularity benchmark'


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


def implementation_for(result):
    impl = safe_get(result, 'implementation')
    if impl:
        return impl
    source = result.get('_source', '')
    lower = source.lower()
    if 'panama' in lower:
        return 'panama'
    if 'javacpp' in lower:
        return 'javacpp'
    return 'javacpp'


def find_benchmark(result, scenario_name):
    for bench in safe_get(result, 'benchmarks', default=[]):
        if safe_get(bench, 'name') == scenario_name:
            return bench
    return None


def throughput_for(result, scenario_name):
    bench = find_benchmark(result, scenario_name)
    if bench:
        value = safe_get(bench, 'metrics', 'throughputMBpsAvg', default=None)
        if value is None:
            value = safe_get(bench, 'metrics', 'throughputMBps', default=0.0)
        return float(value or 0.0)
    return 0.0


def fixed_workload_scenario(results):
    names = set()
    for r in results:
        for bench in safe_get(r, 'benchmarks', default=[]):
            name = safe_get(bench, 'name')
            if name:
                names.add(name)
    if FIXED_WORKLOAD_SCENARIO in names:
        return FIXED_WORKLOAD_SCENARIO
    for name in sorted(names):
        for r in results:
            if safe_get(find_benchmark(r, name), 'metrics', 'throughputMBpsAvg'):
                return name
    return sorted(names)[0] if names else None


def build_platform_rows(results, scenario_name):
    by_platform = {}
    for r in results:
        platform = safe_get(r, 'platform', default='unknown')
        impl = implementation_for(r)
        by_platform.setdefault(platform, {})[impl] = r

    rows = []
    for platform, impls in by_platform.items():
        javacpp_tp = throughput_for(impls.get('javacpp'), scenario_name) if impls.get('javacpp') else 0.0
        panama_tp = throughput_for(impls.get('panama'), scenario_name) if impls.get('panama') else 0.0
        best_tp = max(javacpp_tp, panama_tp)
        rows.append({
            'platform': platform,
            'javacpp': javacpp_tp,
            'panama': panama_tp,
            'bestThroughput': best_tp
        })

    rows.sort(key=lambda x: x['bestThroughput'], reverse=True)
    return rows


def generate_svg(results, output_file, native_version, commit_sha, scenario_name):
    rows = build_platform_rows(results, scenario_name)
    if not rows:
        print('No results found', file=sys.stderr)
        sys.exit(1)

    max_tp = max(row['bestThroughput'] for row in rows) if rows else 1
    if max_tp <= 0:
        max_tp = 1

    width = 800
    top_margin = 90
    bottom_margin = 40
    left_margin = 170
    right_margin = 100
    bar_height = 20
    bar_gap = 5
    group_gap = 18
    chart_height = len(rows) * (bar_height * 2 + bar_gap + group_gap) + group_gap
    height = top_margin + chart_height + bottom_margin

    generated_at = datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S UTC')
    native_version = native_version or safe_get(results[0], 'nativeVersion', default='unknown')
    commit_sha = commit_sha or safe_get(results[0], 'commitSha', default='unknown')
    commit_short = commit_sha[:7] if commit_sha and len(commit_sha) > 7 else commit_sha

    svg = []
    svg.append(f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}" role="img" aria-label="Cross-platform performance summary">')
    svg.append('  <defs>')
    svg.append('    <linearGradient id="javacppGradient" x1="0%" y1="0%" x2="100%" y2="0%">')
    svg.append('      <stop offset="0%" style="stop-color:#2ea043;stop-opacity:1" />')
    svg.append('      <stop offset="100%" style="stop-color:#3fb950;stop-opacity:1" />')
    svg.append('    </linearGradient>')
    svg.append('    <linearGradient id="panamaGradient" x1="0%" y1="0%" x2="100%" y2="0%">')
    svg.append('      <stop offset="0%" style="stop-color:#0969da;stop-opacity:1" />')
    svg.append('      <stop offset="100%" style="stop-color:#54aeff;stop-opacity:1" />')
    svg.append('    </linearGradient>')
    svg.append('  </defs>')

    svg.append(f'  <rect width="{width}" height="{height}" fill="#f6f8fa" rx="6" />')
    svg.append(f'  <text x="{width / 2}" y="30" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="18" font-weight="bold" text-anchor="middle" fill="#1f2328">Hyperscan Java Native Performance Summary</text>')

    subtitle = f"Native {native_version}  ·  {len(rows)} platforms  ·  {scenario_name}  ·  commit {commit_short}"
    svg.append(f'  <text x="{width / 2}" y="52" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="11" text-anchor="middle" fill="#656d76">{escape(subtitle)}</text>')

    legend_x = left_margin
    legend_y = 74
    svg.append(f'  <rect x="{legend_x}" y="{legend_y - 10}" width="12" height="12" fill="url(#javacppGradient)" rx="2" />')
    svg.append(f'  <text x="{legend_x + 18}" y="{legend_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="11" fill="#24292f">JavaCPP</text>')
    svg.append(f'  <rect x="{legend_x + 75}" y="{legend_y - 10}" width="12" height="12" fill="url(#panamaGradient)" rx="2" />')
    svg.append(f'  <text x="{legend_x + 93}" y="{legend_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="11" fill="#24292f">Panama</text>')

    chart_width = width - left_margin - right_margin

    for idx, row in enumerate(rows):
        group_y = top_margin + group_gap + idx * (bar_height * 2 + bar_gap + group_gap)
        platform = row['platform']
        javacpp_tp = row['javacpp']
        panama_tp = row['panama']
        javacpp_width = (javacpp_tp / max_tp) * chart_width
        panama_width = (panama_tp / max_tp) * chart_width

        label_y = group_y + (bar_height * 2 + bar_gap) / 2 + 4
        svg.append(f'  <text x="{left_margin - 10}" y="{label_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="12" font-weight="600" text-anchor="end" fill="#24292f">{escape(platform)}</text>')

        svg.append(f'  <rect x="{left_margin}" y="{group_y}" width="{chart_width}" height="{bar_height}" fill="#e1e4e8" rx="4" />')
        svg.append(f'  <rect x="{left_margin}" y="{group_y + bar_height + bar_gap}" width="{chart_width}" height="{bar_height}" fill="#e1e4e8" rx="4" />')

        if javacpp_width > 0:
            svg.append(f'  <rect x="{left_margin}" y="{group_y}" width="{javacpp_width:.1f}" height="{bar_height}" fill="url(#javacppGradient)" rx="4" />')
        if panama_width > 0:
            svg.append(f'  <rect x="{left_margin}" y="{group_y + bar_height + bar_gap}" width="{panama_width:.1f}" height="{bar_height}" fill="url(#panamaGradient)" rx="4" />')

        javacpp_value_y = group_y + bar_height / 2 + 4
        javacpp_value_text = f'{format_num(javacpp_tp)} MB/s'
        javacpp_value_x = left_margin + min(javacpp_width, chart_width) + 6
        if javacpp_width + 85 > chart_width:
            javacpp_value_x = left_margin + chart_width + 6
        svg.append(f'  <text x="{javacpp_value_x}" y="{javacpp_value_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="11" font-weight="600" fill="#1a7f37">{escape(javacpp_value_text)}</text>')

        panama_value_y = group_y + bar_height + bar_gap + bar_height / 2 + 4
        panama_value_text = f'{format_num(panama_tp)} MB/s'
        panama_value_x = left_margin + min(panama_width, chart_width) + 6
        if panama_width + 85 > chart_width:
            panama_value_x = left_margin + chart_width + 6
        svg.append(f'  <text x="{panama_value_x}" y="{panama_value_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="11" font-weight="600" fill="#0969da">{escape(panama_value_text)}</text>')

    footer_y = height - 15
    svg.append(f'  <text x="{width / 2}" y="{footer_y}" font-family="-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica, Arial, sans-serif" font-size="10" text-anchor="middle" fill="#656d76">Generated {escape(generated_at)}  ·  Full report at xenoamess.github.io/hyperscan-java-test</text>')

    svg.append('</svg>')

    os.makedirs(os.path.dirname(output_file) or '.', exist_ok=True)
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(svg))
    print(f'Performance summary SVG generated: {output_file}')


def main():
    if len(sys.argv) < 3:
        print('Usage: generate-performance-svg.py <input-dir> <output-svg> [native-version] [commit-sha] [scenario]', file=sys.stderr)
        sys.exit(1)
    input_dir = sys.argv[1]
    output_file = sys.argv[2]
    native_version = sys.argv[3] if len(sys.argv) > 3 else os.environ.get('NATIVE_VERSION', 'unknown')
    commit_sha = sys.argv[4] if len(sys.argv) > 4 else os.environ.get('GITHUB_SHA', 'unknown')
    scenario = sys.argv[5] if len(sys.argv) > 5 else None

    results = load_results(input_dir)
    if not results:
        print('No benchmark results found in input directory.', file=sys.stderr)
        sys.exit(1)

    if scenario is None:
        scenario = fixed_workload_scenario(results)
    generate_svg(results, output_file, native_version, commit_sha, scenario)


if __name__ == '__main__':
    main()
