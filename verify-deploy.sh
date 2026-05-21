#!/bin/bash
TARGET_IP=$1
PORT=$2

API_PATH="/health/ready"
URL="http://${TARGET_IP}:${PORT}${API_PATH}"

RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout 5 "${URL}")

if [ "$RESPONSE_CODE" -eq 200 ] || [ "$RESPONSE_CODE" -eq 302 ]; then
    echo "Status: OK ($RESPONSE_CODE)"
else
    echo "Status: ERROR ($RESPONSE_CODE)"
    exit 1
fi

ROOT_URL="http://${TARGET_IP}:${PORT}"
SERVER_HEADER=$(curl -sI "${ROOT_URL}" | grep -i "Server:" | tr -d '\r')

if [[ "$SERVER_HEADER" == *"nginx"* ]]; then
    echo "Web server: OK ($SERVER_HEADER)"
else
    echo "Web server: ERROR ($SERVER_HEADER)"
    exit 1
fi

exit 0