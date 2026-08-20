#!/usr/bin/env python3
import importlib.util
import json
import subprocess
import tempfile
import unittest
from pathlib import Path


def load_script(name, filename):
    spec = importlib.util.spec_from_file_location(name, Path(__file__).with_name(filename))
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


merge = load_script('merge_benchmark_results', 'merge-benchmark-results.py')
report = load_script('generate_performance_report', 'generate-performance-report.py')
svg = load_script('generate_performance_svg', 'generate-performance-svg.py')
CRASH_CHECK = Path(__file__).with_name('check-crash-logs.sh')

SCENARIO = 'ISA fixed workload (direct buffer)'


def result(platform='linux-x86_64-baseline', implementation='javacpp', throughput=100.0,
           commit='abc123'):
    actual_platform = (platform.removesuffix('-upstream-auto')
                       if implementation == 'upstream' else platform)
    benchmarks = [{
        'name': SCENARIO,
        'metrics': {
            'patterns': 500,
            'inputBytes': 20480,
            'measurementSamples': 10,
            'matchesPerOperation': 50,
            'scoreUnit': 'ms/op',
            'throughputMiBpsAvg': throughput,
        },
    }]
    for name in sorted(merge.EXPECTED_SCENARIOS - {SCENARIO}):
        metrics = {
            'measurementSamples': 10,
            'scoreUnit': 'ms/op',
        }
        if name in merge.AVERAGE_TIME_SCENARIOS:
            metrics.update({
                'patterns': 500,
                'inputBytes': 20480,
                'throughputMiBpsAvg': throughput,
            })
        else:
            metrics.update({
                'opsPerSecond': throughput,
                'nsPerOp': 1_000_000_000 / throughput,
            })
        benchmarks.append({'name': name, 'metrics': metrics})
    return {
        'schemaVersion': 3,
        'benchmarkSuiteId': 'hyperscan-jmh-v3',
        'platform': platform,
        'actualPlatform': actual_platform,
        'nativeVersion': '5.4.2',
        'artifactVersion': '1.0',
        'commitSha': commit,
        'runnerOs': 'Linux',
        'runnerArch': 'amd64',
        'cpuModel': 'test cpu',
        'cpuFlags': 'sse4_2 popcnt avx avx2',
        'javaVersion': '25',
        'javaVmName': 'OpenJDK 64-Bit Server VM',
        'implementation': implementation,
        'benchmarks': benchmarks,
    }


class MergeBenchmarkResultsTest(unittest.TestCase):
    def test_compatible_stale_requires_same_environment_and_workload(self):
        candidate = result(implementation='panama')
        platform_reference = result(implementation='javacpp')
        implementation_reference = result('linux-arm64-baseline', 'panama')

        self.assertTrue(merge.compatible_stale(
            candidate, platform_reference, platform_reference, implementation_reference))

        previous_commit = json.loads(json.dumps(candidate))
        previous_commit['commitSha'] = 'older-commit'
        self.assertTrue(merge.compatible_stale(
            previous_commit, platform_reference, platform_reference, implementation_reference))

        changed_suite = json.loads(json.dumps(candidate))
        changed_suite['benchmarkSuiteId'] = 'hyperscan-jmh-v4'
        self.assertFalse(merge.compatible_stale(
            changed_suite, platform_reference, platform_reference, implementation_reference))

        changed_workload = json.loads(json.dumps(candidate))
        changed_workload['benchmarks'][0]['metrics']['inputBytes'] += 1
        self.assertFalse(merge.compatible_stale(
            changed_workload, platform_reference, platform_reference, implementation_reference))

        changed_artifact = json.loads(json.dumps(candidate))
        changed_artifact['artifactVersion'] = '2.0'
        self.assertFalse(merge.compatible_stale(
            changed_artifact, platform_reference, platform_reference, implementation_reference))

        cross_platform = json.loads(json.dumps(candidate))
        cross_platform['actualPlatform'] = 'windows-x86_64'
        cross_platform['nativeVersion'] = '5.4.2'
        cross_platform_implementation = json.loads(json.dumps(implementation_reference))
        cross_platform_implementation['nativeVersion'] = '5.4.12'
        self.assertTrue(merge.compatible_stale(
            cross_platform, platform_reference, None, cross_platform_implementation))

        same_platform_reference = json.loads(json.dumps(cross_platform))
        same_platform_reference['nativeVersion'] = '5.4.12'
        self.assertFalse(merge.compatible_stale(
            cross_platform, platform_reference, same_platform_reference,
            cross_platform_implementation))

    def test_entire_skipped_platform_is_backfilled_across_commits(self):
        current_data = result()
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', current_data),
            ('linux-arm64-baseline', 'panama'): (
                'current-panama.json', result('linux-arm64-baseline', 'panama')),
        }

        with tempfile.TemporaryDirectory() as previous_dir:
            previous = Path(previous_dir)
            for implementation in ('javacpp', 'panama'):
                data = result('linux-x86_64', implementation, commit='older-commit')
                Path(previous, f'benchmark-result-{implementation}.json').write_text(
                    json.dumps(data), encoding='utf-8')

            stale = merge.collect_stale_results(current, [previous_dir])

        self.assertEqual(
            {('linux-x86_64', 'javacpp'), ('linux-x86_64', 'panama')},
            set(stale))
        self.assertTrue(all(data['stale'] for data in stale.values()))

    def test_missing_upstream_without_current_version_reference_is_not_backfilled(self):
        current_data = result()
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', current_data),
        }

        with tempfile.TemporaryDirectory() as previous_dir:
            upstream = result(
                'linux-x86_64-upstream-auto', 'upstream', commit='older-commit')
            Path(previous_dir, 'benchmark-result-upstream.json').write_text(
                json.dumps(upstream), encoding='utf-8')

            stale = merge.collect_stale_results(current, [previous_dir])

        self.assertNotIn(('linux-x86_64-upstream-auto', 'upstream'), stale)

    def test_missing_upstream_with_current_version_reference_is_backfilled(self):
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', result()),
            ('linux-arm64-upstream-auto', 'upstream'): (
                'current-upstream.json', result(
                    'linux-arm64-upstream-auto', 'upstream')),
        }

        with tempfile.TemporaryDirectory() as previous_dir:
            upstream = result(
                'linux-x86_64-upstream-auto', 'upstream', commit='older-commit')
            Path(previous_dir, 'benchmark-result-upstream.json').write_text(
                json.dumps(upstream), encoding='utf-8')

            stale = merge.collect_stale_results(current, [previous_dir])

        self.assertIn(('linux-x86_64-upstream-auto', 'upstream'), stale)

    def test_unsupported_marker_is_backfilled_without_benchmark_schema(self):
        current_data = result()
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', current_data),
        }
        marker = {
            'platform': 'windows-x86_64-upstream-auto',
            'implementation': 'upstream',
            'unsupported': True,
            'reason': 'not published',
            'benchmarks': [],
        }

        with tempfile.TemporaryDirectory() as previous_dir:
            Path(previous_dir, 'benchmark-result-upstream.json').write_text(
                json.dumps(marker), encoding='utf-8')
            stale = merge.collect_stale_results(current, [previous_dir])

        self.assertIn(('windows-x86_64-upstream-auto', 'upstream'), stale)

    def test_partial_schema_migration_is_not_publishable(self):
        current_data = result()
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', current_data),
        }

        self.assertFalse(merge.has_complete_coverage(current, {}))

        complete = {
            key: ('result.json', current_data)
            for key in merge.EXPECTED_KEYS
        }
        self.assertTrue(merge.has_complete_coverage(complete, {}))

    def test_incomplete_results_are_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory, 'benchmark-result.json')
            path.write_text(json.dumps({'schemaVersion': 1, 'benchmarks': []}), encoding='utf-8')
            self.assertEqual({}, merge.load_results(directory))

    def test_fixed_workload_without_match_count_is_rejected(self):
        incomplete = result()
        del incomplete['benchmarks'][0]['metrics']['matchesPerOperation']
        with tempfile.TemporaryDirectory() as directory:
            path = Path(directory, 'benchmark-result.json')
            path.write_text(json.dumps(incomplete), encoding='utf-8')
            self.assertEqual({}, merge.load_results(directory))

    def test_result_without_all_standard_scenarios_is_rejected(self):
        incomplete = result()
        incomplete['benchmarks'] = incomplete['benchmarks'][:1]
        self.assertFalse(merge.is_complete_result(incomplete, 'incomplete.json'))

    def test_result_with_malformed_standard_scenario_is_rejected(self):
        malformed = result()
        scenario = next(benchmark for benchmark in malformed['benchmarks']
                        if benchmark['name'] == 'compileSmallSet')
        del scenario['metrics']['opsPerSecond']
        self.assertFalse(merge.is_complete_result(malformed, 'malformed.json'))

    def test_mismatched_actual_platform_is_rejected(self):
        mismatched = result()
        mismatched['actualPlatform'] = 'linux-x86_64-avx2'
        self.assertFalse(merge.is_complete_result(mismatched, 'mismatched.json'))

    def test_unknown_artifact_identity_is_rejected(self):
        unknown = result()
        unknown['artifactVersion'] = 'unknown'
        self.assertFalse(merge.is_complete_result(unknown, 'unknown.json'))

    def test_malformed_fixed_workload_metrics_are_rejected(self):
        invalid_values = {
            'matchesPerOperation': '50',
            'throughputMiBpsAvg': float('nan'),
            'scoreUnit': '',
        }
        for metric, value in invalid_values.items():
            with self.subTest(metric=metric):
                malformed = result()
                malformed['benchmarks'][0]['metrics'][metric] = value
                self.assertFalse(merge.is_complete_result(malformed, 'malformed.json'))

    def test_current_results_must_match_workflow_commit(self):
        current = {
            ('linux-x86_64-baseline', 'javacpp'): ('current.json', result(commit='current')),
            ('linux-x86_64-baseline', 'panama'): (
                'older.json', result(implementation='panama', commit='older')),
        }
        matching = merge.matching_current_commit(current, 'current')
        self.assertEqual({('linux-x86_64-baseline', 'javacpp')}, set(matching))

    def test_malformed_json_shapes_are_rejected(self):
        malformed_results = [
            [],
            {**result(), 'benchmarks': {}},
            {**result(), 'benchmarks': [None]},
        ]
        for malformed in malformed_results:
            with self.subTest(result=malformed):
                self.assertFalse(merge.is_complete_result(malformed, 'malformed.json'))

    def test_unsupported_marker_requires_boolean_true(self):
        marker = {
            'platform': 'windows-x86_64-upstream-auto',
            'implementation': 'upstream',
            'unsupported': 'false',
            'reason': 'not published',
            'benchmarks': [],
        }
        self.assertFalse(merge.is_complete_result(marker, 'marker.json'))


class PerformanceReportTest(unittest.TestCase):
    def test_winner_is_limited_to_fresh_bindings_on_same_platform(self):
        results = [
            result(throughput=10),
            result(implementation='panama', throughput=5),
            result('linux-arm64-baseline', 'javacpp', throughput=100),
            result('linux-arm64-baseline', 'panama', throughput=200),
            result('linux-x86_64-upstream-auto', 'upstream', throughput=1000),
        ]
        rows = report.build_scenario_rows(results, SCENARIO)
        x86_javacpp = next(row for row in rows
                           if row['platform'] == 'linux-x86_64-baseline'
                           and row['implementation'] == 'javacpp')
        upstream = next(row for row in rows if row['implementation'] == 'upstream')

        self.assertTrue(report.is_scenario_best(x86_javacpp, rows))
        self.assertFalse(report.is_scenario_best(upstream, rows))

        x86_javacpp['stale'] = True
        self.assertFalse(report.is_scenario_best(x86_javacpp, rows))

    def test_missing_metrics_remain_missing(self):
        self.assertIsNone(report.throughput_for({}, SCENARIO))
        self.assertIsNone(svg.throughput_for({}, SCENARIO))

        rows, _ = svg.build_platform_rows([result()], 'missing scenario')
        self.assertIsNone(rows[0]['javacpp'])

    def test_platform_winner_requires_two_fresh_bindings(self):
        javacpp = result(throughput=10)
        rows = report.build_platform_summary([javacpp], SCENARIO)
        self.assertIsNone(rows[0]['bestImplementation'])

        panama = result(implementation='panama', throughput=20)
        rows = report.build_platform_summary([javacpp, panama], SCENARIO)
        self.assertEqual('panama', rows[0]['bestImplementation'])

        panama['stale'] = True
        rows = report.build_platform_summary([javacpp, panama], SCENARIO)
        self.assertIsNone(rows[0]['bestImplementation'])

    def test_html_has_no_cross_platform_rank(self):
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory, 'index.html')
            report.generate_html([result()], str(output), '1.0', 'abc123')
            html = output.read_text(encoding='utf-8')
            self.assertNotIn('<th>Rank</th>', html)
            self.assertIn('<th>Matches/Op</th>', html)
            self.assertIn('Rows may come from different hosted VMs', html)

    def test_svg_subtitle_identifies_scenario(self):
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory, 'scenario.svg')
            svg.generate_svg([result()], str(output), '1.0', 'abc123', SCENARIO)
            self.assertIn(SCENARIO, output.read_text(encoding='utf-8'))


class CrashLogCheckTest(unittest.TestCase):
    @staticmethod
    def run_check(directory):
        return subprocess.run(
            ['bash', str(CRASH_CHECK)], cwd=directory, capture_output=True,
            text=True, check=False)

    @staticmethod
    def write_dumpstream(directory, content):
        reports = Path(directory, 'target', 'surefire-reports')
        reports.mkdir(parents=True)
        Path(reports, 'test.dumpstream').write_bytes(content.encode('utf-8'))

    def test_benign_windows_classpath_dumpstream_is_ignored(self):
        warning = (
            '# Created at 2026-08-20T03:41:25.139\r\n'
            "Boot Manifest-JAR contains absolute paths in classpath 'D:\\\\a\\\\target\\\\test-classes'\r\n"
            'Hint: <argLine>-Djdk.net.URLClassPath.disableClassPathURLCheck=true</argLine>\r\n'
            "'other' has different root\r\n\r\n"
        )
        with tempfile.TemporaryDirectory() as directory:
            self.write_dumpstream(directory, warning + warning)
            completed = self.run_check(directory)

        self.assertEqual(0, completed.returncode, completed.stdout + completed.stderr)
        self.assertIn('Ignoring benign Surefire cross-drive classpath diagnostic', completed.stdout)

    def test_dumpstream_with_additional_content_still_fails(self):
        warning = (
            '# Created at 2026-08-20T03:41:25.139\n'
            "Boot Manifest-JAR contains absolute paths in classpath 'D:\\\\a\\\\target\\\\test-classes'\n"
            'Hint: <argLine>-Djdk.net.URLClassPath.disableClassPathURLCheck=true</argLine>\n'
            "'other' has different root\n\n"
        )
        with tempfile.TemporaryDirectory() as directory:
            self.write_dumpstream(directory, warning + 'unexpected fork output\n')
            completed = self.run_check(directory)

        self.assertEqual(1, completed.returncode)
        self.assertIn('test.dumpstream', completed.stdout)

    def test_hotspot_error_log_still_fails(self):
        with tempfile.TemporaryDirectory() as directory:
            Path(directory, 'hs_err_pid1.log').write_text('crash', encoding='utf-8')
            completed = self.run_check(directory)

        self.assertEqual(1, completed.returncode)
        self.assertIn('hs_err_pid1.log', completed.stdout)


if __name__ == '__main__':
    unittest.main()
