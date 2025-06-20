package com.roomelephant.elephlink.infra;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ArgsParser {
  public Map<String, String> parse(String... parameters) {
    return Arrays.stream(parameters)
        .filter(arg -> arg.startsWith("--") && arg.contains("="))
        .map(arg -> arg.substring(2).split("=", 2))
        .filter(parts -> parts.length == 2)
        .collect(Collectors.toMap(
            parts -> parts[0],
            parts -> parts[1],
            (existing, replacement) -> replacement
        ));
  }
}
