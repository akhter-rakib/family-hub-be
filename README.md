# Family Hub - Backend

Spring Boot modular monolith for household management.

## Tech Stack
- Java 21, Spring Boot 3.3.5
- MySQL 8.0 (Flyway migrations)
- Redis (caching), RabbitMQ (events), MailHog (dev email)
- Spring Security + JWT, MapStruct, SpringDoc OpenAPI

## Prerequisites
- Java 21
- Docker & Docker Compose

## Quick Start

### 1. Start Infrastructure
```bash
docker-compose up -d
```

| Service            | URL                                                      |
|--------------------|----------------------------------------------------------|
| MySQL              | localhost:3306                                           |
| Redis              | localhost:6379                                           |
| RabbitMQ Mgmt      | http://localhost:15672 (familyhub / familyhub123)        |
| phpMyAdmin         | http://localhost:8081                                    |
| Redis Commander    | http://localhost:8082                                    |
| MailHog            | http://localhost:8025                                    |

### 2. Run the Application
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

- API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html

## Project Structure
```
src/main/java/com/familyhub/
├── common/        # Shared: security, config, exceptions
├── auth/          # Authentication & user management
├── family/        # Family & membership management
├── catalog/       # Units, categories, items
├── shopping/      # Shopping requests
├── purchase/      # Purchase tracking
├── bill/          # Bill management
├── gas/           # Gas usage tracking
├── inventory/     # Stock & expiry tracking
├── report/        # Reports & analytics
└── notification/  # In-app notifications
```

## Database Migrations
Flyway migrations run automatically on startup:
- `V1__core_tables.sql` — Users, families, memberships
- `V2__catalog_tables.sql` — Units, categories, items
- `V3__shopping_purchase_tables.sql` — Shopping & purchases
- `V4__remaining_tables.sql` — Bills, gas, inventory, notifications
- `V5__seed_data.sql` — Seed data
