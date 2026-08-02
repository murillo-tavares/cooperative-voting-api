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

## 📖 Documentação

A spec fica em <a href="src/main/resources/static/openapi.yaml" target="_blank" rel="noopener noreferrer">openapi.yaml</a> ↗, um arquivo próprio em vez de gerada por
anotação no controller. **Decisão:** controller fica limpo, e a doc é livre 
pra customizar sem mexer em Java.

- 🌀 Scalar: <a href="https://cooperative-voting-api.onrender.com/api/v1/scalar.html" target="_blank" rel="noopener noreferrer">https://cooperative-voting-api.onrender.com/api/v1/scalar.html</a> ↗
- 🧭 Swagger UI (alternativa): <a href="https://cooperative-voting-api.onrender.com/api/v1/swagger-ui/index.html" target="_blank" rel="noopener noreferrer">https://cooperative-voting-api.onrender.com/api/v1/swagger-ui/index.html</a> ↗
- 📄 Spec: <a href="https://cooperative-voting-api.onrender.com/api/v1/openapi.yaml" target="_blank" rel="noopener noreferrer">https://cooperative-voting-api.onrender.com/api/v1/openapi.yaml</a> ↗

> ⚠️ Hospedado no plano free do Render: a instância dorme por inatividade e o primeiro acesso após um tempo sem
> uso pode demorar cerca de 1 minuto pra subir. Além disso, por se tratar de um ambiente gratuito, é normal haver 
> uma latência um pouco maior nas requisições em comparação com serviços em produção.

<details>
<summary>Local (localhost:8080)</summary>

- 🌀 Scalar: <a href="http://localhost:8080/api/v1/scalar.html" target="_blank" rel="noopener noreferrer">http://localhost:8080/api/v1/scalar.html</a> ↗
- 🧭 Swagger UI (alternativa): <a href="http://localhost:8080/api/v1/swagger-ui/index.html" target="_blank" rel="noopener noreferrer">http://localhost:8080/api/v1/swagger-ui/index.html</a> ↗
- 📄 Spec: <a href="http://localhost:8080/api/v1/openapi.yaml" target="_blank" rel="noopener noreferrer">http://localhost:8080/api/v1/openapi.yaml</a> ↗

</details>

### 📮 Postman

- <a href="docs/postman/cooperative-voting-api.postman_collection.json" target="_blank" rel="noopener noreferrer">cooperative-voting-api.postman_collection.json</a> ↗
- <a href="docs/postman/cooperative-voting-api.local.postman_environment.json" target="_blank" rel="noopener noreferrer">cooperative-voting-api.local.postman_environment.json</a> ↗
- <a href="docs/postman/cooperative-voting-api.render.postman_environment.json" target="_blank" rel="noopener noreferrer">cooperative-voting-api.render.postman_environment.json</a> ↗

Mais detalhes (import, troca de ambiente) em <a href="docs/postman/README.md" target="_blank" rel="noopener noreferrer">docs/postman/README.md</a> ↗.

## 🔌 Integração com sistemas externos ![Tarefa Bônus 1 - Integração com sistemas externos](https://img.shields.io/badge/Tarefa%20B%C3%B4nus%201-Integra%C3%A7%C3%A3o%20com%20sistemas%20externos-orange)

O desafio pede integração com `user-info.herokuapp.com`, mas o serviço está fora do ar.
Uso o **random.org como substituto** com mesmo papel na aplicação: aprova ou recusa o
voto de forma aleatória por chamada externa.

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

### 🔥 Teste de carga ![Tarefa Bônus 2 - Performance](https://img.shields.io/badge/Tarefa%20B%C3%B4nus%202-Performance-orange)

<a href="src/test/java/br/com/cooperativevoting/loadtest/VotoSimulation.java" target="_blank" rel="noopener noreferrer">VotoSimulation</a> ↗ (Gatling) sobe uma pauta,
abre sessão e simula 200 usuários votando ao mesmo tempo. Precisa da **API rodando** com o profile `loadtest`
ativo (cliente de aptidão fake, não depende do random.org):

```bash
SPRING_PROFILES_ACTIVE=loadtest docker compose up -d --build app
./mvnw gatling:test -DbaseUrl=http://localhost:8080/api/v1
```

> ⚠️ A integração externa (random.org) é o maior gargalo de latência do fluxo de voto: média de ~387ms por
> chamada, chegando a ~690ms. Por isso o teste de carga usa o cliente fake em vez do random.org de verdade,
> senão o resultado mediria a latência do random.org, não da aplicação.

## 🏷️ Versionamento ![Tarefa Bônus 3 - Versionamento da API](https://img.shields.io/badge/Tarefa%20B%C3%B4nus%203-Versionamento%20da%20API-orange)

API versionada por path (`/api/v1`).

Releases seguem <a href="https://keepachangelog.com/pt-BR/1.1.0/" target="_blank" rel="noopener noreferrer">Keep a Changelog</a> ↗ e
<a href="https://semver.org/lang/pt-BR/" target="_blank" rel="noopener noreferrer">Semantic Versioning</a> ↗, com tag `vX.Y.Z` no repositório.

- 📝 Changelog: <a href="CHANGELOG.md" target="_blank" rel="noopener noreferrer">CHANGELOG.md</a> ↗
- 🚀 Releases: <a href="https://github.com/murillo-tavares/cooperative-voting-api/releases" target="_blank" rel="noopener noreferrer">github.com/murillo-tavares/cooperative-voting-api/releases</a> ↗

## 🏗️ Arquitetura

### 🔄 DTOs + MapStruct

Mapeamento entre <a href="src/main/java/br/com/cooperativevoting/api/dto" target="_blank" rel="noopener noreferrer">DTO</a> ↗ e
<a href="src/main/java/br/com/cooperativevoting/domain/model" target="_blank" rel="noopener noreferrer">domínio</a> ↗ é gerado em build time pelo
<a href="src/main/java/br/com/cooperativevoting/api/mapper" target="_blank" rel="noopener noreferrer">MapStruct</a> ↗. Sem conversão manual pra escrever ou manter:
mudar um campo é só mexer na interface do mapper.

No controller o DTO nunca escapa da camada web. Chega como JSON, o mapper converte pra entidade de domínio antes
do service; na volta, converte de novo antes de virar JSON.

<img src="docs/diagrams/mapstruct-fluxo.svg" alt="Diagrama de sequência: Client -> Controller -> Mapper -> Service e volta" width="820">

### 🚨 Tratamento de erros

Cada erro de negócio tem sua própria <a href="src/main/java/br/com/cooperativevoting/domain/exception" target="_blank" rel="noopener noreferrer">exception</a> ↗, com
status, mensagem e código únicos. O
<a href="src/main/java/br/com/cooperativevoting/api/exception/GlobalExceptionHandler.java" target="_blank" rel="noopener noreferrer">GlobalExceptionHandler</a> ↗
captura tudo via Zalando Problem: um catálogo autodocumentado e testável pelo código, sem depender de texto solto.

Violação de constraint do banco (ex.: sessão duplicada) usa **strategy + map**. O `INSERT` é otimista (um
`SELECT` prévio não seguraria concorrência); se a constraint falhar, o
<a href="src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationTranslator.java" target="_blank" rel="noopener noreferrer">ConstraintViolationTranslator</a> ↗
busca no `Map<constraintName, Mapper>` qual
<a href="src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationMapper.java" target="_blank" rel="noopener noreferrer">ConstraintViolationMapper</a> ↗
sabe traduzir. Nova constraint é só nova implementação, sem tocar no tradutor.

<img src="docs/diagrams/erros-fluxo.svg" alt="Diagrama de classes: strategy pattern do tratamento de constraint violation" width="820">

### 🧩 Filtro + Specification

<a href="src/main/java/br/com/cooperativevoting/domain/filter" target="_blank" rel="noopener noreferrer">Filtros</a> ↗ usam `Specification` do Spring Data em vez de
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
