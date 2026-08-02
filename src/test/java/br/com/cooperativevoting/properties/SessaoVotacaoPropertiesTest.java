package br.com.cooperativevoting.properties;

import br.com.cooperativevoting.domain.exception.DuracaoSessaoInvalidaException;
import br.com.cooperativevoting.support.suite.UnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessaoVotacaoPropertiesTest implements UnitTest {

    private final SessaoVotacaoProperties properties = new SessaoVotacaoProperties(60, 5);

    @Test
    void naoDeveLancarQuandoDuracaoForIgualAoMinimo() {
        assertThatCode(() -> properties.requireDuracaoValida(5)).doesNotThrowAnyException();
    }

    @Test
    void naoDeveLancarQuandoDuracaoForMaiorQueOMinimo() {
        assertThatCode(() -> properties.requireDuracaoValida(60)).doesNotThrowAnyException();
    }

    @Test
    void deveLancarQuandoDuracaoForMenorQueOMinimo() {
        assertThatThrownBy(() -> properties.requireDuracaoValida(4))
                .isInstanceOfSatisfying(DuracaoSessaoInvalidaException.class,
                        ex -> assertThat(ex.getStatus().getStatusCode()).isEqualTo(400));
    }
}
