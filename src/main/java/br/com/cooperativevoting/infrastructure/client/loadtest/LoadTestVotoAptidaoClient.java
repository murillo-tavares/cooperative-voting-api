package br.com.cooperativevoting.infrastructure.client.loadtest;

import br.com.cooperativevoting.domain.client.VotoAptidaoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Substituto de {@link VotoAptidaoClient} pro profile "loadtest": nunca chama o random.org,
 * só espera um delay aleatório (configurável via {@code loadtest.voto-aptidao.delay-millis-*})
 * pra simular a latência de uma chamada externa real sob carga. Sempre retorna apto, pra não
 * misturar 403 de "não apto" com o que o teste de carga está de fato medindo.
 */
@Component
@Profile("loadtest")
class LoadTestVotoAptidaoClient implements VotoAptidaoClient {

    private final int delayMillisMin;
    private final int delayMillisMax;

    LoadTestVotoAptidaoClient(
            @Value("${loadtest.voto-aptidao.delay-millis-min:50}") int delayMillisMin,
            @Value("${loadtest.voto-aptidao.delay-millis-max:300}") int delayMillisMax) {
        this.delayMillisMin = delayMillisMin;
        this.delayMillisMax = delayMillisMax;
    }

    @Override
    public boolean podeVotar(String cpf) {
        int delayMillis = ThreadLocalRandom.current().nextInt(delayMillisMin, delayMillisMax + 1);

        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        return true;
    }
}
