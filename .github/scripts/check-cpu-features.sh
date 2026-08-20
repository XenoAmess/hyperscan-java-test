#!/usr/bin/env bash
set -euo pipefail

platform=${1:?platform is required}

case "$platform" in
  windows-*)
    exec pwsh -NoProfile -File "$(dirname "$0")/check-windows-cpu-features.ps1" -Platform "$platform"
    ;;
esac

flags=$(grep -m1 -E '^(flags|Features)' /proc/cpuinfo | cut -d: -f2- || true)
missing=()

require_flag() {
  local flag=$1
  if ! grep -qw "$flag" <<< "$flags"; then
    missing+=("$flag")
  fi
}

case "$platform" in
  linux-x86_64-baseline)
    require_flag sse4_2
    require_flag popcnt
    ;;
  linux-x86_64-avx2)
    for flag in sse4_2 popcnt avx avx2 bmi1 bmi2 f16c fma movbe xsave; do
      require_flag "$flag"
    done
    if ! grep -Eqw '(abm|lzcnt)' <<< "$flags"; then
      missing+=("lzcnt")
    fi
    ;;
  linux-x86_64)
    for flag in sse4_2 popcnt avx avx2 bmi1 bmi2 f16c fma movbe xsave \
      avx512f avx512cd avx512dq avx512bw avx512vl avx512vbmi \
      avx512_bitalg avx512_vpopcntdq; do
      require_flag "$flag"
    done
    if ! grep -Eqw '(abm|lzcnt)' <<< "$flags"; then
      missing+=("lzcnt")
    fi
    ;;
  linux-arm64-baseline)
    require_flag asimd
    ;;
  linux-arm64)
    require_flag asimd
    require_flag sve
    require_flag sve2
    ;;
esac

if [ "${#missing[@]}" -gt 0 ]; then
  joined=$(IFS=,; printf '%s' "${missing[*]}")
  echo "SKIPPED=true" >> "$GITHUB_OUTPUT"
  echo "MISSING_FEATURE=$joined" >> "$GITHUB_OUTPUT"
  echo "::warning::CPU does not support $platform; missing: $joined"
else
  echo "SKIPPED=false" >> "$GITHUB_OUTPUT"
fi
