package com.roomelephant.elephlink.domain.model;

import java.time.Duration;
import java.util.List;
import lombok.Builder;

@Builder
public record IpServiceConfig(
    List<String> services,
    Duration timeout
) {
}
