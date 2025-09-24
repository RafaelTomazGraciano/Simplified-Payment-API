# 💳 Simplified Payment API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
[![Licence](https://img.shields.io/github/license/RafaelTomazGraciano/Picpay-Challenge-Solution?style=for-the-badge)](./LICENSE)

A Spring Boot REST API that implements a simplified payment system, allowing user registration and payments between users. This project is my solution to the [PicPay Backend Challenge](https://github.com/PicPay/picpay-desafio-backend).

The API handles user registration (common users and merchants), validates business rules like sufficient balance and merchant restrictions, and integrates with external services for payment authorization and notifications.

## Table of Contents

- [Installation](#installation)
- [Requirements](#requirements)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Docker](#docker)
- [Tests](#tests)
- [License](#license)

## 📥 Installation

Clone the repository:

```bash
git clone https://github.com/RafaelTomazGraciano/Simplified-Payment-API.git
cd Simplified-Payment-API
```

## ⚙️ Requirements

Before running the application, you need:

1. PostgreSQL installed and running
2. Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 🚀 Usage

You can run the application using Maven:

```bash
mvn spring-boot:run
```

The API will be available at http://localhost:8080

## 🔌 API Endpoints

The API provides the following endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /users | Create a new user |
| GET | /users | List all users |
| POST | /transactions | Create a payment transaction |

For complete API documentation and testing interface, access the Swagger UI at:
http://localhost:8080/swagger-ui.html

## 🐳 Docker

The project includes Docker support for easy deployment. To run with Docker:

```bash
docker compose up --build
```

This will start both the API and a PostgreSQL database. To stop:

```bash
docker compose down
```

## ✅ Tests

The project includes unit tests.
Run the test suite with:

```bash
mvn test
```

The project uses JUnit 5, Mockito for mocking, and H2 in-memory database for testing.

## 📄 License

This project is licensed under the Unlicense - see the [LICENSE](LICENSE) file for details.


