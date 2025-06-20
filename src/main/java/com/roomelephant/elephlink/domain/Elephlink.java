package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse;
import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.model.RecordsConfig;
import com.roomelephant.elephlink.infra.TaskManager;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Elephlink {
  private final RecordsConfig recordsConfig;
  private final CloudFlareService cloudFlareService;
  private final IpService ipService;
  private final TaskManager taskManager;


  public Elephlink(RecordsConfig recordsConfig, CloudFlareService cloudFlareService, IpServiceImpl ipService,
                   TaskManager taskManager) {
    this.recordsConfig = recordsConfig;
    this.cloudFlareService = cloudFlareService;
    this.ipService = ipService;
    this.taskManager = taskManager;
  }

  public void validateConfigurations() {
    boolean validToken = cloudFlareService.isValidToken();
    if (!validToken) {
      throw new IllegalArgumentException("Invalid auth configurations");
    }

    for (String dnsRecord : recordsConfig.records()) {
      Optional<DnsRecordsResponse> dnsRecords = cloudFlareService.getDnsRecords(dnsRecord);
      if (dnsRecords.isEmpty()) {
        throw new IllegalArgumentException("Record '" + dnsRecord + "' does not exist. Please create one first.");
      }
    }

    boolean hasValidExpression = taskManager.validateCronExpression();
    if (!hasValidExpression) {
      throw new IllegalArgumentException("Invalid cron expression ");
    }

    boolean hasInitialized = ipService.init();
    if (!hasInitialized) {
      throw new IllegalArgumentException("Invalid ip service configuration");
    }
  }

  public void start() {
    log.debug("Configure job schedule");
    taskManager.scheduleNext(this::task);
    Runtime.getRuntime().addShutdownHook(new Thread(taskManager::shutdown));
  }
  }
}
