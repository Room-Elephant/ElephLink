package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.domain.model.DnsRecord;
import java.util.Optional;

public interface CloudFlareService {
  boolean isValidToken();

  Optional<DnsRecord> getDnsRecord(String recordName);

  boolean updateRecord(DnsRecord dnsRecord);
}
