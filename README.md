# hyperscan-java-test

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
- JMH benchmarks (compile/scan scenarios, multi-GB large scans, instruction-set granularity) on every supported ISA tier
- Three-way benchmark comparison: fork native (JavaCPP), Panama, and upstream `com.gliwka.hyperscan:native` (`upstream`). Upstream has no ISA-tier builds and no `windows-x86_64` classifier — Windows rows report `unsupported`

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
mvn test -Djavacpp.platform=linux-x86_64-baseline
```

Available `javacpp.platform` values:
- `linux-x86_64-baseline`
- `linux-x86_64-avx2`
- `linux-x86_64`
- `linux-arm64-baseline`
- `linux-arm64`
- `windows-x86_64-baseline`
- `windows-x86_64`

Test a different native artifact version:

```bash
mvn test -Dnative.version=5.4.12-2.0.4-x7 -Djavacpp.platform=linux-x86_64
```

## CI

GitHub Actions runs the smoke-test matrix on every push/PR and can be triggered
manually with a selectable native version. On `master`, JMH benchmarks run
separately for JavaCPP and Panama on every platform, and a detailed
cross-platform performance report is published to
[GitHub Pages](https://xenoamess.github.io/hyperscan-java-test/).
