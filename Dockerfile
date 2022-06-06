FROM registry.trendyol.com/platform/base/image/seller/productivity/jre-17:temurin-jre-17-alpine_newrelic-7.4
ENV LANG en_GB.UTF-8
ENV ARTIFACT_NAME="internationalcommissioninvoice-api.jar"
COPY ./src/main/resources/globaltrust.jks /var/ssl/private/globaltrust.jks
COPY build/*.jar /app/$ARTIFACT_NAME
EXPOSE 8080
ENTRYPOINT java -javaagent:/newrelic.jar -Djava.security.egd=file:/dev/./urandom -jar /app/$ARTIFACT_NAME
