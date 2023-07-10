FROM eclipse-temurin:17

COPY target/hungarian_hamster_resque-0.0.1-SNAPSHOT.jar app.jar

CMD ["java","-jar","app.jar"]