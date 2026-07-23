# hyperscan-java-test

> [English](README.md) | **简体中文**

针对 [hyperscan-java-native](https://github.com/XenoAmess/hyperscan-java-native)
（JavaCPP）与 [hyperscan-java-panama](https://github.com/XenoAmess/hyperscan-java-panama)
在各支持平台与指令集档位上的冒烟与对比测试。

📊 **[查看最新性能报告](https://xenoamess.github.io/hyperscan-java-test/)**

[![最新性能摘要](https://xenoamess.github.io/hyperscan-java-test/performance-summary.svg)](https://xenoamess.github.io/hyperscan-java-test/)

## 测试内容

- 原生库加载与平台选择
- 直接调用 Vectorscan JNI API（compile、scan、match），覆盖两种实现
- 移植的 vectorscan 单元测试套件（`vectorscan/unit/hyperscan`），JavaCPP 与 Panama 双跑 —— 见 [docs/vectorscan-port-status.md](docs/vectorscan-port-status.md)
- 合成数据：大量随机字面量模式、字符类、长输入
- 真实数据：HTTP 请求/响应解析、nginx 日志、简单安全签名
- JMH 基准（编译/扫描场景、数 GB 大流量扫描、指令集粒度），覆盖每个支持的 ISA 档位
- 三方基准对比：fork native（JavaCPP）、Panama、上游 `com.gliwka.hyperscan:native`（`upstream`）。上游没有 ISA 档位构建，也没有 `windows-x86_64` classifier —— Windows 行输出 `unsupported`

## 支持的平台

| 操作系统 | 架构     | 测试的 ISA 档位                                   |
|---------|----------|---------------------------------------------------|
| Linux   | x86_64   | `baseline`、`avx2`、`linux-x86_64`（AVX-512）     |
| Linux   | arm64    | `baseline`、`linux-arm64`（SVE2）                 |
| Windows | x86_64   | `baseline`、`windows-x86_64`（AVX2）              |

## 本地运行

```bash
mvn test
```

强制指定某个 ISA 档位：

```bash
mvn test -Djavacpp.platform=linux-x86_64-baseline
```

可用的 `javacpp.platform` 取值：
- `linux-x86_64-baseline`
- `linux-x86_64-avx2`
- `linux-x86_64`
- `linux-arm64-baseline`
- `linux-arm64`
- `windows-x86_64-baseline`
- `windows-x86_64`

测试不同的 native 产物版本：

```bash
mvn test -Dnative.version=5.4.12-2.0.4-x7 -Djavacpp.platform=linux-x86_64
```

## CI

GitHub Actions 在每次 push/PR 时运行冒烟测试矩阵，也可手动触发并选择 native 版本。在 `master` 上，JMH 基准会在每个平台上分别运行 JavaCPP 与 Panama（Linux 平台另加上游 gliwka native），详细的跨平台性能报告发布到
[GitHub Pages](https://xenoamess.github.io/hyperscan-java-test/)。
