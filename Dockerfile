FROM amazoncorretto:17-alpine

WORKDIR /app

COPY target/clustereddatawarehouse-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "/app/clustereddatawarehouse-0.0.1-SNAPSHOT.jar" ]