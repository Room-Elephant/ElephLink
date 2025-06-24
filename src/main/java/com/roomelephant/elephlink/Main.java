package com.roomelephant.elephlink;

import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.CLOUDFLARE_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.IP_LIST_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.RECORDS_CONFIGURATION_FILE;

import com.roomelephant.elephlink.adapters.cloudflare.CloudFlareService;
import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.DnsService;
import com.roomelephant.elephlink.domain.Core;
import com.roomelephant.elephlink.domain.IpService;
import com.roomelephant.elephlink.domain.TaskManager;
import com.roomelephant.elephlink.adapters.cloudflare.CloudflareConfig;
import com.roomelephant.elephlink.domain.model.DnsRecordsConfig;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.infra.TaskManagerImpl;
import com.roomelephant.elephlink.infra.ConfigLoader;
import com.roomelephant.elephlink.infra.config.loaders.CloudflareConfigurationLoader;
import com.roomelephant.elephlink.infra.config.loaders.RecordsConfigurationLoader;
import com.roomelephant.elephlink.infra.config.loaders.IpServiceConfigurationLoader;
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

    Core core;
    try {
      ConfigLoader<CloudflareConfig> cloudflareLoader = new CloudflareConfigurationLoader();
      CloudflareConfig cloudflareConfig = cloudflareLoader.load(parameters.get(CLOUDFLARE_CONFIGURATION_FILE.key()));
      DnsService dnsService = new CloudFlareService(cloudflareConfig);

      ConfigLoader<DnsRecordsConfig> dnsRecordsLoader = new RecordsConfigurationLoader();
      DnsRecordsConfig dnsRecordsConfig = dnsRecordsLoader.load(parameters.get(RECORDS_CONFIGURATION_FILE.key()));
      TaskManager taskManager = new TaskManagerImpl(dnsRecordsConfig);

      ConfigLoader<IpServiceConfig> ipServicesLoader = new IpServiceConfigurationLoader();
      IpServiceConfig ipConfig = ipServicesLoader.load(parameters.get(IP_LIST_CONFIGURATION_FILE.key()));
      IpService ipServiceImpl = new IpServiceImpl(ipConfig);

      core = new Core(dnsRecordsConfig, dnsService, ipServiceImpl, taskManager);
    } catch (Exception e) {
      log.error("Validate your configuration. {}", e.getMessage());
      System.exit(1);
      return;
    }

    try {
      core.validateConfigurations();
    } catch (Exception e) {
      log.error("Invalid configuration. {}", e.getMessage());
      System.exit(1);
      return;
    }
    core.start();
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