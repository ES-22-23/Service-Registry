FROM maven:3.8.6-amazoncorretto-11

COPY target/*.jar service-registry.jar

EXPOSE 9090

ENTRYPOINT [ "java", "-jar", "/service-registry.jar" ]