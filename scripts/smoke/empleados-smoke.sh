#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
USER_NAME="${APP_BASIC_USER:-admin}"
USER_PASS="${APP_BASIC_PASSWORD:-admin123}"

create_response=$(curl -sS -u "$USER_NAME:$USER_PASS" \
  -H "Content-Type: application/json" \
  -X POST "$BASE_URL/api/v1/empleados" \
  -d '{"nombre":"Smoke User","direccion":"Smoke 123","telefono":"5551111111","correo":"smoke.user@empresa.com","contrasena":"SmokePass123"}')

clave=$(echo "$create_response" | sed -n 's/.*"clave":"\([^"]*\)".*/\1/p')
if [[ -z "$clave" ]]; then
  echo "No se obtuvo clave en alta"
  exit 1
fi

curl -sS -u "$USER_NAME:$USER_PASS" "$BASE_URL/api/v1/empleados/$clave" >/dev/null
curl -sS -u "$USER_NAME:$USER_PASS" "$BASE_URL/api/v1/empleados?page=0&size=5" >/dev/null
curl -sS -u "smoke.user@empresa.com:SmokePass123" "$BASE_URL/api/v1/empleados/auth/me" >/dev/null

patch_status=$(curl -sS -o /dev/null -w "%{http_code}" \
  -u "smoke.user@empresa.com:SmokePass123" \
  -H "Content-Type: application/json" \
  -X PATCH "$BASE_URL/api/v1/empleados/$clave/estado" \
  -d '{"activo":false}')

if [[ "$patch_status" != "403" ]]; then
  echo "Se esperaba 403 en PATCH estado para actor no admin, pero fue $patch_status"
  exit 1
fi

invalid_auth_status=$(curl -sS -o /dev/null -w "%{http_code}" \
  -u "smoke.user@empresa.com:PasswordIncorrecta" \
  "$BASE_URL/api/v1/empleados/auth/me")

if [[ "$invalid_auth_status" != "401" ]]; then
  echo "Se esperaba 401 con password incorrecta, pero fue $invalid_auth_status"
  exit 1
fi

curl -sS -u "$USER_NAME:$USER_PASS" \
  -H "Content-Type: application/json" \
  -X PATCH "$BASE_URL/api/v1/empleados/$clave/estado" \
  -d '{"activo":false}' >/dev/null

inactive_auth_status=$(curl -sS -o /dev/null -w "%{http_code}" \
  -u "smoke.user@empresa.com:SmokePass123" \
  "$BASE_URL/api/v1/empleados/auth/me")

if [[ "$inactive_auth_status" != "401" ]]; then
  echo "Se esperaba 401 para cuenta inactiva, pero fue $inactive_auth_status"
  exit 1
fi

curl -sS -u "$USER_NAME:$USER_PASS" \
  -H "Content-Type: application/json" \
  -X PATCH "$BASE_URL/api/v1/empleados/$clave/estado" \
  -d '{"activo":true}' >/dev/null

curl -sS -u "$USER_NAME:$USER_PASS" -X DELETE "$BASE_URL/api/v1/empleados/$clave" >/dev/null

echo "Smoke OK for clave $clave"
