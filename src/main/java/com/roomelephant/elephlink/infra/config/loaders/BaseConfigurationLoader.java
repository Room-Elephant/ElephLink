package com.roomelephant.elephlink.infra.config.loaders;

import com.roomelephant.elephlink.infra.ConfigLoader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseConfigurationLoader<T> implements ConfigLoader<T> {
  private final YmlParser ymlLoader;

  protected BaseConfigurationLoader() {
    this.ymlLoader = new YmlParser();
  }

  @Override
  public T load(String fileName) {
    String path = fileName == null || fileName.isBlank() ? getFileName() : fileName;

    Path configPath;
    Path fileNamePath = Paths.get(path);
    if (fileNamePath.isAbsolute()) {
      configPath = fileNamePath;
    } else {
      try {
        Path jarDir = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        configPath = jarDir.resolve(path);
      } catch (URISyntaxException e) {
        configPath = fileNamePath;
      }
    }

    Map<String, Object> ymlConfig = ymlLoader.parse(configPath.toString());

    if (ymlConfig == null) {
      throw new IllegalArgumentException("No configurations found in file " + fileName + ".");
    }

    T config = convert(ymlConfig);
    log.debug("Loaded configuration file '{}': {}", fileName, config);
    return config;
  }

  protected abstract T convert(Map<String, Object> ymlConfig);

  protected abstract String getFileName();

  protected List<String> castToListString(List<?> casted) {
    return casted.stream()
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .toList();
  }

  protected static Duration getDuration(Map<String, Object> ymlConfig, String key) {
    Long timeoutRaw = null;
    try {
      timeoutRaw = Long.valueOf((Integer) ymlConfig.get(key));
    } catch (Exception e) {
      timeoutRaw = 1000L;

    }
    return Duration.ofMillis(timeoutRaw);
  }
}
