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

Це підніме PostgreSQL на `localhost:5433` з базою `product_catalog`,
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

### Лабораторна: ролі та обмеження доступу до REST API

API `/api/products` використовує HTTP Basic і трьох навчальних користувачів:

| Користувач | Пароль | Роль | Дозволені дії |
| --- | --- | --- | --- |
| `viewer` | `viewer123` | `VIEWER` | GET: список і товар за id |
| `editor` | `editor123` | `EDITOR` | GET, POST, PUT |
| `admin` | `admin123` | `ADMIN` | GET, POST, PUT, DELETE |

Паролі можна змінити змінними середовища `VIEWER_PASSWORD`, `EDITOR_PASSWORD` та `ADMIN_PASSWORD`.
Без облікових даних або з неправильним паролем API повертає `401`; для автентифікованого користувача без потрібної ролі — `403`.
Десять послідовних запитів із перевірками статусів є в [`http/roles.http`](http/roles.http).
Запит №6 створює товар і зберігає його `id` для запитів №7–10; №10 видаляє цей товар.
Запускайте файл після старту PostgreSQL і Spring Boot у HTTP Client IntelliJ IDEA.

### Автоматичні тести

```bash
mvn test
```

Юніт-тести покривають сервісний шар (`ProductServiceTest`) із мок-репозиторієм.
