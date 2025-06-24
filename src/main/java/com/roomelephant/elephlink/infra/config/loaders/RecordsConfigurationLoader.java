package com.roomelephant.elephlink.infra.config.loaders;

import static com.roomelephant.elephlink.infra.config.ConfigurationFiles.DNS_RECORDS_FILE;
import static com.roomelephant.elephlink.infra.config.loaders.RecordsConfigurationLoader.RecordProperties.CRON;
import static com.roomelephant.elephlink.infra.config.loaders.RecordsConfigurationLoader.RecordProperties.RECORDS;

import com.roomelephant.elephlink.domain.model.DnsRecordsConfig;
import java.util.List;
import java.util.Map;

public class RecordsConfigurationLoader extends BaseConfigurationLoader<DnsRecordsConfig> {

  @Override
  protected DnsRecordsConfig convert(Map<String, Object> ymlConfig) {
    Object o = ymlConfig.get(RECORDS.key());

    List<String> list = switch (o) {
      case List<?> casted -> castToListString(casted);
      case String casted -> List.of(casted);
      case null, default ->
          throw new IllegalArgumentException("Invalid dns records configuration. Invalid format for dns records.");
    };

    return DnsRecordsConfig.builder()
        .records(list)
        .cronExpression(getAndValidateString(ymlConfig, CRON.key()))
        .build();

  }

  private String getAndValidateString(Map<String, Object> ymlConfig, String key) {
    String textConfig = (String) ymlConfig.get(key);
    if (textConfig == null || textConfig.isEmpty()) {
      throw new IllegalArgumentException("Invalid dns records configuration. " + key + " is required.");
    }
    return textConfig;
  }

  @Override
  protected String getFileName() {
    return DNS_RECORDS_FILE.key();
  }

  public enum RecordProperties {
    RECORDS("records"),
    CRON("cron-expression");

    private final String key;

    RecordProperties(String key) {
      this.key = key;
    }

    public String key() {
      return key;
    }
  }
}
