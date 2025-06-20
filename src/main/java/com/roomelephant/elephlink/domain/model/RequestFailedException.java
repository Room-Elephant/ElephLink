package com.roomelephant.elephlink.domain.model;

public class RequestFailedException extends RuntimeException {
  public RequestFailedException(String reason, Exception e) {
  }
}
