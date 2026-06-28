# hyperscan-java-test

Smoke tests for [hyperscan-java-native](https://github.com/XenoAmess/hyperscan-java-native)
across supported platforms and instruction-set tiers.

## What is tested

- Native library loading and platform selection
- Direct Vectorscan JNI API (compile, scan, match)
- Synthetic data: many random literal patterns, character classes, long inputs
- Real-world data: HTTP request/response parsing, nginx logs, simple security signatures
- Instruction-set granularity benchmark on every supported ISA tier

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
mvn test -Dnative.version=5.4.12-2.0.4-x6 -Djavacpp.platform=linux-x86_64
```

## CI

GitHub Actions runs the full matrix on every push/PR and can be triggered
manually with a selectable native version.
