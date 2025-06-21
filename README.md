# ElephLink
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/Room-Elephant/ElephLink/blob/main/LICENSE) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Room-Elephant_ElephLink&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Room-Elephant_ElephLink) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Room-Elephant_ElephLink&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Room-Elephant_ElephLink) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Room-Elephant_ElephLink&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Room-Elephant_ElephLink) [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Room-Elephant_ElephLink&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Room-Elephant_ElephLink)

**ElephLink** is a powerful, lightweight Dynamic DNS (DDNS) client that uses the Cloudflare API to keep your domain name pointing to your home network.

It allows you to reliably access self-hosted services (like a NAS, media server, or smart home hub) via a custom domain, even when your Internet Service Provider assigns you a dynamic IP address.

## Why Elephlink?
- Cost-Effective: No need to pay your ISP for a static IP address.
- Reliable: Runs on a schedule to automatically detect IP changes and update your DNS records in seconds.
- Flexible: Supports multiple domains, configurable IP detection services, and cron-based scheduling.
- Secure: Works with modern Cloudflare API tokens for granular, secure access.
- Footprint: Low footprint on your system with less than 10 mb of ram


## Features
- Seamless integration with the Cloudflare v4 API.
- Supports multiple DNS records across different domains.
- Periodic IP checking with cron-style scheduling.
- Pluggable IP detection using a configurable list of providers.
- All configuration is external, making it perfect for containerized deployments.

## How it Works
1. **IP Detection**: Regularly checks your public IP using one or more external services.
2. **Cloudflare Validation**: Authenticates with Cloudflare and verifies your DNS records.
3. **DNS Update**: If your public IP changes, automatically updates the specified Cloudflare DNS records.
4. **Scheduling**: Runs on a user-defined schedule (e.g., every few minutes) using cron expressions.

## Getting Started

### Prerequisites
- A Cloudflare account.
- A registered domain name managed by Cloudflare.
- An A record created in your Cloudflare DNS dashboard that you want to keep updated. The initial IP can be a placeholder like 1.1.1.1.
- A Cloudflare API Token (recommended) or your Global API Key.
- Java 21 or higher
- Maven 3.6+ (only required if building from source).

| Type | Name       | Content | Proxy status | TTL  | 
|------|------------|---------|--------------|------|
| A    | domain.com | 1.1.1.1 | Proxied      | Auto |


### Running ElephLink

1. **Build from Source**

Clone the repository and build the project using Maven. This will create a single executable JAR file.
```bash
git clone https://github.com/Room-Elephant/ElephLink.git
cd ElephLink
mvn clean install
```
2. **Configuration**

Create three YAML configuration files. You can start by copying the examples from src/main/resources/.

[config.yml](https://github.com/Room-Elephant/ElephLink/blob/main/src/main/resources/config.yml): This file handles your Cloudflare API credentials.
```yml
# The email associated with your Cloudflare account.
authEmail: user@example.com
# "token" for a Scoped API Token (recommended) or "global" for a Global API Key.
authMethod: token
# Your Cloudflare API token or key.
authKey: YOUR_CLOUDFLARE_API_TOKEN
# The Zone Identifier for your domain, found on the "Overview" page in the Cloudflare dashboard.
zoneIdentifier: YOUR_ZONE_ID
```
[records.yml](https://github.com/Room-Elephant/ElephLink/blob/main/src/main/resources/records.yml): This file defines which DNS records to update and how often.
```yml
# A list of DNS records you want ElephLink to manage.
records:
  - subdomain.yourdomain.com
  - another.yourdomain.com
# UNIX Cron Expression for the update schedule.
# This example runs every day at 04:00 AM
cronExpression: "0 4 * * *" 
```
A quick guide to cron expressions:
```
 ┌───────────── minute (0 - 59)
 │ ┌───────────── hour (0 - 23)
 │ │ ┌───────────── day of month (1 - 31)
 │ │ │ ┌───────────── month (1 - 12 or JAN-DEC)
 │ │ │ │ ┌───────────── day of week (0 - 6 or SUN-SAT, 0 = Sunday)
 │ │ │ │ │
 │ │ │ │ │
 * * * * *
```

[iplist.yml](https://github.com/Room-Elephant/ElephLink/blob/main/src/main/resources/iplist.yml): A list of web services that return your public IP address as plain text. ElephLink will try them in order until one succeeds.
```yml
# A list of plaintext IP reflector services.
ip-services:
  - https://api.ipify.org
  - https://icanhazip.com
  - https://ipinfo.io/ip
```

3. **Run the Application**
```bash
java -jar elephlink-1.0.0-jar-with-dependencies.jar \
   --authConfigurationFile=path/to/auth.yaml \
   --recordsConfigurationFile=path/to/records.yaml \
   --ipListConfigurationFile=path/to/ipservices.yaml
```
If your configuration files are in the same directory as the JAR, you can omit the arguments:
```bash
java -jar elephlink-1.0.0-jar-with-dependencies.jar
```
The application will validate your configuration, verify the DNS records in Cloudflare, and start the update schedule.

## Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## Acknowledgments
This project was inspired by the functionality of the K0p1-Git/cloudflare-ddns-updater bash script.

## License
This project is licensed under the MIT License. See the LICENSE file for details.