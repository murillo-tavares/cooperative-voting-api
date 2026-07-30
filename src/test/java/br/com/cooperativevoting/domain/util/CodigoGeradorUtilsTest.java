package br.com.cooperativevoting.domain.util;

import br.com.cooperativevoting.support.suite.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CodigoGeradorUtilsTest implements UnitTest {

    @Test
    void deveGerarCodigoComPrefixoInformado() {
        String codigo = CodigoGeradorUtils.gerar("pt_");

        assertThat(codigo).startsWith("pt_");
    }

    @Test
    void deveGerarCodigoComOitoCaracteresAposOPrefixo() {
        String codigo = CodigoGeradorUtils.gerar("pt_");

        assertThat(codigo).matches("^pt_[0-9a-f]{8}$");
    }

    @Test
    void deveGerarCodigoSemPrefixoQuandoPrefixoForNulo() {
        String codigo = CodigoGeradorUtils.gerar(null);

        assertThat(codigo).matches("^[0-9a-f]{8}$");
    }

    @Test
    void deveGerarCodigosDiferentesACadaChamada() {
        Stream<String> codigos = IntStream.range(0, 100)
                .mapToObj(indice -> CodigoGeradorUtils.gerar("pt_"));

        assertThat(codigos).doesNotHaveDuplicates();
    }
}
