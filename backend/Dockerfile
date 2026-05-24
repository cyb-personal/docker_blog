FROM maven:3.8-openjdk-8-slim AS builder
WORKDIR /build
COPY pom.xml .
COPY common common
COPY user-service user-service
COPY post-service post-service
COPY comment-service comment-service
COPY like-service like-service
COPY gateway-service gateway-service
ARG SERVICE_NAME
RUN mvn clean package -pl ${SERVICE_NAME} -am -DskipTests -q

FROM eclipse-temurin:8-jre-focal

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

WORKDIR /app
ARG SERVICE_NAME
COPY --from=builder /build/${SERVICE_NAME}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
