FROM openjdk:17-alpine as builder
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY settings.gradle.kts build.gradle.kts  gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN chmod +x gradlew
RUN ./gradlew build || return 0
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar
RUN cp build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080
CMD ["java","-jar","/app/spring-boot-application.jar"]
