package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class UpdateRecordResponse {
  private boolean success;
  private String message;
}