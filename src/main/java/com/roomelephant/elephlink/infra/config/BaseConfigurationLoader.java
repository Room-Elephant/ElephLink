package com.roomelephant.elephlink.infra.config;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseConfigurationLoader<T> implements ConfigLoader<T> {
  private final YmlParser ymlLoader;

  protected BaseConfigurationLoader() {
    this.ymlLoader = new YmlParser();
  }

  @Override
  public T load(String fileName) {
    String path = fileName == null || fileName.isBlank() ? getFileName() : fileName;
    Map<String, Object> ymlConfig = ymlLoader.parse(path);

    if (ymlConfig == null) {
      throw new IllegalArgumentException("No configurations found in file " + fileName);
    }

    return convert(ymlConfig);
  }

  protected abstract T convert(Map<String, Object> ymlConfig);

  protected abstract String getFileName();


  protected List<String> castToListString(List<?> casted) {
    return casted.stream()
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .toList();
  }
}
