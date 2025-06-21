#!/bin/bash

if [ -n "${JAVA_OPTS}" ]; then
  echo "Using JAVA_OPTS: $JAVA_OPTS"
fi

exec java ${JAVA_OPTS} -jar /app/elephlink/app.jar \
  --authConfigurationFile=/config/config.yml \
  --recordsConfigurationFile=/config/records.yml \
  --ipListConfigurationFile=/config/iplist.yml