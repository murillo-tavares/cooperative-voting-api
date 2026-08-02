# 🗳️ Cooperative Voting API

API de votação cooperativa — cadastro de pautas, sessões de votação e apuração de resultado.

<p>
  <img src="https://img.shields.io/badge/Java-21-e76f00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6db33f?logo=springboot&logoColor=white" alt="Spring Boot 4.1">
  <img src="https://img.shields.io/badge/PostgreSQL-4169e1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Testcontainers-2496ed?logo=docker&logoColor=white" alt="Testcontainers">
</p>

## 🚀 Como executar

Sobe Postgres + API em `http://localhost:8080`:

```bash
docker compose up --build
```

Alternativa rodando a API localmente (IDE/debug), só o banco em container:

```bash
docker compose up postgres -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

> ⚠️ O desafio pede integração com `user-info.herokuapp.com` (Tarefa Bônus 1), mas esse serviço não existe mais
> (Heroku encerrou o plano free). Uso o random.org como substituto: mesmo papel na aplicação (aprova ou recusa o
> voto por chamada externa), serviço diferente.

## 📖 Documentação

A spec fica em [openapi.yaml](src/main/resources/static/openapi.yaml) ↗, um arquivo próprio em vez de gerada por
anotação no controller. **Decisão:** controller fica limpo, e a doc é livre 
pra customizar sem mexer em Java.

- 🧭 Swagger UI: [https://cooperative-voting-api.onrender.com/api/v1/swagger-ui/index.html](https://cooperative-voting-api.onrender.com/api/v1/swagger-ui/index.html) ↗
- 🌀 Scalar (alternativa): [https://cooperative-voting-api.onrender.com/api/v1/scalar.html](https://cooperative-voting-api.onrender.com/api/v1/scalar.html) ↗
- 📄 Spec: [https://cooperative-voting-api.onrender.com/api/v1/openapi.yaml](https://cooperative-voting-api.onrender.com/api/v1/openapi.yaml) ↗

> ⚠️ Hospedado no plano free do Render: a instância dorme por inatividade e o primeiro acesso após um tempo sem
> uso pode demorar cerca de 1 minuto pra subir. Além disso, por se tratar de um ambiente gratuito, é normal haver 
> uma latência um pouco maior nas requisições em comparação com serviços em produção.

<details>
<summary>Local (localhost:8080)</summary>

- 🧭 Swagger UI: [http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html) ↗
- 🌀 Scalar (alternativa): [http://localhost:8080/api/v1/scalar.html](http://localhost:8080/api/v1/scalar.html) ↗
- 📄 Spec: [http://localhost:8080/api/v1/openapi.yaml](http://localhost:8080/api/v1/openapi.yaml) ↗

</details>

### 📮 Postman

- [cooperative-voting-api.postman_collection.json](docs/postman/cooperative-voting-api.postman_collection.json) ↗
- [cooperative-voting-api.local.postman_environment.json](docs/postman/cooperative-voting-api.local.postman_environment.json) ↗
- [cooperative-voting-api.render.postman_environment.json](docs/postman/cooperative-voting-api.render.postman_environment.json) ↗

Mais detalhes (import, troca de ambiente) em [docs/postman/README.md](docs/postman/README.md) ↗.

## 🏷️ Versionamento

API versionada por path (`/api/v1`).

Releases seguem [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) ↗ e
[Semantic Versioning](https://semver.org/lang/pt-BR/) ↗, com tag `vX.Y.Z` no repositório.

- 📝 Changelog: [CHANGELOG.md](CHANGELOG.md) ↗
- 🚀 Releases: [github.com/murillo-tavares/cooperative-voting-api/releases](https://github.com/murillo-tavares/cooperative-voting-api/releases) ↗

## 🧪 Testes

### Teste unitário

Rápido, sem infra:

```bash
./mvnw test
```

### Teste de integração

Sobe banco real via Testcontainers. Precisa de **Docker rodando**:

```bash
./mvnw test -Dsurefire.excludedGroups= -Dsurefire.groups=integration
```

### 🔥 Teste de carga

[VotoSimulation](src/test/java/br/com/cooperativevoting/loadtest/VotoSimulation.java) ↗ (Gatling) sobe uma pauta,
abre sessão e simula 200 usuários votando ao mesmo tempo. Precisa da **API rodando** com o profile `loadtest`
ativo (cliente de aptidão fake, não depende do random.org):

```bash
SPRING_PROFILES_ACTIVE=loadtest docker compose up -d --build app
./mvnw gatling:test -DbaseUrl=http://localhost:8080/api/v1
```

> ⚠️ A integração externa (random.org) é o maior gargalo de latência do fluxo de voto: média de ~387ms por
> chamada, chegando a ~690ms. Por isso o teste de carga usa o cliente fake em vez do random.org de verdade,
> senão o resultado mediria a latência do random.org, não da aplicação.

## 🏗️ Arquitetura

### 🔄 DTOs + MapStruct

Mapeamento entre [DTO](src/main/java/br/com/cooperativevoting/api/dto) ↗ e
[domínio](src/main/java/br/com/cooperativevoting/domain/model) ↗ é gerado em build time pelo
[MapStruct](src/main/java/br/com/cooperativevoting/api/mapper) ↗. Sem conversão manual pra escrever ou manter:
mudar um campo é só mexer na interface do mapper.

No controller o DTO nunca escapa da camada web. Chega como JSON, o mapper converte pra entidade de domínio antes
do service; na volta, converte de novo antes de virar JSON.

<img src="docs/diagrams/mapstruct-fluxo.svg" alt="Diagrama de sequência: Client -> Controller -> Mapper -> Service e volta" width="820">

### 🚨 Tratamento de erros

Cada erro de negócio tem sua própria [exception](src/main/java/br/com/cooperativevoting/domain/exception) ↗, com
status, mensagem e código únicos. O
[GlobalExceptionHandler](src/main/java/br/com/cooperativevoting/api/exception/GlobalExceptionHandler.java) ↗
captura tudo via Zalando Problem: um catálogo autodocumentado e testável pelo código, sem depender de texto solto.

Violação de constraint do banco (ex.: sessão duplicada) usa **strategy + map**. O `INSERT` é otimista (um
`SELECT` prévio não seguraria concorrência); se a constraint falhar, o
[ConstraintViolationTranslator](src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationTranslator.java) ↗
busca no `Map<constraintName, Mapper>` qual
[ConstraintViolationMapper](src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationMapper.java) ↗
sabe traduzir. Nova constraint é só nova implementação, sem tocar no tradutor.

<img src="docs/diagrams/erros-fluxo.svg" alt="Diagrama de classes: strategy pattern do tratamento de constraint violation" width="820">

### 🧩 Filtro + Specification

[Filtros](src/main/java/br/com/cooperativevoting/domain/filter) ↗ usam `Specification` do Spring Data em vez de
query fixa. Cada campo filtrável é um critério isolado em `PautaSpecifications`, e o `SpecificationBuilder`
combina só os critérios presentes na requisição num `AND`. Um filtro novo é só mais um critério: não afeta os
existentes nem pede um método por combinação.

São sempre três passos: campo no record, método novo em `PautaSpecifications` e `.addIfPresent(...)` novo em
`toSpecification()`. Nada existente muda.

<img src="docs/diagrams/specification-fluxo.svg" alt="Critérios de PautaSpecifications plugados na chain do SpecificationBuilder" width="900">

### 🪪 Entidade

#### Exclusão lógica (`data_exclusao`)

Exclusão é lógica: um `UPDATE` que marca `data_exclusao`, não um `DELETE`. Mantém histórico pra auditoria e
permite recuperação.
