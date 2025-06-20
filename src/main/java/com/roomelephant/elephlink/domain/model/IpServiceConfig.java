package com.roomelephant.elephlink.domain.model;

import java.util.List;
import lombok.Builder;

@Builder
public record IpServiceConfig(
    List<String> services
) {
  public enum IpServicesProperties {
    IP_SERVICES("ip-services");

    private final String key;

    IpServicesProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}
