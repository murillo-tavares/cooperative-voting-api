# 🗳️ Cooperative Voting API

API de votação cooperativa — cadastro de pautas, sessões de votação e apuração de resultado.

<p>
  <img src="https://img.shields.io/badge/Java-21-e76f00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6db33f?logo=springboot&logoColor=white" alt="Spring Boot 4.1">
  <img src="https://img.shields.io/badge/PostgreSQL-4169e1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Kafka-231f20?logo=apachekafka&logoColor=white" alt="Kafka">
  <img src="https://img.shields.io/badge/Testcontainers-2496ed?logo=docker&logoColor=white" alt="Testcontainers">
</p>

## 📖 Documentação

A spec fica em um arquivo próprio — [openapi.yaml](src/main/resources/static/openapi.yaml) ↗ — em vez de gerada a
partir de anotação em controller. Decisão consciente: mantém o código do controller limpo, sem anotação de
documentação misturada com lógica de negócio, e deixa a descrição dos endpoints (texto, exemplos, formatação)
livre pra ser customizada sem precisar tocar em código Java.

- 🧭 Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) ↗
- 🌀 Scalar (alternativa ao Swagger, mesma spec): [http://localhost:8080/scalar.html](http://localhost:8080/scalar.html) ↗
- 📄 Spec crua: [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml) ↗

## 🏷️ Versionamento

Cada versão publicada segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) ↗ e
[Semantic Versioning](https://semver.org/lang/pt-BR/) ↗, com uma tag `vX.Y.Z` correspondente no repositório.

- 📝 Changelog: [CHANGELOG.md](CHANGELOG.md) ↗
- 🚀 Releases: [github.com/murillo-tavares/cooperative-voting-api/releases](https://github.com/murillo-tavares/cooperative-voting-api/releases) ↗

## 🏗️ Arquitetura

### 🔄 DTOs + MapStruct

Mapeamento entre [DTO](src/main/java/br/com/cooperativevoting/api/dto) ↗ e
[domínio](src/main/java/br/com/cooperativevoting/domain/model) ↗ é gerado em build time pelo
[MapStruct](src/main/java/br/com/cooperativevoting/api/mapper) ↗ — sem código manual de conversão pra escrever ou
manter; atualizar um campo é só mexer na interface do mapper.

No controller, o DTO nunca escapa da camada web: chega como JSON, o mapper converte pra entidade de domínio antes
de chegar no service; na volta, o mapper converte a entidade de volta pra DTO antes de virar JSON de novo.

<img src="docs/diagrams/mapstruct-fluxo.svg" alt="Diagrama de sequência: Client -> Controller -> Mapper -> Service e volta" width="820">

### 🚨 Tratamento de erros

Cada erro de negócio tem sua própria [exception](src/main/java/br/com/cooperativevoting/domain/exception) ↗, com
status, mensagem e código únicos, capturada globalmente pelo
[GlobalExceptionHandler](src/main/java/br/com/cooperativevoting/api/exception/GlobalExceptionHandler.java) ↗ via
Zalando Problem — catálogo autodocumentado e testável pelo código, sem depender de texto de mensagem.

Violação de constraint do banco (ex.: sessão duplicada) segue **strategy + map**: o `INSERT` é otimista — um
`SELECT` prévio não seguraria concorrência —, e se a constraint falhar, o
[ConstraintViolationTranslator](src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationTranslator.java) ↗
busca num `Map<constraintName, Mapper>` qual
[ConstraintViolationMapper](src/main/java/br/com/cooperativevoting/domain/exception/constraint/ConstraintViolationMapper.java) ↗
sabe traduzi-la. Nova constraint = nova implementação, sem tocar no tradutor.

<img src="docs/diagrams/erros-fluxo.svg" alt="Diagrama de classes: strategy pattern do tratamento de constraint violation" width="820">

### 🧩 Filtro + Specification

[Filtros](src/main/java/br/com/cooperativevoting/domain/filter) ↗ usam o framework `Specification` do Spring Data
em vez de query fixa: cada campo filtrável é um critério isolado e reaproveitável em `PautaSpecifications`, e o
`SpecificationBuilder` combina só os critérios presentes na requisição num único `AND`. Estender é fácil — um
filtro novo é só mais um critério, sem afetar os existentes nem exigir um método por combinação.

Adicionar um filtro é sempre três passos: um campo no record, um método novo em `PautaSpecifications` e um
`.addIfPresent(...)` novo em `toSpecification()` — nada existente muda.

<img src="docs/diagrams/specification-fluxo.svg" alt="Critérios de PautaSpecifications plugados na chain do SpecificationBuilder" width="900">

### 🪪 Entidade

#### `codigo` — identificador público

Entidade expõe um `codigo` gerado como identificador público; o `id` técnico do banco fica interno. Desacopla a
API da chave primária — o formato do código muda sem tocar em schema/chave estrangeira — e, por não ser
sequencial, dificulta adivinhar registro de terceiro.

#### `data_exclusao` — exclusão lógica

Exclusão é lógica: um `UPDATE` que marca `data_exclusao`, não um `DELETE`. Mantém histórico pra auditoria e
permite recuperação.

## 🧪 Testes

Suíte dividida entre testes unitários e de integração, marcados respectivamente pelas interfaces
[UnitTest](src/test/java/br/com/cooperativevoting/support/suite/UnitTest.java) ↗ (`@Tag("unit")`) e
[IntegrationTest](src/test/java/br/com/cooperativevoting/support/suite/IntegrationTest.java) ↗ (`@Tag("integration")`).
Integração sobe banco e Kafka reais via Testcontainers — precisa Docker rodando.

```bash
./mvnw test  # unitário: rápido, sem infra
```

```bash
./mvnw test -Dsurefire.excludedGroups= -Dsurefire.groups=integration  # integração: banco/Kafka via Testcontainers (Docker)
```
