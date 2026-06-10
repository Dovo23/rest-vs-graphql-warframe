# rest-vs-graphql-warframe

Spring Boot test application for comparing REST and GraphQL using Warframe fissure data.

## Project Goal

This project explores how REST and GraphQL affect performance and development effort when used for the same application requirements. The application exposes the same data through both API styles and allows a direct comparison in the frontend.

## Planned Features

- Fetch current fissure data from the Warframe Worldstate API
- REST API for list, filter and detail views
- GraphQL API with equivalent queries
- Shared service layer for both API variants
- Frontend with a switch between REST and GraphQL
- Measurement of response time, payload size and request count

## Technology Stack

- Java
- Spring Boot
- Spring Web
- Spring for GraphQL
- HTML, CSS and JavaScript

## Local Setup

Requirements:

- Java 21
- Maven

Run tests:

```bash
mvn test
```

Start the application:

```bash
mvn spring-boot:run
```

Health check:

```text
GET http://localhost:8081/api/health
```

## Status

The project is currently in the initialization phase.
