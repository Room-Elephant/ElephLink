package com.roomelephant.elephlink.domain;

import com.roomelephant.elephlink.domain.model.DnsRecord;
import com.roomelephant.elephlink.domain.model.RequestFailedException;
import java.util.Optional;

/**
 * Facade interface for managing DNS records on a DNS service.
 *
 * <p>This interface provides a simplified API for common DNS operations,
 * including retrieving DNS records and updating existing records.</p>
 */
public interface DnsService {
  /**
   * Initializes the DNS service.
   *
   * <p>This method should be called before attempting to use the service.
   * It can be used to set up any necessary resources, configure external
   * providers, or perform initial checks to ensure the service is ready
   * for use.</p>
   *
   * @return {@code true} if the service was initialized successfully; {@code false} otherwise.
   *          A return of {@code false} may indicate issues with configuration,
   *          network connectivity, or availability of the service.
   */
  boolean init();

  /**
   * Retrieves a DNS record by its name.
   *
   * <p>This method attempts to find and return a specific DNS record associated with the
   * dns service account based on its name. The record name should typically be the fully
   * qualified domain name (FQDN) of the record (e.g., "www.example.com").</p>
   *
   * @param recordName The name of the DNS record to retrieve (e.g., "mywebapp.example.com").
   * @return An {@link Optional} containing the {@link DnsRecord} if found, or an empty
   *         {@link Optional} if no record with the given name exists or an error occurs during retrieval.
   * @throws RequestFailedException if an error occurs while communicating with the DNS service API
   *         or processing the response.
   */
  Optional<DnsRecord> getDnsRecord(String recordName);

  /**
   * Updates an existing DNS record on DNS service.
   *
   * <p>This method takes a {@link DnsRecord} object and attempts to update the corresponding
   * record on the DNS service. The {@code DnsRecord} object should typically contain
   * the record's ID to ensure the correct record is updated.</p>
   *
   * @param dnsRecord The {@link DnsRecord} object containing the updated information.
   *                  This object must contain sufficient information (e.g., record ID)
   *                  for the DNS service API to identify and update the correct record.
   * @return {@code true} if the record was successfully updated; {@code false} otherwise.
   */
  boolean updateRecord(DnsRecord dnsRecord);
}
