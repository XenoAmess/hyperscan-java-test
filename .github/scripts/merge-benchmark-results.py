#!/usr/bin/env python3
"""Merge benchmark results from the current run with previous successful runs.

Current-run results always win on a (platform, implementation) key. Entries
absent from the current run are backfilled from previous runs (checked in
recency order, first occurrence wins) and tagged with "stale": true so the
report can mark them as older data instead of dropping the row entirely.

Usage: merge-benchmark-results.py <current-dir> <out-dir> [previous-dir...]
Previous dirs are consulted in the order given (most recent first).
"""
import json
import math
import os
import sys

SCHEMA_VERSION = 3
FIXED_WORKLOAD_SCENARIO = 'ISA fixed workload (direct buffer)'
EXPECTED_SCENARIOS = {
    'compileSmallSet',
    'compileLargeSet',
    'wrapperHasMatchShortText',
    'wrapperScanShortText',
    'scanDirectByteBufferCounting',
    'wrapperScanLongText',
    'wrapperScanManyLiteralPatterns',
    'wrapper fixed workload (materialized matches)',
    FIXED_WORKLOAD_SCENARIO,
}
AVERAGE_TIME_SCENARIOS = {
    'wrapperScanLongText',
    'wrapperScanManyLiteralPatterns',
    'wrapper fixed workload (materialized matches)',
    FIXED_WORKLOAD_SCENARIO,
}
EXPECTED_KEYS = {
    *((platform, implementation)
      for platform in (
          'linux-x86_64-baseline', 'linux-x86_64-avx2', 'linux-x86_64',
          'linux-arm64-baseline', 'linux-arm64',
          'windows-x86_64-baseline', 'windows-x86_64'
      )
      for implementation in ('javacpp', 'panama')),
    ('linux-x86_64-upstream-auto', 'upstream'),
    ('linux-arm64-upstream-auto', 'upstream'),
    ('windows-x86_64-upstream-auto', 'upstream'),
}


def is_known_string(value):
    return (isinstance(value, str) and bool(value.strip())
            and value.strip().lower() != 'unknown')


def is_positive_int(value):
    return isinstance(value, int) and not isinstance(value, bool) and value > 0


def is_positive_finite_number(value):
    return (isinstance(value, (int, float)) and not isinstance(value, bool)
            and math.isfinite(value) and value > 0)


def has_expected_scenarios(data):
    if data.get('unsupported'):
        return True
    names = {benchmark.get('name') for benchmark in data.get('benchmarks', [])
             if isinstance(benchmark, dict)}
    return EXPECTED_SCENARIOS.issubset(names)


def has_valid_scenario_metrics(benchmark):
    name = benchmark.get('name')
    metrics = benchmark.get('metrics', {})
    if not is_positive_int(metrics.get('measurementSamples')):
        return False
    if metrics.get('scoreUnit') != 'ms/op':
        return False
    if name in AVERAGE_TIME_SCENARIOS:
        return (is_positive_int(metrics.get('patterns'))
                and is_positive_int(metrics.get('inputBytes'))
                and is_positive_finite_number(metrics.get('throughputMiBpsAvg')))
    return (is_positive_finite_number(metrics.get('opsPerSecond'))
            and is_positive_finite_number(metrics.get('nsPerOp')))


def is_complete_result(data, path):
    if not isinstance(data, dict):
        print(f"Warning: skipping non-object result: {path}", file=sys.stderr)
        return False
    platform = data.get('platform')
    implementation = data.get('implementation')
    if (platform, implementation) not in EXPECTED_KEYS:
        print(f"Warning: skipping result with unexpected platform/implementation: {path}",
              file=sys.stderr)
        return False
    unsupported = data.get('unsupported', False)
    if not isinstance(unsupported, bool):
        print(f"Warning: skipping result with non-boolean unsupported marker: {path}",
              file=sys.stderr)
        return False
    if unsupported:
        if (platform, implementation) != ('windows-x86_64-upstream-auto', 'upstream'):
            print(f"Warning: skipping unexpected unsupported marker: {path}", file=sys.stderr)
            return False
        reason = data.get('reason')
        if not isinstance(reason, str) or not reason.strip():
            print(f"Warning: skipping unsupported result without a reason: {path}", file=sys.stderr)
            return False
        return True
    if data.get('schemaVersion') != SCHEMA_VERSION:
        print(f"Warning: skipping incompatible schema in {path}", file=sys.stderr)
        return False
    identity_fields = ('benchmarkSuiteId', 'actualPlatform', 'nativeVersion',
                       'artifactVersion', 'commitSha')
    if any(not is_known_string(data.get(field)) for field in identity_fields):
        print(f"Warning: skipping result without suite/platform identity: {path}", file=sys.stderr)
        return False
    expected_actual = (platform.removesuffix('-upstream-auto')
                       if implementation == 'upstream' else platform)
    if data['actualPlatform'] != expected_actual:
        print(f"Warning: skipping result whose actual platform is {data['actualPlatform']}, "
              f"expected {expected_actual}: {path}", file=sys.stderr)
        return False
    benchmarks = data.get('benchmarks')
    if not isinstance(benchmarks, list) or not benchmarks:
        print(f"Warning: skipping empty result {path}", file=sys.stderr)
        return False
    if any(not isinstance(benchmark, dict) for benchmark in benchmarks):
        print(f"Warning: skipping result with malformed benchmarks: {path}", file=sys.stderr)
        return False
    names = [benchmark.get('name') for benchmark in benchmarks]
    if (any(not isinstance(name, str) or not name.strip() for name in names)
            or len(names) != len(set(names))):
        print(f"Warning: skipping result with missing/duplicate benchmark names: {path}", file=sys.stderr)
        return False
    if not has_expected_scenarios(data):
        missing = sorted(EXPECTED_SCENARIOS - set(names))
        print(f"Warning: skipping result without all expected benchmark scenarios: {path}; "
              f"missing: {', '.join(missing)}", file=sys.stderr)
        return False
    if any(not isinstance(benchmark.get('metrics'), dict) or not benchmark['metrics']
           for benchmark in benchmarks):
        print(f"Warning: skipping result with empty metrics: {path}", file=sys.stderr)
        return False
    expected_benchmarks = [benchmark for benchmark in benchmarks
                           if benchmark['name'] in EXPECTED_SCENARIOS]
    if any(not has_valid_scenario_metrics(benchmark) for benchmark in expected_benchmarks):
        print(f"Warning: skipping result with invalid standard scenario metrics: {path}",
              file=sys.stderr)
        return False
    fixed = next((benchmark for benchmark in benchmarks
                  if benchmark.get('name') == FIXED_WORKLOAD_SCENARIO), None)
    if fixed is None:
        print(f"Warning: skipping result without complete fixed-workload metrics: {path}",
              file=sys.stderr)
        return False
    metrics = fixed['metrics']
    if not all(is_positive_int(metrics.get(key)) for key in (
            'patterns', 'inputBytes', 'measurementSamples', 'matchesPerOperation')):
        print(f"Warning: skipping result with invalid fixed-workload counts: {path}", file=sys.stderr)
        return False
    if metrics.get('scoreUnit') != 'ms/op':
        print(f"Warning: skipping result with invalid fixed-workload score unit: {path}", file=sys.stderr)
        return False
    if not is_positive_finite_number(metrics.get('throughputMiBpsAvg')):
        print(f"Warning: skipping result with invalid fixed-workload throughput: {path}", file=sys.stderr)
        return False
    return True


def compatible_stale(candidate, workload_reference, platform_reference=None,
                     implementation_reference=None):
    workload_fields = ('schemaVersion', 'benchmarkSuiteId')
    if any(candidate.get(field) != workload_reference.get(field) for field in workload_fields):
        return False
    if benchmark_signature(candidate) != benchmark_signature(workload_reference):
        return False
    platform_fields = (
        'actualPlatform', 'runnerOs', 'runnerArch', 'cpuModel', 'cpuFlags',
        'javaVersion', 'javaVmName', 'nativeVersion'
    )
    if platform_reference is not None and any(
            candidate.get(field) != platform_reference.get(field)
            for field in platform_fields):
        return False
    if implementation_reference is None:
        return False
    implementation_fields = ('artifactVersion',)
    return all(
        candidate.get(field) == implementation_reference.get(field)
        for field in implementation_fields)


def benchmark_signature(data):
    identity_metrics = (
        'patterns', 'inputBytes', 'measurementSamples',
        'matchesPerOperation', 'operationsPerInvocation', 'scoreUnit'
    )
    return sorted(
        (benchmark.get('name'),) + tuple(benchmark.get('metrics', {}).get(key)
                                        for key in identity_metrics)
        for benchmark in data.get('benchmarks', [])
    )


def load_results(input_dir):
    results = {}
    if not input_dir or not os.path.isdir(input_dir):
        return results
    for root, _, files in os.walk(input_dir):
        for name in files:
            if not name.endswith('.json'):
                continue
            path = os.path.join(root, name)
            try:
                with open(path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
            except Exception as e:
                print(f"Warning: failed to parse {path}: {e}", file=sys.stderr)
                continue
            if not is_complete_result(data, path):
                continue
            platform = data['platform']
            impl = data['implementation']
            if (platform, impl) in results:
                raise ValueError(f"duplicate result for {platform}/{impl}: {path}")
            results[(platform, impl)] = (path, data)
    return results


def collect_stale_results(current, previous_dirs):
    fresh = [data for _, data in current.values() if not data.get('unsupported')]
    if not fresh:
        return {}

    workload_reference = fresh[0]
    platform_references = {}
    implementation_references = {}
    for (platform, implementation), (_, data) in current.items():
        if not data.get('unsupported'):
            platform_references.setdefault(platform, data)
            implementation_references.setdefault(implementation, data)

    stale = {}
    for previous_dir in previous_dirs:
        for key, (_, data) in load_results(previous_dir).items():
            platform, implementation = key
            if (key in current or key in stale):
                continue
            if data.get('unsupported'):
                data['stale'] = True
                stale[key] = data
                continue
            if not compatible_stale(
                    data,
                    workload_reference,
                    platform_references.get(platform),
                    implementation_references.get(implementation)):
                continue
            data['stale'] = True
            stale[key] = data
            # A wholly skipped platform has no current-run environment to use as
            # a reference. Anchor its remaining implementations to the first
            # compatible result recovered from the same or a newer prior run.
            platform_references.setdefault(platform, data)
            implementation_references.setdefault(implementation, data)
    return stale


def has_complete_coverage(current, stale):
    if not EXPECTED_KEYS.issubset(set(current) | set(stale)):
        return False
    merged = {key: data for key, (_, data) in current.items()}
    merged.update(stale)
    return all(has_expected_scenarios(merged[key]) for key in EXPECTED_KEYS)


def matching_current_commit(results, expected_commit):
    if not expected_commit:
        return results
    matching = {}
    for key, value in results.items():
        path, data = value
        if data.get('unsupported') or data.get('commitSha') == expected_commit:
            matching[key] = value
        else:
            print(f"Warning: skipping current result from another commit: {path}", file=sys.stderr)
    return matching


def main():
    if len(sys.argv) < 3:
        print('Usage: merge-benchmark-results.py <current-dir> <out-dir> [previous-dir...]', file=sys.stderr)
        sys.exit(1)
    current_dir, out_dir = sys.argv[1], sys.argv[2]
    previous_dirs = sys.argv[3:]

    current = matching_current_commit(load_results(current_dir), os.environ.get('GITHUB_SHA'))

    os.makedirs(out_dir, exist_ok=True)
    for key, (path, data) in current.items():
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            f.write(content)

    stale = collect_stale_results(current, previous_dirs)

    for key, data in stale.items():
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2)

    fresh_count = sum(1 for _, data in current.values() if not data.get('unsupported'))
    complete = has_complete_coverage(current, stale)
    github_output = os.environ.get('GITHUB_OUTPUT')
    if github_output:
        with open(github_output, 'a', encoding='utf-8') as output:
            output.write(f"count={fresh_count}\n")
            output.write(f"complete={'true' if complete else 'false'}\n")
    if fresh_count == 0:
        print("Warning: no complete fresh benchmark results; keeping the previous report.",
              file=sys.stderr)
    elif not complete:
        missing = sorted(f'{platform}/{implementation}'
                         for platform, implementation in EXPECTED_KEYS - (set(current) | set(stale)))
        print(f"Warning: incomplete benchmark coverage; keeping the previous report. Missing: {', '.join(missing)}",
              file=sys.stderr)
    print(f"Merged {len(current)} current results with {len(stale)} stale results from {len(previous_dirs)} previous run(s) into {out_dir}")


if __name__ == '__main__':
    main()
