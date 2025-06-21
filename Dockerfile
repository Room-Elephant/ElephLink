FROM eclipse-temurin:21-jre-jammy

RUN groupadd -r elephlinkGroup \
    && useradd -r -g elephlinkGroup elephlinkUser

WORKDIR /app

COPY target/elephlink-jar-with-dependencies.jar elephlink/app.jar
COPY src/main/resources/config.yml /config/config.yml
COPY src/main/resources/iplist.yml /config/iplist.yml
COPY src/main/resources/records.yml /config/records.yml

COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

USER elephlinkUser
CMD ["./entrypoint.sh"]
