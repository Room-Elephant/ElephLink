package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TokenVerifyResponse {
  private boolean success;
  private Result result;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class Result {
    private String status;
  }
}
