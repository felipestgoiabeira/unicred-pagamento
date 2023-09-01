FROM maven:3.6.3-openjdk-17 as builder
WORKDIR /application

COPY pom.xml pom.xml
COPY associate/pom.xml  associate/pom.xml
COPY ticket/pom.xml  ticket/pom.xml
COPY files/pom.xml  files/pom.xml
COPY common/pom.xml  common/pom.xml

RUN mvn dependency:go-offline

COPY . /application

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17
COPY --from=builder /application/associate/target/application.jar associate/application.jar
COPY --from=builder /application/ticket/target/application.jar ticket/application.jar
COPY --from=builder /application/files/target/application.jar files/application.jar
EXPOSE 8080
