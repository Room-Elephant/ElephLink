package com.roomelephant.elephlink.infra.config;

public enum ConfigurationProperties {
  AUTH_CONFIGURATION_FILE("auth-file"),
  RECORDS_CONFIGURATION_FILE("dns-file"),
  IP_LIST_CONFIGURATION_FILE("ip-service-file");

  private final String key;

  ConfigurationProperties(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
