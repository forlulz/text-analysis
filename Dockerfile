FROM openjdk:17-alpine
RUN addgroup -S textanalysis && adduser -S textanalysis -G textanalysis
USER textanalysis:textanalysis
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} text-analysis.jar
ENTRYPOINT ["java","-jar","/text-analysis.jar"]
