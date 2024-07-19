# Exchange Rate Service

Этот проект предоставляет API для получения и управления курсами валют. Проект использует Spring Boot и интегрирован с внешним API для получения актуальных курсов валют.

## Установка и запуск

### Сборка Docker-образа

1. Склонируйте репозиторий:
  
2. Соберите и запустите Docker-контейнеры:

docker-compose up --build

Запуск без Docker
Убедитесь, что у вас установлены Java 17 и Maven.
Склонируйте репозиторий:
Соберите проект с помощью Maven:
mvn clean package
Запустите приложение:


## Endpoints
### Получение и сохранение курсов валют

`GET /api/exchange-rates/fetch-and-store`

### Получение курсов валют с внешнего API и сохранение их в базу данных.

Параметры запроса:

date (required): Дата, для которой необходимо получить курсы валют (формат YYYY-MM-DD).

Пример запроса:
 `GET "http://localhost:8080/api/exchange-rates/fetch-and-store?date=2024-07-19"`
 
Получение курса валют по дате и коду валюты
`GET /api/exchange-rates`

Параметры запроса:
date (required): Дата, для которой необходимо получить курс валюты (формат YYYY-MM-DD).

currencyCode (required): Код валюты (например, USD).

Пример запроса:
 `GET "http://localhost:8080/api/exchange-rates?date=2024-07-19&currencyCode=USD"`
 
## Документация API
##% Документация API доступна через Swagger.

Swagger UI: `http://localhost:8080/swagger-ui.html`

Дополнительно, вы можете использовать коллекцию Postman для тестирования API. Коллекция доступна по следующей ссылке:
Postman Collection: https://www.postman.com/gold-crater-220108/workspace/exchangerate/collection/29720666-00ebc99e-b928-4e1d-920c-9c8696931b57?action=share&source=copy-link&creator=29720666





