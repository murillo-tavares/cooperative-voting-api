# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.2.0] - 2026-08-01

### Added
- Validação de duração mínima na abertura de sessão de votação
  (`votacao.sessao.duracao-minima-segundos`, padrão 5s).
- Logs de aplicação: erros de negócio, votos registrados, sessões abertas, e um log por
  requisição (método, URI, status, duração).
- Teste de carga com Gatling (`VotoSimulation`), simulando votação concorrente numa pauta.
- Dockerfile e serviço da API no Docker Compose; configuração de produção
  (`application.yml`) via variáveis de ambiente.

### Changed
- Todas as rotas passam a viver sob o prefixo `/api/v1` (estratégia de versionamento da
  API). Quebra clientes que ainda chamam os caminhos antigos sem o prefixo.

### Fixed
- `openapi.yaml` servido com `Content-Type: application/octet-stream`, quebrando o
  carregamento da spec no Scalar/Swagger UI; corrigido também um erro de sintaxe YAML na
  própria spec.

[1.2.0]: https://github.com/murillo-tavares/cooperative-voting-api/releases/tag/v1.2.0

## [1.1.0] - 2026-08-01

### Added
- `POST /pautas/{id}/votos` — recebe voto (`Sim`/`Não`) de um associado numa pauta, um voto
  por associado por pauta (constraint de unicidade no banco).
- `GET /pautas/{id}/resultado` — apuração do resultado da pauta (contagem de votos e
  veredito: aprovada/reprovada/empate).
- Integração externa de aptidão de voto por CPF, isolada atrás de `VotoAptidaoClient`
  (implementação atual usa o random.org como substituto de teste, já que o serviço
  originalmente previsto para o desafio está fora do ar).

### Changed
- Identificador de pauta simplificado: id/código separados da v1.0.0 viraram um único UUID.

### Removed
- Infra do Kafka (Docker Compose e dependências) — sem uso real no projeto.

[1.1.0]: https://github.com/murillo-tavares/cooperative-voting-api/releases/tag/v1.1.0

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
