FROM openjdk:17.0.2 as builder

WORKDIR /app/CARRITOADMIN

COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .

# Convertir finales de línea y hacer ejecutable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Crear manualmente el archivo maven-wrapper.properties con el contenido correcto
RUN echo "wrapperVersion=3.3.1" > .mvn/wrapper/maven-wrapper.properties && \
    echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip" >> .mvn/wrapper/maven-wrapper.properties

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/ || true

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/CARRITOADMIN/target/CARRITOADMIN-0.0.1-SNAPSHOT.jar .
COPY dialogflow-key.json /dialogflow-key.json 


EXPOSE 8081

CMD ["java", "-jar", "CARRITOADMIN-0.0.1-SNAPSHOT.jar"]