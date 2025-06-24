package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
class DnsRecordsResponse {
  private List<RecordResponse> result;

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class RecordResponse {
    private String id;
    private DnsRecordType type;
    private String name;
    private String content;
    private int ttl;
  }

  enum DnsRecordType { A }
}
