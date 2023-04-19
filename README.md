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

## Sections of the course
1. [X] Introduction
2. [X] Vert.x Core
3. [ ] Vert.x Web
4. [ ] Vert.x Config
5. [ ] Vert.x Data - Reactive SQL Clients
6. [ ] Vert.x Reactive - Mutiny
7. [ ] Quarkus Reactive and Vert.x
8. [ ] Vert.x Web Sockets
9. [ ] Ending

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
