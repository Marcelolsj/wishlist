# Antes de trabalhar/executar no projeto

* Baixar plugin do lombok no intelliJ.
* Instalar o docker e docker-compose.

## Banco de dados

Na pasta mongodb, executar o comando abaixo para criar o banco de dados.  
`sudo docker-compose up -d`

### Subir a aplicação localmente

Na pasta raiz da aplicação, executar o comando abaixo.  
`./gradlew bootRun`

### Acessando o Swagger
* http://localhost:8080/swagger-ui/index.html

### Acessando o banco de dados via browser
* http://localhost:8081