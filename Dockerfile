FROM eclipse-temurin:21-jre-jammy

RUN groupadd -r elephlinkGroup \
    && useradd -r -g elephlinkGroup elephlinkUser

WORKDIR /app

COPY target/elephlink-jar-with-dependencies.jar elephlink/app.jar
COPY src/main/resources/auth.yml /config/auth.yml
COPY src/main/resources/ip-services.yml /config/ip-services.yml
COPY src/main/resources/dns-records.yml /config/dns-records.yml

COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

USER elephlinkUser
CMD ["./entrypoint.sh"]
