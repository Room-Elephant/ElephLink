package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse;
import java.util.Optional;

public interface CloudFlareService {
  boolean isValidToken();

  Optional<DnsRecordsResponse> getDnsRecords(String recordName);
}
