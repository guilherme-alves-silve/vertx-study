# Vert.x Study

Based on the course [Learn Vert.x - Reactive microservices with Java](https://www.udemy.com/course/reactive-web-applications-with-vertx-and-vuejs)

## Tecnologies

* [vert.x](https://vertx.io/)
* [gradle](https://gradle.org/)
* [maven](https://maven.apache.org/)
* [jib](https://github.com/GoogleContainerTools/jib)
* [slf4j](https://www.slf4j.org/)
* [log4j2](https://logging.apache.org/log4j/2.x/)
* [logback](https://logback.qos.ch/)
* [vegeta](https://github.com/tsenart/vegeta)
* [PostgreSQL](https://www.postgresql.org/)
* [MySQL](https://www.mysql.com/)
* [Quarkus](https://quarkus.io/)

## Sections of the course
1. [X] Introduction
2. [X] Vert.x Core
3. [X] Vert.x Web
4. [X] Vert.x Config
5. [X] Vert.x Data - Reactive SQL Clients
6. [X] Vert.x Reactive - Mutiny
7. [X] Quarkus Reactive and Vert.x
8. [X] Vert.x Web Sockets
9. [X] Ending

## Building

### Gradle
To launch your tests:
```
./gradlew clean test
```

To package your application:
```
./gradlew clean assemble
```

To build container:
```
./gradlew jibDockerBuild
docker run -t -i -p 8888:8888 example/vertx-starter
```

To run your application:
```
./gradlew clean run
```

### Maven

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```

## Vegeta Load Testing

`echo "GET http://localhost:8888/assets" | vegeta attack -workers=4 -max-workers=10 -duration=30s`

## GIT

``
git add . & git commit -m"Update" & git push
``

## Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://discord.gg/6ry7aqPWXy[Vert.x Discord]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]
