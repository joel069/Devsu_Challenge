FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copia el JAR construido desde tu sistema local al contenedor
COPY target/devsu-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que tu aplicación expone servicios (por defecto, el 8080)
EXPOSE 8082

# Comando para ejecutar la aplicación Spring Boot cuando el contenedor se inicie
CMD ["java", "-jar", "app.jar"]