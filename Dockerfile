FROM registry.trendyol.com/platform/base/image/seller/productivity/jre-17:trendyol-semeru-openj9-jre-17-alpine_newrelic-7.4
ENV LANG en_GB.UTF-8
ENV ARTIFACT_NAME="internationalcommissioninvoice-api.jar"
COPY build/*.jar /app/$ARTIFACT_NAME
EXPOSE 8080
RUN apk update && apk add bash && apk add --update ttf-dejavu && rm -rf /var/cache/apk/* && apk add fontconfig
ENTRYPOINT java -javaagent:/newrelic.jar -Djava.security.egd=file:/dev/./urandom -jar /app/$ARTIFACT_NAME
