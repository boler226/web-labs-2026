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

### Лабораторна: доступ через анотації

Правила доступу для GET, POST, PUT і DELETE тепер задано анотаціями `@PreAuthorize` на методах `ProductRestController`; `@EnableMethodSecurity` вмикає їх перевірку. Десять сценаріїв для гостя, `viewer`, `editor` і `admin` є в [`http/method-security.http`](http/method-security.http). Запустіть їх послідовно: запит №5 зберігає id товару, а №10 видаляє його. Для Moodle зробіть скриншоти анотацій у контролері та результатів HTTP Client; посилання на коміт додайте після власного коміту.

### Лабораторна: отримання та перегляд JWT

Імпортуйте [`postman/jwt-lab.postman_collection.json`](postman/jwt-lab.postman_collection.json) у Postman і виконайте **Get JWT token** після запуску застосунку. Запит `POST /api/auth/token` використовує Basic облікові дані `admin` / `admin123` і повертає `accessToken`, `tokenType` та `expiresIn`. Альтернативний запит для IntelliJ є в [`http/jwt.http`](http/jwt.http).

Скопіюйте `accessToken` у [JWT Debugger](https://jwt.io/) і зробіть скриншот розібраних Header та Payload. Очікувані поля: `alg: HS256`, `sub: admin`, `roles: ["ADMIN"]`, `iss: product-catalog`, `iat`, `exp`. Для перевірки підпису введіть локальний навчальний секрет з `application.properties` у поле **Secret**. Робочий секрет задавайте змінною `JWT_SECRET` і не публікуйте його.

Токен видається на 1 годину для демонстрації JWT. Маршрути товарів у цій лабораторній і далі використовують HTTP Basic.

### Автоматичні тести

```bash
mvn test
```

Юніт-тести покривають сервісний шар (`ProductServiceTest`) із мок-репозиторієм.
