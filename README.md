# Order Application based on microservice architecture and built with Spring Cloud Framework

It was created two version of application:
- Docker Compose based (Branch "Main")
- Kubernetes based (Branch "k8s")

# Docker Compose based application

Two versions of Docker Compose based application:
- With Discovery: The application uses Spring Cloud Netflix Eureka Discovery;
- Without Discovery: The application uses Docker Discovery (Spring Cloud LoadBalance is not supported here);

# Technologies
Development: Java 11, Jakarta EE (JPA, Bean Validation API), Spring Framework (Spring Core, Spring MVC, Spring WebFlux, Spring GraphQL, Spring Boot, Spring Data JPA, Spring Data Mongo, Spring Data ElasticSearch, Spring Batch, Spring Kafka, Spring Security, Spring Cloud Eureka, Spring Cloud Client LoadBalancer, Spring Cloud CircuitBreaker Resilience4j, Spring Cloud Configuration Server, Spring Cloud API Gateway, Spring Cloud Bus, Spring Cloud Sleuth), Apache Kafka;

Database: MySql, PostgreSQL, MongoDB, ElasticSearch;

Build tools: Gradle, Docker, Docker Compose, Bash;

Security: OAuth 2.0 and OpenID via Spring Security OAuth and based on Keycloak Authorization Server;

Other: Git, Rest API, GraphQL API, ELK (ElasticSearch, Logstash, Kibana), Zipkin, SAGA, Event-driven;

# Diagram


# Usage

1) Go to scripts folder: from the project root "cd .\scripts";
2) Build docker images of services: run ".\build-project-images-layered.sh";
3) Go to docker compose folder:
   1) for Application With Discovery: from the project root "cd .\docker\compose\with-discovery";
   2) for Application Without Discovery: from the project root "cd .\docker\compose\without-discovery";
4) Start application via Docker Compose: "docker compose -d up"

## For feedback
**e-mail:** artsiombarodka@gmail.com      
**telegram:** @ArtiomBar