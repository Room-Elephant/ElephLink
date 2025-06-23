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
public class DnsRecordsResponse {
  private List<RecordResponse> result;

  @ToString
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class RecordResponse {
    private String id;
    private DnsRecordType type;
    private String name;
    private String content;
    private boolean proxied;
    private int ttl;
  }

  public enum DnsRecordType {
    A, AAAA, CAA, CERT, CNAME, DNSKEY, DS,
    HTTPS, LOC, MX, NAPTR, NS, OPENPGPKEY,
    PTR, SMIMEA, SRV, SSHFP, SVCB, TLSA, TXT, URI
  }
}
