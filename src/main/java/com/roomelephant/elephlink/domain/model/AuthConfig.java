package com.roomelephant.elephlink.domain.model;

import lombok.Builder;

@Builder
public record AuthConfig(
    String email,
    Method method,
    String key,
    String zoneIdentifier
) {
  public enum Method {
    GLOBAL("global"), TOKEN("token");

    private final String value;

    Method(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }

    public static Method fromString(String text) {
      return valueOf(text.toUpperCase());
    }
  }
}
