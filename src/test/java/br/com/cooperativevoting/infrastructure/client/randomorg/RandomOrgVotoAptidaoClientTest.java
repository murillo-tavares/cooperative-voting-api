package br.com.cooperativevoting.infrastructure.client.randomorg;

import br.com.cooperativevoting.domain.exception.client.VotoAptidaoIndisponivelException;
import br.com.cooperativevoting.support.suite.UnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.zalando.problem.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RandomOrgVotoAptidaoClientTest implements UnitTest {

    private static final String CPF_QUALQUER = "52998224725";

    @Test
    void deveRetornarAptoQuandoRandomOrgResponderUm() {
        RandomOrgVotoAptidaoClient client = new RandomOrgVotoAptidaoClient(() -> "1\n");

        assertThat(client.podeVotar(CPF_QUALQUER)).isTrue();
    }

    @Test
    void deveRetornarNaoAptoQuandoRandomOrgResponderZero() {
        RandomOrgVotoAptidaoClient client = new RandomOrgVotoAptidaoClient(() -> "0");

        assertThat(client.podeVotar(CPF_QUALQUER)).isFalse();
    }

    @Test
    void deveLancarIndisponivelComStatus503QuandoRespostaForInesperada() {
        RandomOrgVotoAptidaoClient client = new RandomOrgVotoAptidaoClient(() -> "erro");

        assertThatThrownBy(() -> client.podeVotar(CPF_QUALQUER))
                .isInstanceOfSatisfying(VotoAptidaoIndisponivelException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(Status.SERVICE_UNAVAILABLE));
    }

    @Test
    void deveLancarIndisponivelQuandoRespostaForVazia() {
        RandomOrgVotoAptidaoClient client = new RandomOrgVotoAptidaoClient(() -> "");

        assertThatThrownBy(() -> client.podeVotar(CPF_QUALQUER))
                .isInstanceOf(VotoAptidaoIndisponivelException.class);
    }

    @Test
    void deveLancarIndisponivelComDetalheDaFalhaQuandoChamadaHttpFalhar() {
        RandomOrgVotoAptidaoClient client = new RandomOrgVotoAptidaoClient(() -> {
            throw new RestClientException("timeout ao conectar");
        });

        assertThatThrownBy(() -> client.podeVotar(CPF_QUALQUER))
                .isInstanceOfSatisfying(VotoAptidaoIndisponivelException.class,
                        ex -> assertThat(ex.getDetail()).contains("timeout ao conectar"));
    }
}
