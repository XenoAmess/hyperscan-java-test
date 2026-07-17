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
import os
import sys


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
            platform = data.get('platform', 'unknown')
            impl = data.get('implementation') or implementation_from_name(name)
            results[(platform, impl)] = (path, data)
    return results


def implementation_from_name(name):
    lower = name.lower()
    if 'upstream' in lower:
        return 'upstream'
    if 'panama' in lower:
        return 'panama'
    return 'javacpp'


def main():
    if len(sys.argv) < 3:
        print('Usage: merge-benchmark-results.py <current-dir> <out-dir> [previous-dir...]', file=sys.stderr)
        sys.exit(1)
    current_dir, out_dir = sys.argv[1], sys.argv[2]
    previous_dirs = sys.argv[3:]

    current = load_results(current_dir)

    os.makedirs(out_dir, exist_ok=True)
    for key, (path, data) in current.items():
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            f.write(content)

    stale = {}
    for previous_dir in previous_dirs:
        for key, (path, data) in load_results(previous_dir).items():
            if key not in current and key not in stale:
                data['stale'] = True
                stale[key] = data

    for key, data in stale.items():
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2)

    print(f"Merged {len(current)} current results with {len(stale)} stale results from {len(previous_dirs)} previous run(s) into {out_dir}")


if __name__ == '__main__':
    main()
