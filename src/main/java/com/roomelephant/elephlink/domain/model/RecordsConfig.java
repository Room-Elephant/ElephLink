package com.roomelephant.elephlink.domain.model;

import java.util.List;
import lombok.Builder;

@Builder
public record RecordsConfig(
    List<String> records,
    String cronExpression
) {
  public enum RecordProperties {
    RECORDS("records"),
    CRON("cronExpression");

    private final String key;

    RecordProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}