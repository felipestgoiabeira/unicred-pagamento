Unicred Pagamento
==========

Este projeto contém às API's para realização de operações Associads(CRUD) e operações sobre boletos.
 
Para executar o projeto é necessário ter o Docker e Docker Compose instalados.

Para rodar o projeto execute:
```
  docker-compose -f .\docker\docker-compose.yml up --build
```

O Docker Compose deste projeto possui os seguintes serviços:
* API Associados
* Banco Postgres Associados
* API Boletos
* Banco Postgres Boletos
* Zookeeper
* Kafka

Swagger
-------------

O Swagger das aplicações foi desenvolvido de objetivo de permitir que todas 
que todas os requisitos sejam testados por eles. Por favor, fique atento ao resumo e descrição dos endpoints.

Swagger API Associados 
-------------

Para acessar acessar o Swagger da API de Associados acesse o link:

```
http://localhost:8080/api/swagger-ui/index.html
```

Swagger API Boletos
-------------

Para acessar acessar o Swagger da API de Associados acesse o link:

```
http://localhost:8081/api/swagger-ui/index.html
```