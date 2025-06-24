package com.roomelephant.elephlink.adapters;

import java.net.http.HttpClient;
import lombok.Getter;

public class SharedHttpClient {
  @Getter
  private static final HttpClient client = HttpClient.newHttpClient();

  private SharedHttpClient() {
  }

}
