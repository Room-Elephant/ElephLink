package com.roomelephant.elephlink.domain;

import java.util.Optional;

public interface IpService {
  boolean init();

  Optional<String> fetchCurrentIp();
}
