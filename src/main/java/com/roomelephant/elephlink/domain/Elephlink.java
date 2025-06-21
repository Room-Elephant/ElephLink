package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.model.DnsRecord;
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
  private final LocalCache cache = LocalCache.getInstance();


  public Elephlink(RecordsConfig recordsConfig, CloudFlareService cloudFlareService, IpServiceImpl ipService,
                   TaskManager taskManager) {
    this.recordsConfig = recordsConfig;
    this.cloudFlareService = cloudFlareService;
    this.ipService = ipService;
    this.taskManager = taskManager;
  }

  public void validateConfigurations() {
    initializeCloudflare();
    initializeTaskManager();
    initializeIpServices();
  }

  public void start() {
    log.debug("Configure job schedule");
    taskManager.scheduleNext(this::task);
    Runtime.getRuntime().addShutdownHook(new Thread(taskManager::shutdown));
  }

  private void initializeCloudflare() {
    boolean validToken = cloudFlareService.isValidToken();
    if (!validToken) {
      throw new IllegalArgumentException("Invalid auth configurations");
    }

    for (String dnsRecord : recordsConfig.records()) {
      Optional<DnsRecord> cfDnsRecord = cloudFlareService.getDnsRecord(dnsRecord);
      if (cfDnsRecord.isEmpty()) {
        throw new IllegalArgumentException("Record '" + dnsRecord + "' does not exist. Please create one first.");
      }
      cache.getDnsRecords().add(cfDnsRecord.get());
    }
  }

  private void initializeTaskManager() {
    boolean hasValidExpression = taskManager.validateCronExpression();
    if (!hasValidExpression) {
      throw new IllegalArgumentException("Invalid cron expression ");
    }
  }

  private void initializeIpServices() {
    boolean hasInitialized = ipService.init();
    if (!hasInitialized) {
      throw new IllegalArgumentException("Invalid ip service configuration");
    }

    Optional<String> curr = ipService.fetchCurrentIp();
    if (curr.isEmpty()) {
      throw new IllegalArgumentException("Failed to fetch current ip");
    }
  }


  private void task() {
    Optional<String> curr = ipService.fetchCurrentIp();
    if (curr.isEmpty()) {
      log.warn("Unable to fetch external IP");
      return;
    }
    log.info("Current external IP address is {}", curr.get());

    String currentIp = curr.get();
    if (currentIp.equals(cache.getCurrentIp())) {
      log.debug("No IP change detected");
      return;
    }
    cache.setCurrentIp(currentIp);

    for (DnsRecord dnsRecord : cache.getDnsRecords()) {
      String previousIp = dnsRecord.getContent();
      if (previousIp.equals(currentIp)) {
        log.warn("Cloudflare record '{}' already configured with new IP {}", dnsRecord.getName(), currentIp);
        continue;
      }

      dnsRecord.setContent(currentIp);
      try {
        boolean result = cloudFlareService.updateRecord(dnsRecord);
        if (result) {
          log.info("Successfully updated cloudflare record '{}' to use ip {} instead of {}", dnsRecord.getName(),
              currentIp, previousIp);
        } else {
          dnsRecord.setContent(previousIp);
          log.warn("Failed to update cloudflare record '{}' to use ip {}. Using {} instead", dnsRecord.getName(),
              currentIp, previousIp);
        }
      } catch (Exception e) {
        dnsRecord.setContent(previousIp);
        log.warn("Something went wrong updating cloudflare record '{}' to use ip {}. Using {} instead",
            dnsRecord.getName(),
            currentIp, previousIp);
      }
    }
  }
}
