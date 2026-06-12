# Arquitetura

## Organização

Quatro camadas (`api`, `application`, `domain`, `infrastructure`). O cálculo pesado — agrupar por
produtor e achar os intervalos entre vitórias — fica numa query SQL com window function. A classe de
domínio `AwardIntervalCalculator` é Java puro e cuida só da seleção do menor e do maior intervalo
(com empates). Controller e service ficam "burros" (só entrada/saída e orquestração).

O acesso a dados fica atrás da interface `MovieRepository` (no domínio); na infraestrutura, o
`MovieRepositoryAdapter` implementa essa porta delegando ao Spring Data (`JpaMovieRepository`). Para
um projeto deste tamanho é inversão de dependência suficiente — não fui atrás de hexagonal completo,
seria over-engineering.

## A regra de negócio

O grosso do trabalho está numa query SQL com window function: particiono por produtor, ordeno por
ano e o `LAG` me devolve o ano da vitória anterior — a diferença é o intervalo. Em uma passada no
banco eu resolvo o agrupamento e todos os intervalos consecutivos, em vez de encadear vários loops
em Java (e é justamente o tipo de SQL que a vaga pede). Só entram produtores com duas vitórias ou
mais, o que cai naturalmente do `WHERE previous_win IS NOT NULL`. O Java recebe essa lista de
intervalos e escolhe o menor e o maior global (com empates); se a lista vier vazia, `min` e `max`
voltam vazios.

O enunciado avisa que vão usar outros datasets, então três decisões não são óbvias e mudam o
resultado dependendo dos dados:

- **Um produtor pode aparecer mais de uma vez.** Cada item da resposta é um par
  (vitória anterior → vitória seguinte). Se alguém tem três vitórias e dois pares empatam no extremo,
  os dois aparecem — preferi isso a "achatar" por produtor e perder os anos corretos.
- **Não deduplico anos.** Se o mesmo produtor vence dois filmes no mesmo ano, conta como intervalo 0.
  É a leitura literal de "dois prêmios".
- **Comparo produtores por nome exato** (depois de `trim`). "Michael DeLuca" e "Michael De Luca" são
  tratados como pessoas diferentes; não entrei em fuzzy matching, seria chute.

Os empates saem ordenados por ano da vitória anterior (e por nome, no desempate) só para a resposta
ser determinística e os testes não dependerem da ordem que o banco devolve.

## Carga dos dados

O CSV vem embarcado e é lido no startup por um `ApplicationRunner`, direto para o H2 em memória.
Linha quebrada é ignorada com um WARN em vez de derrubar a aplicação — prefiro subir com o que é
válido. O caminho do arquivo é uma property (`app.csv.path`), então dá para apontar outro CSV sem
recompilar. O parser separa os produtores por vírgula e por " and " na mesma regex, porque o campo
mistura os dois ("A, B and C").

Esse split na carga é o que viabiliza o SQL: cada produtor vira uma linha em `movie_producers`, então
a window function consegue particionar por produtor e olhar a vitória anterior. Sem normalizar isso,
o `LAG` não teria como agrupar.

## Testes

Só de integração, como o enunciado pede. Cada teste sobe o contexto, carrega um CSV e bate na API de
verdade. São três cenários: o dataset oficial (resultado conhecido), empates (vários em `min` e `max`)
e o caso vazio. Como a carga acontece no startup, cada dataset roda em seu próprio contexto — por isso
não multipliquei cenários além do necessário. Em teste, cada contexto usa um H2 com nome único
(`mem:test-<uuid>`) para um dataset não vazar no outro.
