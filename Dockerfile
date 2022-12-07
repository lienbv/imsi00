FROM openjdk:11
EXPOSE 1507
ADD target/vibee-0.0.1-SNAPSHOT.jar vibee-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "vibee-0.0.1-SNAPSHOT.jar"]