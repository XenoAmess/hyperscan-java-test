#!/usr/bin/env bash

set -euo pipefail
shopt -s nullglob

is_benign_surefire_classpath_dumpstream() {
  local file=$1
  local line
  local state=0
  local saw_warning=false

  while IFS= read -r line || [ -n "$line" ]; do
    line=${line%$'\r'}
    case $state in
      0)
        [[ $line == "# Created at "* ]] || return 1
        state=1
        ;;
      1)
        [[ $line == "Boot Manifest-JAR contains absolute paths in classpath "* ]] || return 1
        state=2
        ;;
      2)
        [[ $line == "Hint: <argLine>-Djdk.net.URLClassPath.disableClassPathURLCheck=true</argLine>" ]] || return 1
        state=3
        ;;
      3)
        [[ $line == "'other' has different root" ]] || return 1
        state=4
        saw_warning=true
        ;;
      4)
        [[ -z $line ]] || return 1
        state=0
        ;;
    esac
  done < "$file"

  [[ $saw_warning == true && ($state -eq 0 || $state -eq 4) ]]
}

files=(
  hs_err_pid*.log
  target/surefire-reports/*.dump
  target/surefire-reports/*.dumpstream
)

diagnostics=()
for file in "${files[@]}"; do
  if [[ $file == *.dumpstream ]] && is_benign_surefire_classpath_dumpstream "$file"; then
    echo "::notice::Ignoring benign Surefire cross-drive classpath diagnostic: $file"
  else
    diagnostics+=("$file")
  fi
done

if [ "${#diagnostics[@]}" -ne 0 ]; then
  echo "::error::Native/JVM crash diagnostics were generated:"
  printf '  %s\n' "${diagnostics[@]}"
  exit 1
fi
