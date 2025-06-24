#!/bin/bash

if [ -n "${JAVA_OPTS}" ]; then
  echo "Using JAVA_OPTS: $JAVA_OPTS"
fi

exec java -XX:+UseSerialGC ${JAVA_OPTS} -jar /app/elephlink/app.jar \
  --cloudflare-file=/config/cloudflare.yml \
  --dns-records-file=/config/dns-records.yml \
  --ip-service-file=/config/ip-services.yml