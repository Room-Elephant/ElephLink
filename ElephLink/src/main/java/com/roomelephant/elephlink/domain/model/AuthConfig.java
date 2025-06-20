package com.roomelephant.elephlink.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class AuthConfig {
  private final String authEmail;
  private final AuthMethod authMethod;
  private final String authKey;
  private final String zoneIdentifier;

  public enum AuthMethod {
    GLOBAL("global"), TOKEN("token");

    private final String value;

    AuthMethod(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }

    public static AuthMethod fromString(String text) {
      return valueOf(text.toUpperCase());
    }
  }

  @Getter
  public enum AuthProperties {
    AUTH_EMAIL("authEmail"),
    AUTH_METHOD("authMethod"),
    AUTH_KEY("authKey"),
    ZONE_IDENTIFIER("zoneIdentifier");


    private final String key;

    AuthProperties(String key) {
      this.key = key;
    }
  }
}
