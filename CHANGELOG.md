# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2026-07-30

### Added
- CRUD de pautas (criação, busca por código, atualização e exclusão lógica).
- Listagem paginada de pautas com filtros por data de criação via `Specification`.
- Abertura de sessão de votação para uma pauta, com tratamento de concorrência
  (constraint de unicidade) para impedir mais de uma sessão por pauta.
- Tratamento global de erros com Zalando Problem e catálogo de exceptions de negócio.
- Especificação OpenAPI e Swagger UI para os endpoints de Pauta e Sessão de Votação.
- Infra local via Docker Compose (PostgreSQL, Kafka + Zookeeper).
- Suíte de testes dividida em unitário e integração (Testcontainers).

[1.0.0]: https://github.com/murillo-tavares/cooperative-voting-api/releases/tag/v1.0.0
