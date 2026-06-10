# Changelog

## Unreleased

### Added
- Initial Spring Boot project setup with Maven and Java 21.
- Basic application package structure.
- Health endpoint at `/api/health`.
- Local server port configuration using `server.port=8081`.
- Basic README and `.gitignore`.
- Fissure domain model and filter object.
- Warframe fissure client with external API mapping.
- Cached fissure service with configurable TTL.
- Fissure service tests for cache behavior, lookup and filtering.

## Planned

### Added
- REST API for fissure list and detail views.
- GraphQL API with equivalent fissure queries.
- Frontend with REST/GraphQL switch.
- Measurement view for response time, payload size and request count.
