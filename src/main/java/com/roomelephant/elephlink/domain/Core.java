package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.domain.model.DnsRecord;
import com.roomelephant.elephlink.domain.model.DnsRecordsConfig;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Core {
  private final DnsRecordsConfig dnsRecordsConfig;
  private final DnsService DNSService;
  private final IpService ipService;
  private final TaskManager taskManager;
  private final LocalCache cache = LocalCache.getInstance();


  public Core(DnsRecordsConfig dnsRecordsConfig, DnsService DNSService, IpService ipService,
              TaskManager taskManager) {
    this.dnsRecordsConfig = dnsRecordsConfig;
    this.DNSService = DNSService;
    this.ipService = ipService;
    this.taskManager = taskManager;
  }

  public void validateConfigurations() {
    initializeCloudflare();
    initializeTaskManager();
    initializeIpServices();
  }

  public void start() {
    log.debug("Configure job schedule.");
    taskManager.execute(this::task, 0);
    Runtime.getRuntime().addShutdownHook(new Thread(taskManager::shutdown));
  }

  private void initializeCloudflare() {
    boolean validToken = DNSService.isValidToken();
    if (!validToken) {
      throw new IllegalArgumentException("Cloudflare token is not valid.");
    }

    for (String dnsRecord : dnsRecordsConfig.records()) {
      Optional<DnsRecord> cfDnsRecord = DNSService.getDnsRecord(dnsRecord);
      if (cfDnsRecord.isEmpty()) {
        throw new IllegalArgumentException("Record '" + dnsRecord
            + "' does not exist. Please create one first. "
            + "Additionally, confirm if the zone identifier is correctly configured.");
      }
      cache.getDnsRecords().add(cfDnsRecord.get());
    }
  }

  private void initializeTaskManager() {
    boolean hasValidExpression = taskManager.validateCronExpression();
    if (!hasValidExpression) {
      throw new IllegalArgumentException("Cron expression is not valid. Please insert a UNIX cron expression.");
    }
  }

  private void initializeIpServices() {
    boolean hasInitialized = ipService.init();
    if (!hasInitialized) {
      throw new IllegalArgumentException("Invalid ip service configuration. Add only valid url.");
    }

    Optional<String> curr = ipService.fetchCurrentIp();
    if (curr.isEmpty()) {
      throw new IllegalArgumentException(
          "No valid service to validate external IP address. Please insert at least one.");
    }
  }


  private void task() {
    Optional<String> curr = ipService.fetchCurrentIp();
    if (curr.isEmpty()) {
      log.warn("Unable to fetch external IP.");
      return;
    }
    log.info("Current external IP address is {}.", curr.get());

    String currentIp = curr.get();
    if (currentIp.equals(cache.getCurrentIp())) {
      log.debug("No IP change detected.");
      return;
    }
    cache.setCurrentIp(currentIp);

    for (DnsRecord dnsRecord : cache.getDnsRecords()) {
      String previousIp = dnsRecord.getContent();
      if (previousIp.equals(currentIp)) {
        log.warn("Cloudflare record '{}' already configured with new IP {}.", dnsRecord.getName(), currentIp);
        continue;
      }

      dnsRecord.setContent(currentIp);
      try {
        boolean result = DNSService.updateRecord(dnsRecord);
        if (result) {
          log.info("Successfully updated cloudflare record '{}' to use ip {} instead of {}.", dnsRecord.getName(),
              currentIp, previousIp);
        } else {
          dnsRecord.setContent(previousIp);
          log.warn("Failed to update cloudflare record '{}' to use ip {}. Using {} instead.", dnsRecord.getName(),
              currentIp, previousIp);
        }
      } catch (Exception e) {
        dnsRecord.setContent(previousIp);
        log.warn("Something went wrong updating cloudflare record '{}' to use ip {}. Using {} instead.",
            dnsRecord.getName(),
            currentIp, previousIp);
      }
    }
  }
}
