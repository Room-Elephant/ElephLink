package com.roomelephant.elephlink.adapters.cloudflare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class DnsRecordsResponse {
  private boolean success;
  private List<ApiError> errors;
  private List<ApiMessage> messages;
  private List<RecordResponse> result;

  @JsonProperty("result_info")
  public ResultInfo resultInfo;

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ApiError {
    private int code;
    private String message;
    @JsonProperty("documentation_url")
    private String documentationUrl;
  }

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ApiMessage {
    private int code;
    private String message;
    @JsonProperty("documentation_url")
    private String documentationUrl;
  }

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ResultInfo {
    private int count;
    private int page;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total_count")
    private int totalCount;
  }

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class RecordResponse {
    private String id;
    private DnsRecordType type;
    private String name;
    private String content;
    private boolean proxiable;
    private boolean proxied;
    private int ttl;
    private boolean locked;

    @JsonProperty("zone_id")
    private String zoneId;

    @JsonProperty("zone_name")
    private String zoneName;

    @JsonProperty("created_on")
    private String createdOn;

    @JsonProperty("modified_on")
    private String modifiedOn;
  }

  public enum DnsRecordType {
    A, AAAA, CAA, CERT, CNAME, DNSKEY, DS,
    HTTPS, LOC, MX, NAPTR, NS, OPENPGPKEY,
    PTR, SMIMEA, SRV, SSHFP, SVCB, TLSA, TXT, URI
  }
}
