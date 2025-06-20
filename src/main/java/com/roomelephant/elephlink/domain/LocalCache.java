package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.domain.model.DnsRecord;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;


public class LocalCache {
  @Getter
  private static final LocalCache instance = new LocalCache();

  @Setter
  @Getter
  private String currentIp = null;
  @Getter
  private List<DnsRecord> dnsRecords = new CopyOnWriteArrayList<>();

  private LocalCache() {
  }
}
