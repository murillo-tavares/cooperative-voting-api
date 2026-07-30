package br.com.cooperativevoting.support.suite;

import org.junit.jupiter.api.Tag;

/**
 * Marca um teste unitário puro (sem contexto Spring, sem containers).
 * Contraponto de {@link IntegrationTest}: permite rodar só a suíte rápida com
 * {@code mvn test} (que já exclui a suíte de integração) ou filtrar explicitamente por tag.
 */
@Tag("unit")
public interface UnitTest {
}
