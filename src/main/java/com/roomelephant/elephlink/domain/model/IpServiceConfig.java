package com.roomelephant.elephlink.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class IpServiceConfig {
  private final List<String> ips;

  @Getter
  public enum IpServicesProperties {
    IP_SERVICES("ip-services");

    private final String key;

    IpServicesProperties(String key) {
      this.key = key;
    }
  }
}
