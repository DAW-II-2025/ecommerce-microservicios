FROM openjdk:17.0.2 as builder

WORKDIR /app/msvc-carrito-bot

COPY ./pom.xml /app
COPY ./msvc-carrito-bot/.mvn ./.mvn
COPY ./msvc-carrito-bot/mvnw .
COPY ./msvc-carrito-bot/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-carrito-bot/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-carrito-bot/target/msvc-carrito-bot-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

CMD ["java", "-jar", "msvc-carrito-bot-0.0.1-SNAPSHOT.jar"]