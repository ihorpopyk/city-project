FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/city-project-0.0.1-SNAPSHOT.jar city-project-0.0.1-SNAPSHOT.jar
ENV PORT 8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","city-project-0.0.1-SNAPSHOT.jar"]