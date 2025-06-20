package com.roomelephant.elephlink.infra.config.auth;

import static com.roomelephant.elephlink.domain.model.AuthConfig.AuthProperties.AUTH_EMAIL;
import static com.roomelephant.elephlink.domain.model.AuthConfig.AuthProperties.AUTH_KEY;
import static com.roomelephant.elephlink.domain.model.AuthConfig.AuthProperties.AUTH_METHOD;
import static com.roomelephant.elephlink.domain.model.AuthConfig.AuthProperties.ZONE_IDENTIFIER;

import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.infra.config.BaseConfigurationLoader;
import java.util.Map;

public class AuthConfigurationLoader extends BaseConfigurationLoader<AuthConfig> {
  protected static final String DEFAULT_FILE = "config.yml";

  @Override
  protected AuthConfig convert(Map<String, Object> ymlConfig) {
    return AuthConfig.builder()
        .authEmail((String) ymlConfig.get(AUTH_EMAIL.getKey()))
        .authMethod(AuthConfig.AuthMethod.fromString((String) ymlConfig.get(AUTH_METHOD.getKey())))
        .authKey((String) ymlConfig.get(AUTH_KEY.getKey()))
        .zoneIdentifier((String) ymlConfig.get(ZONE_IDENTIFIER.getKey()))
        .build();
  }

  @Override
  protected String getFileName() {
    return DEFAULT_FILE;
  }
}