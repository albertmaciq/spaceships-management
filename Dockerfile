FROM openjdk:21-jdk
WORKDIR /spaceships-api
COPY target/spaceships-1.0.0-RELEASE.jar /spaceships-api
EXPOSE 8080
CMD ["java", "-jar", "spaceships-1.0.0-RELEASE.jar"]
