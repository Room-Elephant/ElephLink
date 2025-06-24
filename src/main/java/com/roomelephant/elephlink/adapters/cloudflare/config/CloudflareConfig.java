package com.roomelephant.elephlink.adapters.cloudflare.config;

import lombok.Builder;

@Builder
public record CloudflareConfig(
    String email,
    String key,
    String zoneIdentifier
) {
  @Override
  public String toString() {
    return " CloudflareConfig["
        + "email=" + email + ", "
        + "key=*****, "
        + "zoneIdentifier=*****"
        + "]";
  }
}
