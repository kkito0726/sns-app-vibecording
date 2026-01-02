FROM gradle:8.5.0-jdk21 AS build

ARG BUILD_DIR=/home/build

COPY ./app ${BUILD_DIR}/app
COPY ./jooq-generator ${BUILD_DIR}/jooq-generator
COPY ./flyway-migration ${BUILD_DIR}/flyway-migration
COPY ./build.gradle.kts ${BUILD_DIR}
COPY ./settings.gradle.kts ${BUILD_DIR}

WORKDIR ${BUILD_DIR}

RUN gradle app:presentation:bootJar

FROM openjdk:26-ea-21-oraclelinux8

RUN mkdir /app

EXPOSE 80

COPY --from=build "/home/build/app/presentation/build/libs/presentation-0.0.1-SNAPSHOT.jar" /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
