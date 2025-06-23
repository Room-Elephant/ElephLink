package com.roomelephant.elephlink.infra.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

@Slf4j
public class YmlParser {

  @SuppressWarnings("unchecked")
  public Map<String, Object> parse(String fileName) {
    try (FileInputStream fis = new FileInputStream(Path.of(fileName).toFile()); Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
      LoadSettings settings = LoadSettings.builder().build();
      Load load = new Load(settings);
      return (Map<String, Object>) load.loadFromReader(reader);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File '" + fileName + "' not found.", e);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error while loading the file '" + fileName + "'.", e);
    } catch (Exception e) {
      throw new IllegalArgumentException("Something went wrong loading the file '" + fileName + "'.", e);
    }
  }
}
