# https://hub.docker.com/_/eclipse-temurin/
# - docker build . -t example/vertx-starter
# - docker run -t -i -p 8888:8888 example/vertx-starter
FROM amazoncorretto:17

# Alternative
#FROM eclipse-temurin:17.0.6_10-jre

ENV FAT_JAR vertx-starter-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app

EXPOSE 8888

RUN mkdir $APP_HOME
COPY build/libs/$FAT_JAR $APP_HOME

WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
