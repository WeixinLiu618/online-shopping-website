FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
RUN mvn -q -DskipTests package

FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

