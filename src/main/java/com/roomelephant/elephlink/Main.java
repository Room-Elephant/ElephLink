package com.roomelephant.elephlink;

import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.AUTH_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.IP_LIST_CONFIGURATION_FILE;
import static com.roomelephant.elephlink.infra.config.ConfigurationProperties.RECORDS_CONFIGURATION_FILE;

import com.roomelephant.elephlink.adapters.cloudflare.CloudFlareServiceImpl;
import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.CloudFlareService;
import com.roomelephant.elephlink.domain.Elephlink;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import com.roomelephant.elephlink.domain.model.RecordsConfig;
import com.roomelephant.elephlink.infra.ArgsParser;
import com.roomelephant.elephlink.infra.TaskManager;
import com.roomelephant.elephlink.infra.config.ConfigLoader;
import com.roomelephant.elephlink.infra.config.auth.AuthConfigurationLoader;
import com.roomelephant.elephlink.infra.config.ipservice.IpServiceConfigurationLoader;
import com.roomelephant.elephlink.infra.config.records.RecordsConfigurationLoader;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {
    ArgsParser argsParser = new ArgsParser();
    Map<String, String> parameters = argsParser.parse(args);

    Elephlink elephlink;
    try {
      ConfigLoader<AuthConfig> authConfigurationLoader = new AuthConfigurationLoader();
      AuthConfig authConfig = authConfigurationLoader.load(parameters.get(AUTH_CONFIGURATION_FILE.key()));
      CloudFlareService cloudFlareServiceImpl = new CloudFlareServiceImpl(authConfig);

      ConfigLoader<RecordsConfig> recordsConfigurationLoader = new RecordsConfigurationLoader();
      RecordsConfig recordsConfig = recordsConfigurationLoader.load(parameters.get(RECORDS_CONFIGURATION_FILE.key()));
      TaskManager taskManager = new TaskManager(recordsConfig);

      ConfigLoader<IpServiceConfig> ipsConfigurationLoader = new IpServiceConfigurationLoader();
      IpServiceConfig ipConfig = ipsConfigurationLoader.load(parameters.get(IP_LIST_CONFIGURATION_FILE.key()));
      IpServiceImpl ipServiceImpl = new IpServiceImpl(ipConfig);

      elephlink = new Elephlink(recordsConfig, cloudFlareServiceImpl, ipServiceImpl, taskManager);
      elephlink.validateConfigurations();
    } catch (Exception e) {
      log.error("Invalid configuration parameters. Reason: {}", e.getMessage());
      System.exit(1);
      return;
    }

    elephlink.start();
  }
}