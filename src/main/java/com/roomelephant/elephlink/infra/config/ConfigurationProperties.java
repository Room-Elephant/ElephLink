package com.roomelephant.elephlink.infra.config;

public enum ConfigurationProperties {
  AUTH_CONFIGURATION_FILE("authConfigurationFile"),
  RECORDS_CONFIGURATION_FILE("recordsConfigurationFile"),
  IP_LIST_CONFIGURATION_FILE("ipListConfigurationFile");

  private final String key;

  ConfigurationProperties(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
