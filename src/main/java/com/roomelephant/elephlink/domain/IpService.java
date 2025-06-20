package com.roomelephant.elephlink.domain;

import java.util.Optional;

/**
 * Interface for retrieving the user's external IP address.
 *
 * <p>This interface defines methods for initializing the IP service and
 * fetching the current external IP address of the user. Implementations
 * will typically interact with one or more external IP providers to
 * determine the public IP address.</p>
 */
public interface IpService {
  /**
   * Initializes the IP service.
   *
   * <p>This method should be called before attempting to fetch the current IP.
   * It can be used to set up any necessary resources, configure external
   * IP providers, or perform initial checks to ensure the service is ready
   * for use.</p>
   *
   * @return {@code true} if the service was initialized successfully; {@code false} otherwise.
   *          A return of {@code false} may indicate issues with configuration,
   *          network connectivity, or availability of external IP providers.
   */
  boolean init();

  /**
   * Fetches the current external IP address of the user.
   *
   * <p>This method queries the configured external IP providers to determine
   * the public IP address that the user's network is currently using.
   * The order and selection of providers are implementation-specific.</p>
   *
   * @return An {@link Optional} containing the external IP address as a {@code String}
   *         if successfully retrieved; an empty {@link Optional} otherwise.
   *         An empty {@code Optional} may indicate network issues,
   *         unavailability of IP providers, or other errors during the retrieval process.
   */
  Optional<String> fetchCurrentIp();
}
