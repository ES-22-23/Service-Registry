FROM maven:3.8.6-amazoncorretto-11

COPY . /app
WORKDIR /app

RUN mvn package -DskipTests=true
RUN mv target/*.jar service-registry.jar

EXPOSE 9090

ENTRYPOINT [ "java", "-jar", "service-registry.jar" ]
