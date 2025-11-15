FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /payment
COPY gradlew .
COPY gradle gradle

COPY build.gradle settings.gradle ./

COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /payment/build/libs/*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]