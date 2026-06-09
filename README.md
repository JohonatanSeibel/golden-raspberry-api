# Golden Raspberry Awards API

API REST (somente leitura) sobre a lista de indicados e vencedores do **Pior Filme** do Golden
Raspberry Awards. O foco é um cálculo: entre os produtores que já venceram, quem teve o **maior** e
o **menor** intervalo entre duas vitórias consecutivas.

Os dados vêm de um CSV embarcado, carregado num H2 em memória no startup — não precisa instalar
banco, subir arquivo nem apontar caminho. Sobe e responde.

## Endpoint

```
GET /api/producers/award-intervals
```

```json
{
  "min": [ { "producer": "Joel Silver", "interval": 1, "previousWin": 1990, "followingWin": 1991 } ],
  "max": [ { "producer": "Matthew Vaughn", "interval": 13, "previousWin": 2002, "followingWin": 2015 } ]
}
```

`min` e `max` trazem todos os produtores empatados no intervalo extremo, ordenados por `previousWin`
(e por nome, no desempate). Se ninguém venceu duas vezes, voltam vazios.

## Como rodar

Local (Java 17+ e Maven):

```bash
mvn spring-boot:run
```

Container:

```bash
docker compose up --build
```

Nos dois casos a API sobe em `http://localhost:8080` e o Swagger fica em
`http://localhost:8080/swagger-ui.html`.

## Testes

```bash
mvn test
```

Apenas testes de integração, como o enunciado pede: sobem a aplicação, carregam o CSV e validam a
resposta real da API — o dataset oficial, um caso de empate e o caso vazio.

## Decisões que valem comentar

A vaga é Java/EJB/JPA. Fui de **Spring Boot 3 + Java 17**, com este projeto pretendo demonstrar uma pouco mais
como trabalho: JPA/Hibernate, SQL, REST, entrega web, persistência, H2 e testes num pacote único que sobe com um
comando. Trazer um container EJB para um teste isolado seria demais, mas posso refatorar o projeto se for o caso.

O resto é o básico bem-feito: camadas separadas
(`api → application → domain ← infrastructure`), a regra de negócio numa classe pura e testável
(`AwardIntervalCalculator`), configuração por variável de ambiente (nada hardcoded) e o cálculo
pensado para acertar em qualquer dataset, não só no de exemplo.

As demais decisões de negócio e arquitetura estão em [docs/ARQUITETURA.md](docs/ARQUITETURA.md).

## Stack

Java 17 · Spring Boot 3.5 · Spring Data JPA / Hibernate · H2 · springdoc (Swagger) · JUnit 5 ·
Maven · Docker.
