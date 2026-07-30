# 🗳️ Cooperative Voting API

API de votação cooperativa — cadastro de pautas, sessões de votação e apuração de resultado.

<p>
  <img src="https://img.shields.io/badge/Java-21-e76f00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6db33f?logo=springboot&logoColor=white" alt="Spring Boot 4.1">
  <img src="https://img.shields.io/badge/PostgreSQL-4169e1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Kafka-231f20?logo=apachekafka&logoColor=white" alt="Kafka">
  <img src="https://img.shields.io/badge/Testcontainers-2496ed?logo=docker&logoColor=white" alt="Testcontainers">
</p>

## 🧱 Stack

|                 |                            |
|-----------------|----------------------------|
| ☕ Linguagem    | Java 21                    |
| 🌱 Framework    | Spring Boot 4.1            |
| 🐘 Banco        | PostgreSQL                 |
| 📨 Mensageria   | Kafka                      |
| 🧪 Testes       | JUnit 5 + Testcontainers   |
| 🐳 Infra local  | Docker / Docker Compose    |

## 📖 Documentação

A spec OpenAPI fica num arquivo próprio, isolado do código — não é gerada a partir de anotação em controller.

- 🧭 Swagger UI: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
- 📄 Spec crua: [`http://localhost:8080/openapi.yaml`](http://localhost:8080/openapi.yaml)

## 🧠 Decisões técnicas

<table>
  <tr>
    <td width="260">
      🔄
      <b>
        <a href="src/main/java/br/com/cooperativevoting/api/dto">DTOs</a> +
        <a href="src/main/java/br/com/cooperativevoting/api/mapper">MapStruct</a>
      </b>
    </td>
    <td>
      Mapeamento gerado em build time.
      Sem código manual de conversão pra escrever ou manter —
      atualizar um campo é só mexer na interface do mapper.
    </td>
  </tr>
  <tr>
    <td>
      🪪 <b>Código como id público</b>
    </td>
    <td>
      Entidade expõe um <code>codigo</code> gerado;
      o <code>id</code> técnico do banco fica interno.
      Desacopla a API do PK — o formato do código muda sem tocar em schema/FK —
      e por não ser sequencial, dificulta adivinhar registro de terceiro.
    </td>
  </tr>
  <tr>
    <td>
      🧩
      <b>
        <a href="src/main/java/br/com/cooperativevoting/domain/specification">Specification</a> +
        <a href="src/main/java/br/com/cooperativevoting/domain/specification/SpecificationBuilder.java">SpecificationBuilder</a>
      </b>
    </td>
    <td>
      Filtros de listagem chegam ao service como uma <code>Specification</code> composta,
      não um parâmetro por filtro.
      Sem sobrecarga de método a cada filtro novo; cada filtro fica isolado e reaproveitável.
    </td>
  </tr>
  <tr>
    <td>
      🚨
      <b>
        Zalando Problem +
        <a href="src/main/java/br/com/cooperativevoting/domain/exception">exception por erro</a>
      </b>
    </td>
    <td>
      Cada erro de negócio tem sua própria exception, com status, mensagem e código únicos.
      Catálogo de erro autodocumentado, e o código dá aos testes uma forma confiável
      de validar qual erro aconteceu — sem depender de texto de mensagem.
    </td>
  </tr>
  <tr>
    <td>
      🗄️ <b>Soft delete</b>
    </td>
    <td>
      Exclusão é um <code>UPDATE</code> que marca <code>data_exclusao</code>, não um <code>DELETE</code>.
      Mantém histórico pra auditoria e permite recuperação.
    </td>
  </tr>
  <tr>
    <td>
      🧪
      <b>
        Testcontainers +
        <a href="src/test/java/br/com/cooperativevoting/support/suite">suíte dividida</a>
      </b>
    </td>
    <td>
      Unitário (rápido, sem infra) separado de integração
      (banco/Kafka reais via Testcontainers — precisa Docker rodando).
      <pre><code>./mvnw test  # unitário</code></pre>
      <pre><code>./mvnw test -Dsurefire.excludedGroups= -Dsurefire.groups=integration  # integração (Docker)</code></pre>
    </td>
  </tr>
</table>
