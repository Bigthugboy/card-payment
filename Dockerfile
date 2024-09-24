FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests

FROM openjdk:11
COPY --from=build web/target/*.jar app.jar
# ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "-Dspring.profiles.active=${PROFILE}","app.jar"]
ENTRYPOINT [ "java", "-jar", "app.jar" ]