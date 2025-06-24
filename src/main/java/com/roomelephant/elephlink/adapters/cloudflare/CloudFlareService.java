package com.roomelephant.elephlink.adapters.cloudflare;

import static com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse.DnsRecordType.A;
import static com.roomelephant.elephlink.adapters.cloudflare.TokenVerifyResponse.Status.ACTIVE;

import com.roomelephant.elephlink.adapters.cloudflare.config.CloudflareConfig;
import com.roomelephant.elephlink.domain.DnsService;
import com.roomelephant.elephlink.domain.model.DnsRecord;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudFlareService implements DnsService {
  private static final String TOKEN_VALIDATION_URL = "/user/tokens/verify";
  private static final String LIST_RECORDS = "/zones/%s/dns_records?type=%s&name=%s";
  private static final String UPDATE_RECORDS = "/zones/%s/dns_records/%s";
  private static final String BEARER = "Bearer ";
  private static final String AUTHORIZATION = "Authorization";
  private static final String X_AUTH_EMAIL = "X-Auth-Email";

  private final CloudflareConfig cloudflareConfig;
  private final CfRequest client;

  public CloudFlareService(CloudflareConfig cloudflareConfig, Duration timeout) {
    this.cloudflareConfig = cloudflareConfig;
    this.client = new CfRequest(timeout);
  }

  @Override
  public boolean isValidToken() {
    Map<String, String> headers = Map.of(AUTHORIZATION, BEARER + cloudflareConfig.key());
    TokenVerifyResponse response = client.get(TOKEN_VALIDATION_URL, headers, TokenVerifyResponse.class);

    return response.isSuccess() && response.getResult() != null && ACTIVE.equals(response.getResult().getStatus());
  }

  @Override
  public Optional<DnsRecord> getDnsRecord(String recordName) {
    Map<String, String> headers = getAuthHeaders();
    String requestUrl = String.format(LIST_RECORDS, cloudflareConfig.zoneIdentifier(), A, recordName);
    DnsRecordsResponse response = client.get(requestUrl, headers, DnsRecordsResponse.class);

    if (response.getResult() == null || response.getResult().isEmpty()) {
      return Optional.empty();
    }

    return response.getResult().stream().findFirst()
        .map(rawDnsRecord -> new DnsRecord(
            rawDnsRecord.getId(),
            DnsRecord.Type.valueOf(rawDnsRecord.getType().name()),
            rawDnsRecord.getName(),
            rawDnsRecord.getContent(),
            rawDnsRecord.getTtl()
        ));
  }

  @Override
  public boolean updateRecord(DnsRecord dnsRecord) {
    Map<String, String> headers = getAuthHeaders();
    String requestUrl = String.format(UPDATE_RECORDS, cloudflareConfig.zoneIdentifier(), dnsRecord.getId());

    UpdateRecordResponse response = client.patch(requestUrl, headers, dnsRecord, UpdateRecordResponse.class);

    return response.isSuccess();
  }

  private Map<String, String> getAuthHeaders() {
    return Map.of(AUTHORIZATION, BEARER + cloudflareConfig.key(),
        X_AUTH_EMAIL, cloudflareConfig.email());
  }
}
