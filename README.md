# Order Application based on microservice architecture and built with Spring Cloud Framework

It was created two version of application:
- Docker Compose based (Branch "Main")
- Kubernetes based (Branch "k8s")

# Kubernetes based application

# Technologies
**Development**: Java 11, Jakarta EE (JPA, Bean Validation API), Spring Framework (Spring Core, Spring MVC, Spring WebFlux, Spring GraphQL, Spring Boot, Spring Data JPA, Spring Data Mongo, Spring Data ElasticSearch, Spring Batch, Spring Kafka, Spring Security, Spring Cloud Kubernetes, Spring Cloud Kubernetes Client LoadBalancer, Spring Cloud CircuitBreaker Resilience4j, Spring Cloud API Gateway, Spring Cloud Sleuth), Apache Kafka;

**Database**: MySql, PostgreSQL, MongoDB, ElasticSearch;

**Build tools**: Gradle, Docker, Kubernetes, Bash;

**Security**: OAuth 2.0 and OpenID via Spring Security OAuth and based on Keycloak Authorization Server;

**Other**: Git, Rest API, GraphQL API, ELK (ElasticSearch, Logstash, Kibana), Zipkin, SAGA, Event-driven;

# Diagram


# Usage

1) Go to scripts folder: from the project root "cd .\scripts";
2) Build docker images of services: run ".\build-project-images-layered.sh";
3) Start related services in Kubernetes: run ".\start-k8s-bootstrap-resources.sh";
4) Start application in Kubernetes: run ".\start-k8s-service-resources.sh"

## For feedback
**e-mail:** artsiombarodka@gmail.com      
**telegram:** @ArtiomBar

