package com.roomelephant.elephlink.adapters.ipservice;

import com.roomelephant.elephlink.domain.IpService;
import com.roomelephant.elephlink.domain.model.IpServiceConfig;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpServiceImpl implements IpService {
  private static final Pattern ipv4Pattern = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
  private static final HttpClient client = HttpClient.newHttpClient();

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
      return client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      log.debug("Failed to fetch current ip.", e);
      Thread.currentThread().interrupt();
      return null;
    }
  }
}
