FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY /target/hw17-0.0.1-SNAPSHOT.jar /app/hw17.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hw17.jar"]