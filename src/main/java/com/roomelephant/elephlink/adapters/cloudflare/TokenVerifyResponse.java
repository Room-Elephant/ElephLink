package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class TokenVerifyResponse {
  private boolean success;
  private Result result;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  @Setter
  public static class Result {
    private Status status;
  }

  enum Status {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("disabled")
    DISABLED,
    @JsonProperty("expired")
    EXPIRED
  }
}
