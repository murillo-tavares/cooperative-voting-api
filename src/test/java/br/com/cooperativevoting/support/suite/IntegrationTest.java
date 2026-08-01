package br.com.cooperativevoting.support.suite;

import br.com.cooperativevoting.TestcontainersConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base para testes de integração com contexto Spring completo e banco real via Testcontainers.
 * Cada teste roda em transação própria, revertida ao final, então nenhum dado criado num teste
 * vaza para o próximo.
 * Contraponto de {@link UnitTest}: a tag "integration" permite excluir essa suíte lenta de
 * {@code mvn test} e rodá-la à parte com {@code mvn test -Dgroups=integration}.
 */
@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
public abstract class IntegrationTest {
}
