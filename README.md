# Aplicação PIX

Este é um guia rápido sobre como rodar a aplicação localmente.

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em seu sistema:

- Docker
- Java Development Kit (JDK)

## Passos para executar a aplicação

1. Clone este repositório em sua máquina local.

2. Abra um terminal e navegue até o diretório raiz do projeto.

3. Execute o seguinte comando para iniciar os contêineres Docker:

    ```shell
    docker build
    ```

    ```shell
    docker-compose up -d
    ```

    Isso irá baixar as imagens necessárias e iniciar os contêineres.


4. Após os contêineres estarem em execução, execute o seguinte comando para iniciar a aplicação:

    ```shell
    ./mvnw spring-boot:run
    ```

    Isso irá compilar e executar a aplicação Java.

5. Após os contêineres estarem em execução, execute o seguinte comando para popular o banco de dados:


    ```shell
    mvn liquibase:update
    ```

6. Para verificar se a aplicação está rodando, acesse em seu navegador através do seguinte endereço:

    ```
    http://localhost:8080/api/v1/health
    ```
    Na pasta resources/documentation você encontrará a collection do Postamn para testar a aplicação toda.

    Agora você pode interagir com a aplicação PIX!

