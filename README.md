# hyperscan-java-test

> **English** | [简体中文](README.zh.md)

Smoke and comparison tests for [hyperscan-java-native](https://github.com/XenoAmess/hyperscan-java-native)
(JavaCPP) and [hyperscan-java-panama](https://github.com/XenoAmess/hyperscan-java-panama)
across supported platforms and instruction-set tiers.

📊 **[View latest performance report](https://xenoamess.github.io/hyperscan-java-test/)**

[![Latest performance summary](https://xenoamess.github.io/hyperscan-java-test/performance-summary.svg)](https://xenoamess.github.io/hyperscan-java-test/)

## What is tested

- Native library loading and platform selection
- Direct Vectorscan JNI API (compile, scan, match) on both implementations
- Ported vectorscan unit-test suite (`vectorscan/unit/hyperscan`), dual-run on JavaCPP and Panama — see [docs/vectorscan-port-status.md](docs/vectorscan-port-status.md)
- Synthetic data: many random literal patterns, character classes, long inputs
- Real-world data: HTTP request/response parsing, nginx logs, simple security signatures
- JMH benchmarks (compile/scan scenarios and instruction-set granularity) on every supported ISA tier; multi-GiB scans are opt-in for manual runs
- Three-way benchmark comparison: fork native (JavaCPP), Panama, and upstream `com.gliwka.hyperscan:native` (`upstream-auto`). Upstream has no ISA-tier builds and no `windows-x86_64` classifier, so it runs once per Linux architecture and Windows reports `unsupported`

## Supported platforms

| OS      | Architecture | ISA tiers tested                                  |
|---------|--------------|---------------------------------------------------|
| Linux   | x86_64       | `baseline`, `avx2`, `linux-x86_64` (AVX-512)      |
| Linux   | arm64        | `baseline`, `linux-arm64` (SVE2)                  |
| Windows | x86_64       | `baseline`, `windows-x86_64` (AVX2)               |

## Running locally

```bash
mvn test
```

Force a specific ISA tier:

```bash
mvn test -Djavacpp.platform=linux-x86_64-baseline \
  -Dorg.bytedeco.javacpp.platform=linux-x86_64-baseline
```

Available `javacpp.platform` values:
- `linux-x86_64-baseline`
- `linux-x86_64-avx2`
- `linux-x86_64`
- `linux-arm64-baseline`
- `linux-arm64`
- `windows-x86_64-baseline`
- `windows-x86_64`

Set `NATIVE_VERSION` to a different native artifact release and test it:

```bash
mvn test -Dnative.version="$NATIVE_VERSION" \
  -Djavacpp.platform=linux-x86_64 \
  -Dorg.bytedeco.javacpp.platform=linux-x86_64
```

## CI

GitHub Actions runs the smoke-test matrix on pushes to `master` and release
branches, and on every PR. It can also be triggered manually with a selectable
native version and an opt-in multi-GiB benchmark flag.
On `master` and release branches, routine JMH benchmarks run on every platform
for JavaCPP and Panama (plus one auto-dispatched upstream run per Linux
architecture). Complete `master` results publish a detailed cross-platform
performance report to [GitHub Pages](https://xenoamess.github.io/hyperscan-java-test/).
