FROM ubuntu:latest AS build
LABEL authors="Wagner Rosa"

RUN apt update \
    apt install wget -y
RUN wget https://download.java.net/java/early_access/jdk21/28/GPL/openjdk-21-ea+xx_linux-x64_bin.tar.gz
RUN tar -xvf openjdk-21-ea+28_linux-x64_bin.tar.gz
RUN cd jdk-21
RUN mkdir -p /usr/local/jdk-21 && mv * /usr/local/jdk-21

COPY . .

RUN apt install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim
EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]