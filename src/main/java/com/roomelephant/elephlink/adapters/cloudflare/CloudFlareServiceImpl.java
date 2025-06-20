package com.roomelephant.elephlink.adapters.cloudflare;

import static com.roomelephant.elephlink.adapters.cloudflare.DnsRecordsResponse.DnsRecordType.A;
import static com.roomelephant.elephlink.adapters.cloudflare.TokenVerifyResponse.Status.ACTIVE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomelephant.elephlink.domain.CloudFlareService;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import com.roomelephant.elephlink.domain.model.RequestFailedException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudFlareServiceImpl implements CloudFlareService {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String TOKEN_VALIDATION_URL = "/user/tokens/verify";
  private static final String LIST_RECORDS = "/zones/%s/dns_records?type=%s&name=%s";
  public static final String BEARER = "Bearer ";
  public static final CFRequest client = new CFRequest();
  private final AuthConfig authConfig;

  public CloudFlareServiceImpl(AuthConfig authConfig) {
    this.authConfig = authConfig;
  }

  @Override
  public boolean isValidToken() {
    Map<String, String> headers = Map.of("Authorization", BEARER + authConfig.authKey());
    String response = client.get(TOKEN_VALIDATION_URL, headers);

    TokenVerifyResponse verify;
    try {
      verify = objectMapper.readValue(response, TokenVerifyResponse.class);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse token", e);
      return false;
    }

    if (verify.isSuccess() && verify.getResult() != null && ACTIVE.equals(verify.getResult().getStatus())) {
      log.info("Successfully validated token");
      return true;
    }

    log.error("Failed to validate token. Status: {}", verify.getResult() != null
        ? verify.getResult().getStatus() : "");
    return false;

  }

  @Override
  public Optional<DnsRecordsResponse> getDnsRecords(String recordName) {
    Map<String, String> headers = getAuthHeaders();
    String requestUrl = String.format(LIST_RECORDS, authConfig.zoneIdentifier(), A, recordName);
    String response = client.get(requestUrl, headers);

    DnsRecordsResponse dnsRecords;
    try {
      dnsRecords = objectMapper.readValue(response, DnsRecordsResponse.class);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse token", e);
      throw new RequestFailedException("Failed to parse response", e);
    }

    if (dnsRecords.getResult() == null || dnsRecords.getResult().isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(dnsRecords);
  }

  private Map<String, String> getAuthHeaders() {
    String authHeader = switch (authConfig.authMethod()) {
      case TOKEN -> "Authorization";
      case GLOBAL -> "X-Auth-Key";
    };
    String authValue = switch (authConfig.authMethod()) {
      case TOKEN -> BEARER + authConfig.authKey();
      case GLOBAL -> authConfig.authKey();
    };

    return Map.of(authHeader, authValue, "X-Auth-Email", authConfig.authEmail());
  }
}
