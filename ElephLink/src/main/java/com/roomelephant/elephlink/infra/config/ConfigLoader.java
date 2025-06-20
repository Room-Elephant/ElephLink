package com.roomelephant.elephlink.infra.config;

public interface ConfigLoader<T> {
  T load(String fileName);
}
