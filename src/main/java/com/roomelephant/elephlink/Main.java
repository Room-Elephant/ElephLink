package com.roomelephant.elephlink;

import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.AUTH_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.IP_LIST_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.RECORDS_CONFIGURATION_FILE;

import com.roomelephant.elephlink.adapters.cloudflare.CloudFlareService;
import com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse;
import com.roomelephant.elephlink.adapters.ipservice.IpService;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.domain.model.RecordsConfig;
import com.roomelephant.elephlink.infra.ArgsParser;
import com.roomelephant.elephlink.infra.config.ConfigLoader;
import com.roomelephant.elephlink.infra.config.auth.AuthConfigurationLoader;
import com.roomelephant.elephlink.infra.config.ipservice.IpServiceConfigurationLoader;
import com.roomelephant.elephlink.infra.config.records.RecordsConfigurationLoader;
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
    RecordsConfig recordsConfig;
    IpService ipService;
    CloudFlareService cloudFlareService;

    try {
      ConfigLoader<AuthConfig> authConfigurationLoader = new AuthConfigurationLoader();
      authConfig = authConfigurationLoader.load(parameters.get(AUTH_CONFIGURATION_FILE.key()));
      cloudFlareService = new CloudFlareService(authConfig);

      ConfigLoader<RecordsConfig> recordsConfigurationLoader = new RecordsConfigurationLoader();
      recordsConfig = recordsConfigurationLoader.load(parameters.get(RECORDS_CONFIGURATION_FILE.key()));
      for (String dnsRecord : recordsConfig.records()){
        cloudFlareService.getDnsRecords(dnsRecord);
      }

      log.debug("auth configs have been loaded");

      ConfigLoader<IpServiceConfig> ipsConfigurationLoader = new IpServiceConfigurationLoader();
      ipConfig = ipsConfigurationLoader.load(parameters.get(IP_LIST_CONFIGURATION_FILE.key()));
      ipService = new IpService(ipConfig);
      log.debug("IP list configs have been loaded: {}", ipConfig);
    } catch (Exception e) {
      log.error("Invalid configuration parameters. Reason: {}", e.getMessage());
      System.exit(1);
      return;
    }

    DnsRecordsResponse dnsRecordsResponse = cloudFlareService.getDnsRecords("roomelephant.com");
    log.info("dns records response: {}", dnsRecordsResponse);

    Optional<String> currentIp = ipService.fetchCurrentIp();
    if (currentIp.isEmpty()) {
      log.error("Failed to fetch current ip");
    }
  }
}