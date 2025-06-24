package com.roomelephant.elephlink.adapters.cloudflare;

import lombok.Builder;

@Builder
public record CloudflareConfig(
    String email,
    String key,
    String zoneIdentifier
) {
}
