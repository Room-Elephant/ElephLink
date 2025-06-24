package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomelephant.elephlink.adapters.SharedHttpClient;
import com.roomelephant.elephlink.domain.model.RequestFailedException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CfRequest {
  private static final String BASE_URL = "https://api.cloudflare.com/client/v4";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  <T> T get(String url, Map<String, String> headers, Class<T> clazz) {
    HttpRequest.Builder builder = HttpRequest.newBuilder()
        .GET();
    return doRequest(url, headers, builder, clazz);
  }

  <T> T patch(String url, Map<String, String> headers, Object rawBody, Class<T> clazz) {
    String body;
    try {
      body = objectMapper.writeValueAsString(rawBody);
    } catch (JsonProcessingException e) {
      throw new RequestFailedException("Failed to parse response.", e);
    }
    HttpRequest.Builder builder = HttpRequest.newBuilder()
        .method("PATCH", HttpRequest.BodyPublishers.ofString(body));
    return doRequest(url, headers, builder, clazz);
  }

  private <T> T doRequest(String url, Map<String, String> headers, HttpRequest.Builder builder, Class<T> clazz) {
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder.header(entry.getKey(), entry.getValue());
    }
    HttpRequest request = builder
        .uri(URI.create(BASE_URL + url))
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> response;
    try {
      response = SharedHttpClient.getClient().send(request, HttpResponse.BodyHandlers.ofString());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RequestFailedException("Interrupted while making request to " + url, e);
    } catch (Exception e) {
      throw new RequestFailedException("Failed to make request to " + url, e);
    }

    try {
      return objectMapper.readValue(response.body(), clazz);
    } catch (JsonProcessingException e) {
      throw new RequestFailedException("Failed to parse " + clazz.getName() + " response.", e);
    }
  }
}
