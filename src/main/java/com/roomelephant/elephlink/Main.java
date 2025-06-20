package com.roomelephant.elephlink;

import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.AUTH_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.IP_LIST_CONFIGURATION_FILE;

import com.roomelephant.elephlink.adapters.cloudflare.CloudFlareService;
import com.roomelephant.elephlink.adapters.ipservice.IpService;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.infra.ArgsParser;
import com.roomelephant.elephlink.infra.config.ConfigLoader;
import com.roomelephant.elephlink.infra.config.auth.AuthConfigurationLoader;
import com.roomelephant.elephlink.infra.config.ipservice.IpServiceConfigurationLoader;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {
    ArgsParser argsParser = new ArgsParser();
    Map<String, String> parameters = argsParser.parse(args);

    AuthConfig authConfig;
    IpServiceConfig ipConfig;
    IpService ipService;
    CloudFlareService cloudFlareService;

    try {
      ConfigLoader<AuthConfig> authConfigurationLoader = new AuthConfigurationLoader();
      authConfig = authConfigurationLoader.load(parameters.get(AUTH_CONFIGURATION_FILE.getKey()));
      cloudFlareService = new CloudFlareService(authConfig);
      log.debug("auth configs have been loaded");

      ConfigLoader<IpServiceConfig> ipsConfigurationLoader = new IpServiceConfigurationLoader();
      ipConfig = ipsConfigurationLoader.load(parameters.get(IP_LIST_CONFIGURATION_FILE.getKey()));
      ipService = new IpService(ipConfig);
      log.debug("IP list configs have been loaded: {}", ipConfig);
    } catch (Exception e) {
      log.error("Invalid configuration parameters. Reason: {}", e.getMessage());
      System.exit(1);
      return;
    }

    Optional<String> currentIp = ipService.fetchCurrentIp();
    if (currentIp.isEmpty()) {
      log.error("Failed to fetch current ip");
    }
  }
}