# Vertx-starter

Based on the course [Learn Vert.x - Reactive microservices with Java](https://www.udemy.com/course/reactive-web-applications-with-vertx-and-vuejs)

## Tecnologies

* [jib](https://github.com/GoogleContainerTools/jib)

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

## Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://discord.gg/6ry7aqPWXy[Vert.x Discord]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]
