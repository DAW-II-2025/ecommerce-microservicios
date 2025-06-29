FROM openjdk:17.0.2 as builder

WORKDIR /app/msvc-archivos

COPY ./pom.xml /app
COPY ./msvc-archivos/.mvn ./.mvn
COPY ./msvc-archivos/mvnw .
COPY ./msvc-archivos/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-archivos/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-archivos/target/msvc-archivos-0.0.1-SNAPSHOT.jar .

EXPOSE 8083

CMD ["java", "-jar", "msvc-archivos-0.0.1-SNAPSHOT.jar"]