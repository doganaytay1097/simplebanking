
FROM eclipse-temurin:11-jdk AS build
WORKDIR /app


COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./


RUN sed -i 's/\r$//' gradlew && chmod +x gradlew


RUN ./gradlew --no-daemon dependencies || true


COPY src src


RUN ./gradlew --no-daemon clean bootJar -x test

# ===== RUNTIME STAGE =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
