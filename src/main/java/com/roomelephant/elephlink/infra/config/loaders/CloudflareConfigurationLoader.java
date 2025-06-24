package com.roomelephant.elephlink.infra.config.loaders;

import static com.roomelephant.elephlink.infra.config.ConfigurationFiles.CLOUDFLARE_FILE;
import static com.roomelephant.elephlink.infra.config.loaders.CloudflareConfigurationLoader.CloudflareProperties.EMAIL;
import static com.roomelephant.elephlink.infra.config.loaders.CloudflareConfigurationLoader.CloudflareProperties.KEY;
import static com.roomelephant.elephlink.infra.config.loaders.CloudflareConfigurationLoader.CloudflareProperties.ZONE_IDENTIFIER;

import com.roomelephant.elephlink.adapters.cloudflare.CloudflareConfig;
import java.util.Map;

public class CloudflareConfigurationLoader extends BaseConfigurationLoader<CloudflareConfig> {

  @Override
  protected CloudflareConfig convert(Map<String, Object> ymlConfig) {
    String email = getAndValidateString(ymlConfig, EMAIL.key());
    String key = getAndValidateString(ymlConfig, KEY.key());
    String zoneIdentifier = getAndValidateString(ymlConfig, ZONE_IDENTIFIER.key());

    return CloudflareConfig.builder()
        .email(email)
        .key(key)
        .zoneIdentifier(zoneIdentifier)
        .build();
  }

  @Override
  protected String getFileName() {
    return CLOUDFLARE_FILE.key();
  }

  private String getAndValidateString(Map<String, Object> ymlConfig, String key) {
    String textConfig = (String) ymlConfig.get(key);
    if (textConfig == null || textConfig.isEmpty()) {
      throw new IllegalArgumentException("Invalid Cloudflare configuration. " + key + " is required.");
    }
    return textConfig;
  }

  enum CloudflareProperties {
    EMAIL("email"),
    KEY("key"),
    ZONE_IDENTIFIER("zoneIdentifier");

    private final String key;

    CloudflareProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}