package com.roomelephant.elephlink;

import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.AUTH_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.IP_LIST_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.RECORDS_CONFIGURATION_FILE;

import com.roomelephant.elephlink.adapters.cloudflare.CloudFlareServiceImpl;
import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.CloudFlareService;
import com.roomelephant.elephlink.domain.Elephlink;
import com.roomelephant.elephlink.domain.IpService;
import com.roomelephant.elephlink.domain.TaskManager;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.domain.model.DnsRecordsConfig;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.infra.TaskManagerImpl;
import com.roomelephant.elephlink.infra.config.ConfigLoader;
import com.roomelephant.elephlink.infra.config.auth.AuthConfigurationLoader;
import com.roomelephant.elephlink.infra.config.dnsrecords.RecordsConfigurationLoader;
import com.roomelephant.elephlink.infra.config.ipservice.IpServiceConfigurationLoader;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {
    log.info("Starting Elephlink ...");
    Map<String, String> parameters = parser.apply(args);

    Elephlink elephlink;
    try {
      ConfigLoader<AuthConfig> authLoader = new AuthConfigurationLoader();
      AuthConfig authConfig = authLoader.load(parameters.get(AUTH_CONFIGURATION_FILE.key()));
      CloudFlareService cloudFlareServiceImpl = new CloudFlareServiceImpl(authConfig);

      ConfigLoader<DnsRecordsConfig> dnsRecordsLoader = new RecordsConfigurationLoader();
      DnsRecordsConfig dnsRecordsConfig = dnsRecordsLoader.load(parameters.get(RECORDS_CONFIGURATION_FILE.key()));
      TaskManager taskManager = new TaskManagerImpl(dnsRecordsConfig);

      ConfigLoader<IpServiceConfig> ipServicesLoader = new IpServiceConfigurationLoader();
      IpServiceConfig ipConfig = ipServicesLoader.load(parameters.get(IP_LIST_CONFIGURATION_FILE.key()));
      IpService ipServiceImpl = new IpServiceImpl(ipConfig);

      elephlink = new Elephlink(dnsRecordsConfig, cloudFlareServiceImpl, ipServiceImpl, taskManager);
    } catch (Exception e) {
      log.error("Validate your configuration. {}", e.getMessage());
      System.exit(1);
      return;
    }

    try {
      elephlink.validateConfigurations();
    } catch (Exception e) {
      log.error("Invalid configuration. {}", e.getMessage());
      System.exit(1);
      return;
    }
    elephlink.start();
  }

  private static final Function<String[], Map<String, String>> parser = parameters -> Arrays.stream(parameters)
      .filter(arg -> arg.startsWith("--") && arg.contains("="))
      .map(arg -> arg.substring(2).split("=", 2))
      .filter(parts -> parts.length == 2)
      .collect(Collectors.toMap(
          parts -> parts[0],
          parts -> parts[1],
          (existing, replacement) -> replacement
      ));
}