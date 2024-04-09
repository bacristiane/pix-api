# Aplicação PIX

Este é um guia rápido sobre como rodar a aplicação XYZ localmente.

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

5. Acesse a aplicação em seu navegador através do seguinte endereço:

    ```
    http://localhost:8080
    ```

    Agora você pode interagir com a aplicação PIX.


