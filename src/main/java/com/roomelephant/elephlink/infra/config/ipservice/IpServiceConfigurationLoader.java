package com.roomelephant.elephlink.infra.config.ipservice;

import static com.roomelephant.elephlink.infra.config.ConfigurationFiles.IP_SERVICES_FILE;
import static com.roomelephant.elephlink.infra.config.ipservice.IpServiceConfigurationLoader.IpServicesProperties.IP_SERVICES;

import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.infra.config.BaseConfigurationLoader;
import java.util.List;
import java.util.Map;

public class IpServiceConfigurationLoader extends BaseConfigurationLoader<IpServiceConfig> {

  @Override
  protected IpServiceConfig convert(Map<String, Object> ymlConfig) {
    Object o = ymlConfig.get(IP_SERVICES.key());

    List<String> list = switch (o) {
      case List<?> casted -> castToListString(casted);
      case String casted -> List.of(casted);
      case null, default ->
          throw new IllegalArgumentException("Invalid ip services configuration. Invalid format for services.");

    };

    return IpServiceConfig.builder()
        .services(list)
        .build();

  }

  @Override
  protected String getFileName() {
    return IP_SERVICES_FILE.key();
  }

  public enum IpServicesProperties {
    IP_SERVICES("services");

    private final String key;

    IpServicesProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}
