# Product Catalog

Просте веб-застосування на Spring Boot для управління каталогом товарів (CRUD):
перегляд списку, пошук, додавання, редагування та видалення товарів.
Дані зберігаються в PostgreSQL, інтерфейс — server-side рендеринг на Thymeleaf.

## Стек технологій

- Java 17
- Spring Boot 3.3 (Web, Data JPA, Thymeleaf, Validation)
- PostgreSQL 16
- Maven
- Lombok

## Запуск

### 1. Підняти базу даних PostgreSQL

Найпростіше — через Docker Compose (файл `docker-compose.yml` вже в корені проєкту):

```bash
docker compose up -d
```

Це підніме PostgreSQL на `localhost:5432` з базою `product_catalog`,
користувачем `postgres` і паролем `postgres`.

Якщо своя інсталяція PostgreSQL — просто створіть базу вручну:

```sql
CREATE DATABASE product_catalog;
```

і за потреби перевизначте підключення змінними середовища
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (див. `application.properties`).

### 2. Запустити застосунок

```bash
mvn spring-boot:run
```

Таблиця `products` створюється автоматично (Hibernate `ddl-auto=update`)
при першому старті.

### 3. Відкрити в браузері

http://localhost:8080 — перенаправить на `/products`.

## Тести

```bash
mvn test
```

Юніт-тести покривають сервісний шар (`ProductServiceTest`) із мок-репозиторієм.