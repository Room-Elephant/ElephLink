package com.roomelephant.elephlink.infra.config.ipservice;

import static com.roomelephant.elephlink.domain.model.IpServiceConfig.IpServicesProperties.IP_SERVICES;

import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.infra.config.BaseConfigurationLoader;
import java.util.List;
import java.util.Map;

public class IpServiceConfigurationLoader extends BaseConfigurationLoader<IpServiceConfig> {
  protected static final String DEFAULT_FILE = "iplist.yml";

  @Override
  protected IpServiceConfig convert(Map<String, Object> ymlConfig) {
    Object o = ymlConfig.get(IP_SERVICES.key());

    List<String> list = switch (o) {
      case List<?> casted -> castToListString(casted);
      case String casted -> List.of(casted);
      case null, default -> throw new IllegalArgumentException("Invalid configuration for ip list");

    };

    return IpServiceConfig.builder()
        .services(list)
        .build();

  }

  @Override
  protected String getFileName() {
    return DEFAULT_FILE;
  }
}
