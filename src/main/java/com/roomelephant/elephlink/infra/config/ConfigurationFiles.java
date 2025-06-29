package com.roomelephant.elephlink.infra.config;

public enum ConfigurationFiles {
  CLOUDFLARE_FILE("cloudflare.yml"),
  DNS_RECORDS_FILE("dns-records.yml"),
  IP_SERVICES_FILE("ip-services.yml");

  private final String key;

  ConfigurationFiles(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }
}
