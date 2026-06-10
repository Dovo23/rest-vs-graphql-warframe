# rest-vs-graphql-warframe

Spring-Boot-Testanwendung zum Vergleich von REST und GraphQL anhand von Warframe-Fissure-Daten.

## Projektziel

Das Projekt untersucht, wie sich REST und GraphQL bei gleichen fachlichen Anforderungen auf Performance und Entwicklungsaufwand auswirken. Dafür stellt die Anwendung dieselben Daten über beide Schnittstellen bereit und ermöglicht einen direkten Vergleich im Frontend.

## Geplanter Funktionsumfang

- Abruf aktueller Fissure-Daten aus der Warframe Worldstate API
- REST-API für Listen-, Filter- und Detailansichten
- GraphQL-API mit gleichwertigen Abfragen
- Gemeinsame Service-Schicht für beide API-Varianten
- Frontend mit Umschalter zwischen REST und GraphQL
- Messung von Antwortzeit, Payload-Größe und Request-Anzahl

## Technologiestack

- Java
- Spring Boot
- Spring Web
- Spring for GraphQL
- HTML, CSS und JavaScript

## Status

Das Projekt befindet sich in der Planungs- und Initialisierungsphase.
