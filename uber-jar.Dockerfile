FROM bellsoft/liberica-openjdk-debian:21.0.5-11 AS builder
WORKDIR /build

COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

COPY ./src src/
COPY pom.xml pom.xml

RUN ./mvnw package -DskipTests
RUN mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

FROM bellsoft/liberica-openjre-alpine:21.0.5-11

ENV SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/tasktrackerrestapi?createDatabaseIfNotExist=true"
ENV SPRING_DATASOURCE_USERNAME="postgres"
ENV SPRING_RABBITMQ_HOST="host.docker.internal"
ENV SPRING_RABBITMQ_USERNAME="guest"
ENV SPRING_RABBITMQ_PASSWORD="guest"

WORKDIR /app

RUN apk add --no-cache curl

COPY --from=builder /build/target/app.jar app.jar

HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]