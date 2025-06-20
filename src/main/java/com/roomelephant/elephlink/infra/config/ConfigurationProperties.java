package com.roomelephant.elephlink.infra.config;

import lombok.Getter;

@Getter
public enum ConfigurationProperties {
  AUTH_CONFIGURATION_FILE("authConfigurationFile"),
  IP_LIST_CONFIGURATION_FILE("ipListConfigurationFile");

  private final String key;

  ConfigurationProperties(String key) {
    this.key = key;
  }
}
