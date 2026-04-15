#!/usr/bin/env bash

set -euo pipefail

BASE_REF="${1:-master}"
FEATURE_SPEC_DIR="specs/004-login-frontend-admin"

if [[ ! -d .git ]]; then
  echo "ERROR: Ejecuta este script desde la raiz del repositorio." >&2
  exit 1
fi

resolve_ref() {
  local ref="$1"
  if git rev-parse --verify "$ref" >/dev/null 2>&1; then
    echo "$ref"
    return 0
  fi

  if git rev-parse --verify "origin/$ref" >/dev/null 2>&1; then
    echo "origin/$ref"
    return 0
  fi

  echo ""
}

RESOLVED_BASE_REF="$(resolve_ref "$BASE_REF")"

if [[ -z "$RESOLVED_BASE_REF" ]]; then
  echo "ERROR: No se pudo resolver base ref '$BASE_REF' (ni local ni origin)." >&2
  exit 1
fi

MERGE_BASE="$(git merge-base HEAD "$RESOLVED_BASE_REF")"
CHANGED_FILES="$(git diff --name-only "$MERGE_BASE"...HEAD)"

backend_changed=false
contracts_changed=false

if echo "$CHANGED_FILES" | grep -E '^backend/src/main/java/.*\.java$' >/dev/null; then
  while IFS= read -r file; do
    [[ -z "$file" ]] && continue
    if git diff "$MERGE_BASE"...HEAD -- "$file" | grep -E '^[+-].*@((Request|Get|Post|Put|Patch|Delete)Mapping)|^[+-].*/api/v[0-9]+' >/dev/null; then
      echo "ERROR: Posible cambio de endpoint backend detectado en $file" >&2
      backend_changed=true
    fi
  done < <(echo "$CHANGED_FILES" | grep -E '^backend/src/main/java/.*\.java$' || true)
fi

if echo "$CHANGED_FILES" | grep -E '^specs/.*/contracts/.*\.ya?ml$' >/dev/null; then
  echo "ERROR: Cambio en contratos OpenAPI YAML detectado:" >&2
  echo "$CHANGED_FILES" | grep -E '^specs/.*/contracts/.*\.ya?ml$' >&2
  contracts_changed=true
fi

if [[ "$backend_changed" == true || "$contracts_changed" == true ]]; then
  echo "FAIL: Este feature no debe agregar/modificar endpoints backend ni contratos OpenAPI YAML." >&2
  exit 1
fi

echo "OK: No se detectaron cambios de endpoints backend ni contratos OpenAPI YAML para este feature."
