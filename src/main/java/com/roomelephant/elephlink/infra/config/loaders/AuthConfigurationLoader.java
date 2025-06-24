package com.roomelephant.elephlink.infra.config.loaders;

import static com.roomelephant.elephlink.infra.config.ConfigurationFiles.AUTH_FILE;
import static com.roomelephant.elephlink.infra.config.loaders.AuthConfigurationLoader.AuthProperties.AUTH_EMAIL;
import static com.roomelephant.elephlink.infra.config.loaders.AuthConfigurationLoader.AuthProperties.AUTH_KEY;
import static com.roomelephant.elephlink.infra.config.loaders.AuthConfigurationLoader.AuthProperties.ZONE_IDENTIFIER;

import com.roomelephant.elephlink.domain.model.AuthConfig;
import java.util.Map;

public class AuthConfigurationLoader extends BaseConfigurationLoader<AuthConfig> {

  @Override
  protected AuthConfig convert(Map<String, Object> ymlConfig) {
    String email = getAndValidateString(ymlConfig, AUTH_EMAIL.key());
    String key = getAndValidateString(ymlConfig, AUTH_KEY.key());
    String zoneIdentifier = getAndValidateString(ymlConfig, ZONE_IDENTIFIER.key());

    return AuthConfig.builder()
        .email(email)
        .key(key)
        .zoneIdentifier(zoneIdentifier)
        .build();
  }

  @Override
  protected String getFileName() {
    return AUTH_FILE.key();
  }

  private String getAndValidateString(Map<String, Object> ymlConfig, String key) {
    String textConfig = (String) ymlConfig.get(key);
    if (textConfig == null || textConfig.isEmpty()) {
      throw new IllegalArgumentException("Invalid auth configuration. " + key + " is required.");
    }
    return textConfig;
  }

  enum AuthProperties {
    AUTH_EMAIL("email"),
    AUTH_KEY("key"),
    ZONE_IDENTIFIER("zoneIdentifier");

    private final String key;

    AuthProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}