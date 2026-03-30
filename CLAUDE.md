# PetClinic API Gateway

## Overview
Spring Cloud Gateway that routes API requests to backend services and serves the AngularJS frontend. Part of the PetClinic multi-repo microservices demo.

## Tech Stack
- Java 17, Spring Boot 4.0.1, Spring Cloud 2025.1.0
- Spring Cloud Gateway (WebFlux)
- Resilience4j circuit breaker + retry
- Eureka client for service discovery
- Spring Cloud Config client
- AngularJS 1.8 frontend (Webjars)

## Package Structure
```
org.springframework.samples.petclinic.api
├── ApiGatewayApplication.java              # Main class
├── application/
│   ├── CustomersServiceClient.java         # WebClient → customers-service
│   └── VisitsServiceClient.java            # WebClient → visits-service
├── boundary/web/
│   ├── ApiGatewayController.java           # Aggregation endpoint
│   └── FallbackController.java             # Circuit breaker fallback
└── dto/
    ├── OwnerDetails.java
    ├── PetDetails.java
    ├── PetType.java
    ├── VisitDetails.java
    └── Visits.java
```

## Routing Rules (application.yml)
```
/api/customer/** → lb://customers-service (StripPrefix=2)
/api/vet/**      → lb://vets-service      (StripPrefix=2)
/api/visit/**    → lb://visits-service     (StripPrefix=2)
```

## Aggregation Endpoint
- `GET /api/gateway/owners/{ownerId}` — calls customers-service + visits-service, merges owner + pets + visits into a single response

## Cross-Service Clients
- `CustomersServiceClient` — calls `http://customers-service/owners/{ownerId}` via WebClient + Eureka
- `VisitsServiceClient` — calls `http://visits-service/pets/visits?petId={ids}` via WebClient + Eureka

## Frontend
AngularJS SPA served as static resources from `src/main/resources/static/`. Pages: owner list, owner details, owner form, pet form, vet list, visits, GenAI chat.

## Circuit Breaker
- Default circuit breaker on all routes with fallback to `/fallback`
- Retry filter: 1 retry on `SERVICE_UNAVAILABLE` for POST methods
- Dedicated `genaiCircuitBreaker` on the GenAI route

## Build & Run
```bash
mvn clean package -DskipTests
# Runs on port 8080
```

## Service Registration
Registers as `api-gateway` with Eureka.
