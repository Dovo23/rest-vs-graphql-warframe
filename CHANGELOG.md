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
- REST response model for fissure data.
- REST endpoints for fissure list and detail views.
- REST query parameters for tier, mission type, enemy and active-only filtering.
- REST error handling for unavailable upstream fissure data.
- GraphQL schema for fissure list and detail queries.
- GraphQL query controller for fissure data.
- GraphQL tests for list, detail and filter behavior.

## Planned

### Added
- Frontend with REST/GraphQL switch.
- Measurement view for response time, payload size and request count.
