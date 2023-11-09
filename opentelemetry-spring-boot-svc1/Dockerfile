FROM eclipse-temurin:21-alpine

COPY target/*.jar spring-cloud-open-telemetry.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/spring-cloud-open-telemetry.jar"]