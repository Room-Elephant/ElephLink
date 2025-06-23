#!/bin/bash

if [ -n "${JAVA_OPTS}" ]; then
  echo "Using JAVA_OPTS: $JAVA_OPTS"
fi

exec java ${JAVA_OPTS} -jar /app/elephlink/app.jar \
  --auth-file=/config/auth.yml \
  --dns-records-file=/config/dns-records.yml \
  --ip-service-file=/config/ip-services.yml