# Arquitetura

## Organização

Quatro camadas (`api`, `application`, `domain`, `infrastructure`) com uma regra simples: a lógica de
negócio mora no `domain` e não conhece o Spring. O cálculo dos intervalos (`AwardIntervalCalculator`)
é uma classe Java pura — recebe a lista de vencedores e devolve o resultado. Isso mantém controller e
repositório "burros" (só entrada/saída) e deixa a parte que realmente importa fácil de ler e de testar.

O acesso a dados fica atrás da interface `MovieRepository` (no domínio); a implementação Spring Data
(`JpaMovieRepository`) fica na infraestrutura. Para um projeto deste tamanho é inversão de dependência
suficiente — não fui atrás de hexagonal completo, seria over-engineering.

## A regra de negócio

Para cada produtor, junto os anos em que ganhou, ordeno e calculo o intervalo entre vitórias
consecutivas. `min` e `max` são o menor e o maior intervalo global. Só entram produtores com duas
vitórias ou mais; se ninguém se qualifica, as duas listas voltam vazias.

O enunciado avisa que vão usar outros datasets, então três decisões aqui não são óbvias e mudam o
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

Um ponto que vale citar: a busca de vencedores usa `join fetch` com `distinct` para trazer os
produtores numa query só e evitar a duplicação de linhas que o `@ElementCollection` causa num join.

## Testes

Só de integração, como o enunciado pede. Cada teste sobe o contexto, carrega um CSV e bate na API de
verdade. São três cenários: o dataset oficial (resultado conhecido), empates (vários em `min` e `max`)
e o caso vazio. Como a carga acontece no startup, cada dataset roda em seu próprio contexto — por isso
não multipliquei cenários além do necessário. Em teste, cada contexto usa um H2 com nome único
(`mem:test-<uuid>`) para um dataset não vazar no outro.
