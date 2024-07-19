# Этап сборки
FROM maven:3.8.5-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и исходный код в контейнер
COPY pom.xml .
COPY src ./src

# Выполняем сборку проекта, включая тесты
RUN mvn clean package -DskipTests

# Этап запуска
FROM openjdk:17-alpine

# Путь к JAR файлу, созданному на предыдущем этапе
ARG JAR_FILE=/app/target/exchange-rate-service-0.0.1-SNAPSHOT.jar

# Копируем JAR файл в рабочую директорию контейнера
COPY --from=build ${JAR_FILE} app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
