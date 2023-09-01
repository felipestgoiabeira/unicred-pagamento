FROM maven:3.6.3-openjdk-17 as builder
WORKDIR /application

COPY . /application
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17
COPY --from=builder /application/associate/target/application.jar associate/application.jar
COPY --from=builder /application/ticket/target/application.jar ticket/application.jar
EXPOSE 8080
