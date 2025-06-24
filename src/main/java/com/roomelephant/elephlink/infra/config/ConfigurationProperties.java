package com.roomelephant.elephlink.infra.config;

public enum ConfigurationProperties {
  CLOUDFLARE_CONFIGURATION_FILE("cloudflare-file"),
  RECORDS_CONFIGURATION_FILE("dns-records-file"),
  IP_LIST_CONFIGURATION_FILE("ip-service-file");

  private final String key;

  ConfigurationProperties(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
