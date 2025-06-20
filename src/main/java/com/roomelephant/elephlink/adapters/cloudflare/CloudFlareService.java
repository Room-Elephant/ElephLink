package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomelephant.elephlink.domain.model.AuthConfig;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudFlareService {
  private static final HttpClient client = HttpClient.newHttpClient();
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String TOKEN_VALIDATION_URL = "https://api.cloudflare.com/client/v4/user/tokens/verify";
  private final AuthConfig authConfig;

  public CloudFlareService(AuthConfig authConfig) {
    this.authConfig = authConfig;
    if (!isValidToken()) {
      throw new IllegalArgumentException("Invalide auth configurations");
    }
  }

  public boolean isValidToken() {
    String authHeader = "Authorization";
    HttpRequest getRequest = HttpRequest.newBuilder()
        .uri(URI.create(TOKEN_VALIDATION_URL))
        .header(authHeader, "Bearer " + authConfig.getAuthKey())
        .GET()
        .build();

    HttpResponse<String> response;
    try {
      response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      log.error("Failed to validate token", e);
      Thread.currentThread().interrupt();
      return false;
    }

    TokenVerifyResponse verify;
    try {
      verify = objectMapper.readValue(response.body(), TokenVerifyResponse.class);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse token", e);
      return false;
    }

    if (verify.isSuccess() && verify.getResult() != null && "active".equalsIgnoreCase(verify.getResult().getStatus())) {
      log.info("Successfully validated token");
      return true;
    }

    log.error("Failed to validate token. Status: {}", verify.getResult() != null
        ? verify.getResult().getStatus() : "");
    return false;

  }

  public void listDNS() {/*
}
