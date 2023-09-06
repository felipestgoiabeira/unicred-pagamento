Unicred Pagamento
==========

Este projeto contém as API's para realização de operações sobre Associados(CRUD) e operações sobre Boletos.
 
Para executar o projeto é necessário ter o Docker e Docker Compose instalados.

Para rodar o projeto execute:
```
  docker-compose -f .docker/docker-compose.yml down -v && docker-compose -f .docker/docker-compose.yml up --build
```

O primeiro comando executado é para garantir que o volume com os relatórios de testes do Jacoco esteja atualizado.

O segundo comando irá contruir e subir os containers necessários para o sistema.


O Docker Compose deste projeto possui os seguintes serviços:

* API Associados
* Banco Postgres Associados
* API Boletos
* Banco Postgres Boletos
* API de Arquivos
* Zookeeper
* Kafka
* Nginx

Descrição das API's
-------------
* API Associados - CRUD para Associados e endpoints para gerar boletos.


* API Boletos - Enpoints para consultar, pagar boletos de associados e fornece um endpoint para gerar um arquivo com
os boletos aguardando pagamento do Associado.


* API de Arquivos - Fornece um endpoint para realizar o pagamento em lote de dívidas através de um arquivo.

Swagger
-------------

A definição mais detalhada das API's encontra-se no Swagger respectivo de cada aplicação. Foi desenvolvido de objetivo
de permitir que todas 
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


Swagger API de Arquivos
-------------

Para acessar acessar o Swagger da API de Associados acesse o link:

```
http://localhost:8083/api/swagger-ui/index.html
```

Relatório de Cobertura de Testes do Jacoco
-------------

Este projeto possui uma cobertura mínima de 70%. Para visualizar os relatórios de cobertura 
do Jacoco, acesse os seguintes links para cada serviço.

API de Associados
```
http://localhost/associate/
```

API de Boletos
```
http://localhost/ticket/
```

API de Arquivos
```
http://localhost/files/
```