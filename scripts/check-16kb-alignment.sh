#!/usr/bin/env bash
#
# Verify an APK is 16 KB page-size compliant (Google Play requirement for Android 15+ / API 35+).
#
# Checks two things for every bundled native library (.so):
#   1. ELF LOAD segment alignment >= 16 KB (2**14). AGP cannot fix this — it's baked into the
#      prebuilt .so, so a failure here means a dependency shipped a non-aligned binary.
#   2. The .so is page-aligned & uncompressed inside the APK zip (zipalign -c -P 16).
#
# Usage:
#   scripts/check-16kb-alignment.sh <path-to-apk>
#
# Example:
#   ./gradlew assembleRelease
#   scripts/check-16kb-alignment.sh app/build/outputs/apk/release/app-release.apk
#
set -euo pipefail

APK="${1:-}"
if [[ -z "$APK" || ! -f "$APK" ]]; then
  echo "usage: $0 <path-to-apk>" >&2
  exit 2
fi

# Locate zipalign from the Android SDK build-tools (newest version wins).
SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}}"
ZIPALIGN="$(ls "$SDK"/build-tools/*/zipalign 2>/dev/null | sort -V | tail -1 || true)"
if [[ -z "$ZIPALIGN" ]]; then
  echo "error: could not find zipalign under $SDK/build-tools" >&2
  exit 2
fi

MIN_ALIGN=16384 # 16 KB = 2**14
fail=0

# 16 KB pages only exist on 64-bit Android. Only these ABIs count toward pass/fail; 32-bit ABIs
# (armeabi-v7a, armeabi, x86) and dead ABIs (mips/mips64) never run on 16 KB pages, so their
# alignment is reported for information only.
is_64bit_abi() {
  case "$1" in
    arm64-v8a | x86_64 | riscv64) return 0 ;;
    *) return 1 ;;
  esac
}

echo "=== ELF LOAD segment alignment (64-bit ABIs need >= 2**14 / 16384) ==="
tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT
unzip -o -q "$APK" "lib/*/*.so" -d "$tmp" 2>/dev/null || true

if compgen -G "$tmp/lib/*/*.so" > /dev/null; then
  while IFS= read -r f; do
    rel="${f#"$tmp"/}"          # e.g. lib/arm64-v8a/libmiqat.so
    abi="$(basename "$(dirname "$f")")"
    align_pow="$(objdump -p "$f" 2>/dev/null | awk '/LOAD/{print $NF; exit}')" # e.g. 2**14
    exp="${align_pow##*\*\*}"
    align=$(( 1 << exp ))
    if is_64bit_abi "$abi"; then
      if (( align < MIN_ALIGN )); then
        status="FAIL"
        fail=1
      else
        status="OK"
      fi
    else
      status="skip"          # 32-bit / dead ABI — irrelevant to 16 KB compliance
    fi
    printf '  %-6s %-8s %s\n' "$status" "$align_pow" "$rel"
  done < <(find "$tmp/lib" -name '*.so' | sort)
else
  echo "  (no .so files found in APK — nothing to check)"
fi

echo ""
echo "=== APK zip alignment (zipalign -c -P 16) ==="
if ! "$ZIPALIGN" -c -P 16 -v 4 "$APK" | grep -iE "\.so|Verification"; then
  echo "  FAIL: zip alignment verification failed"
  fail=1
fi

echo ""
if (( fail == 0 )); then
  echo "16 KB alignment: PASS"
else
  echo "16 KB alignment: FAIL" >&2
fi
exit "$fail"
