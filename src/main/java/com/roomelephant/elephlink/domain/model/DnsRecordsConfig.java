package com.roomelephant.elephlink.domain.model;

import java.time.Duration;
import java.util.List;
import lombok.Builder;

@Builder
public record DnsRecordsConfig(
    List<String> records,
    String cronExpression,
    Duration timeout
) {
}