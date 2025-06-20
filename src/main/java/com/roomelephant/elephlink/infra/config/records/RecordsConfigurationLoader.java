package com.roomelephant.elephlink.infra.config.records;

import static com.roomelephant.elephlink.domain.model.RecordsConfig.RecordProperties.RECORDS;

import com.roomelephant.elephlink.domain.model.RecordsConfig;
import com.roomelephant.elephlink.infra.config.BaseConfigurationLoader;
import java.util.List;
import java.util.Map;

public class RecordsConfigurationLoader extends BaseConfigurationLoader<RecordsConfig> {
  protected static final String DEFAULT_FILE = "records.yml";

  @Override
  protected RecordsConfig convert(Map<String, Object> ymlConfig) {
    Object o = ymlConfig.get(RECORDS.key());

    List<String> list = switch (o) {
      case List<?> casted -> castToListString(casted);
      case String casted -> List.of(casted);
      case null, default -> throw new IllegalArgumentException("Invalid configuration for records");

    };

    return RecordsConfig.builder()
        .records(list)
        .build();

  }

  @Override
  protected String getFileName() {
    return DEFAULT_FILE;
  }
}
