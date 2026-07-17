#!/usr/bin/env python3
"""Merge benchmark results from the current run with the previous successful run.

Current-run results always win on a (platform, implementation) key. Entries
present only in the previous run are copied through with "stale": true injected
so the report can mark them as older data instead of dropping the row entirely.
"""
import json
import os
import shutil
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
        print('Usage: merge-benchmark-results.py <current-dir> <previous-dir> <out-dir>', file=sys.stderr)
        sys.exit(1)
    current_dir, previous_dir, out_dir = sys.argv[1], sys.argv[2], sys.argv[3]

    current = load_results(current_dir)
    previous = load_results(previous_dir)

    os.makedirs(out_dir, exist_ok=True)
    stale_count = 0
    for key, (path, data) in current.items():
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            f.write(content)

    for key, (path, data) in previous.items():
        if key in current:
            continue
        data['stale'] = True
        out_name = f"benchmark-result-{key[0]}-{key[1]}.json"
        with open(os.path.join(out_dir, out_name), 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2)
        stale_count += 1

    print(f"Merged {len(current)} current results with {stale_count} stale results from the previous run into {out_dir}")


if __name__ == '__main__':
    main()
