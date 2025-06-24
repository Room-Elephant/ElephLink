package com.roomelephant.elephlink.infra;

/**
 * Generic interface for loading configuration files.
 *
 * <p>This interface provides a contract for implementations that are responsible for loading
 * configuration data from a specified file. Implementations will typically parse the file
 * content and transform it into a specific configuration object type {@code T}.</p>
 *
 * @param <T> the type of the configuration object that will be loaded by this loader.
 */
public interface ConfigLoader<T> {
  /**
   * Loads configuration data from the specified file.
   *
   * @param fileName The name or path of the configuration file to load. This could be a relative
   *                 path, an absolute path, or a resource name depending on the specific implementation.
   * @return An instance of the configuration object of type {@code T}, populated with data
*            from the loaded file.
   * @throws IllegalArgumentException If something is miss configured. Either filename, or
   *                                  invalid properties
   */
  T load(String fileName);
}
