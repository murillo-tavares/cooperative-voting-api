package br.com.cooperativevoting.infrastructure.client.randomorg;

import br.com.cooperativevoting.domain.client.VotoAptidaoClient;
import br.com.cooperativevoting.domain.exception.client.VotoAptidaoIndisponivelException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/**
 * Implementação provisória de {@link VotoAptidaoClient} sobre a API pública do random.org.
 * <p>
 * O serviço originalmente previsto para esta consulta (que verificaria o CPF de fato) está
 * fora do ar; o random.org só devolve um bit aleatório, sem nenhuma relação com o CPF
 * informado, por isso o parâmetro é ignorado aqui.
 * <p>
 * Desligado no profile {@code loadtest} — ver
 * {@link br.com.cooperativevoting.infrastructure.client.loadtest.LoadTestVotoAptidaoClient}.
 */
@Component
@Profile("!loadtest")
@RequiredArgsConstructor
class RandomOrgVotoAptidaoClient implements VotoAptidaoClient {

    private final RandomOrgIntegerApi api;

    @Override
    public boolean podeVotar(String cpf) {
        String resposta;
        try {
            resposta = api.buscarBit();
        } catch (RestClientException exception) {
            throw VotoAptidaoIndisponivelException.falhaNaConsulta(exception);
        }

        String bit = resposta == null ? "" : resposta.trim();
        return switch (bit) {
            case "1" -> true;
            case "0" -> false;
            default -> throw VotoAptidaoIndisponivelException.respostaInesperada(resposta);
        };
    }
}
