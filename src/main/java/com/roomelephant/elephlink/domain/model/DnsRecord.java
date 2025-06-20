package com.roomelephant.elephlink.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DnsRecord {
  private String id;
  private Type type;
  private String name;
  private String content;
  private int ttl;
  private boolean proxie;

  public enum Type { A }
}