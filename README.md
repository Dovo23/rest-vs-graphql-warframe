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

## REST API

List fissures:

```text
GET http://localhost:8081/api/rest/fissures
```

Optional query parameters:

```text
tier
missionType
enemy
activeOnly
```

Example:

```text
GET http://localhost:8081/api/rest/fissures?tier=Lith&enemy=Grineer&activeOnly=true
```

Get one fissure by ID:

```text
GET http://localhost:8081/api/rest/fissures/{id}
```

If fissure data cannot be loaded from the upstream source, the REST API returns `503 Service Unavailable`.

## GraphQL API

GraphQL endpoint:

```text
POST http://localhost:8081/graphql
```

List fissures:

```graphql
query {
  fissures {
    id
    node
    missionType
    enemy
    tier
    isStorm
    isHard
  }
}
```

List fissures with filters:

```graphql
query {
  fissures(filter: { tier: "Lith", enemy: "Grineer", activeOnly: true }) {
    id
    node
    tier
  }
}
```

Get one fissure by ID:

```graphql
query {
  fissure(id: "fissure-id") {
    id
    activation
    expiry
    node
    missionType
    enemy
    tier
    tierNum
    isStorm
    isHard
  }
}
```

## Configuration

The application uses these configuration values:

```properties
server.port=8081
warframe.api.base-url=https://api.warframestat.us/pc
warframe.cache.ttl-seconds=60
```

- `server.port` defines the local application port.
- `warframe.api.base-url` defines the Warframe API base URL used by the fissure client.
- `warframe.cache.ttl-seconds` defines how long fetched fissure data is cached by the service layer.

## Status

The project currently contains the initial Spring Boot setup, the shared fissure data service, the REST API and the GraphQL API.
