package com.roomelephant.elephlink.adapters.ipservice;

import com.roomelephant.elephlink.adapters.SharedHttpClient;
import com.roomelephant.elephlink.domain.IpService;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpServiceImpl implements IpService {
  private static final Pattern ipv4Pattern = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");

  private final IpServiceConfig config;
  private List<HttpRequest> requests;

  public IpServiceImpl(IpServiceConfig config) {
    this.config = config;
  }

  @Override
  public boolean init() {
    try {
      requests = config.services().stream().map(ipService -> HttpRequest.newBuilder()
          .uri(URI.create(ipService))
          .timeout(config.timeout())
          .GET()
          .build()).toList();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public Optional<String> fetchCurrentIp() {
    for (HttpRequest request : requests) {
      HttpResponse<String> response = request(request);

      if (response == null) {
        continue;
      }

      String ip = response.body().trim();
      if (ipv4Pattern.matcher(ip).matches()) {
        log.debug("Current public IP {}.", ip);
        return Optional.of(ip);
      }
    }

    return Optional.empty();
  }

  private HttpResponse<String> request(HttpRequest request) {
    try {
      return SharedHttpClient.getClient().send(request, HttpResponse.BodyHandlers.ofString());
    } catch (HttpTimeoutException e) {
      log.warn("Request timed out from {}.", request.uri(), e);
      return null;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.debug("Interrupted while fetching current IP from {}.", request.uri(), e);
      return null;
    } catch (Exception e) {
      log.warn("Failed to fetch current IP from {}.", request.uri(), e);
      return null;
    }
  }
}
