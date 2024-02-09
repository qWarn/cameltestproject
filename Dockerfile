ARG JDK_VERSION=21

FROM openjdk:${JDK_VERSION}

WORKDIR CamelProject

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/camelproject-0.0.1-SNAPSHOT.jar"]


