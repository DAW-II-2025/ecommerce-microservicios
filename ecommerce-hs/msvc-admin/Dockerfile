FROM openjdk:17.0.2 as builder

WORKDIR /app/msvc-admin

COPY ./pom.xml /app
COPY ./msvc-admin/.mvn ./.mvn
COPY ./msvc-admin/mvnw .
COPY ./msvc-admin/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-admin/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-admin/target/msvc-admin-2.6.4.jar .

EXPOSE 8084

CMD ["java", "-jar", "msvc-admin-2.6.4.jar"]