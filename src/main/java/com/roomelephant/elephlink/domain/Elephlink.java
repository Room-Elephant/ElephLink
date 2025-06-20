package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse;
import com.roomelephant.elephlink.adapters.ipservice.IpServiceImpl;
import com.roomelephant.elephlink.domain.model.RecordsConfig;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Elephlink {
  private final RecordsConfig recordsConfig;
  private final CloudFlareService cloudFlareService;
  private final IpService ipService;


  public Elephlink(RecordsConfig recordsConfig, CloudFlareService cloudFlareService, IpServiceImpl ipService) {
    this.recordsConfig = recordsConfig;
    this.cloudFlareService = cloudFlareService;
    this.ipService = ipService;
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

    boolean hasInitialized = ipService.init();
    if (!hasInitialized) {
      throw new IllegalArgumentException("Invalid ip service configuration");
    }

  }

  public void start() {
  }
}
