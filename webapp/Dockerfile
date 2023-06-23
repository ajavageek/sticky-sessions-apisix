#docker build -t sticky-session:1.0 .
FROM eclipse-temurin:17-jdk-jammy as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2,rw ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-jammy

COPY --from=build target/sticky-sessions-1.0.jar sticky-sessions.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "sticky-sessions.jar"]
