package com.roomelephant.elephlink.adapters.cloudflare;

import com.roomelephant.elephlink.domain.model.RequestFailedException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CFRequest {
  private static final HttpClient client = HttpClient.newHttpClient();
  private static final String BASE_URL = "https://api.cloudflare.com/client/v4";

  public String get(String url, Map<String, String> headers) {
    HttpRequest.Builder builder = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(BASE_URL + url))
        .header("Accept", "application/json");
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder.header(entry.getKey(), entry.getValue());
    }
    HttpRequest request = builder.build();

    HttpResponse<String> response;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      log.error("Failed to validate token", e);
      Thread.currentThread().interrupt();
      throw new RequestFailedException("Failed to make request to " + url, e);
    }
    return response.body();
  }
}
