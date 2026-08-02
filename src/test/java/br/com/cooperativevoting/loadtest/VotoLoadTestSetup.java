package br.com.cooperativevoting.loadtest;

import br.com.cooperativevoting.api.dto.request.PautaRequest;
import br.com.cooperativevoting.api.dto.request.SessaoVotacaoRequest;
import br.com.cooperativevoting.api.dto.response.PautaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.datafaker.Faker;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Prepara os dados que {@link VotoSimulation} precisa antes de rodar o cenário: cria a
 * pauta, abre a sessão de votação e gera um feeder de associadoId únicos. Fica fora do
 * fluxo do Gatling (chamadas HTTP diretas, sem métricas) porque isso é setup do teste,
 * não o que a carga está de fato medindo.
 */
@RequiredArgsConstructor
class VotoLoadTestSetup {

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /** Cria uma pauta e devolve o id gerado. */
    @SneakyThrows
    String criarPauta() {
        PautaRequest pauta = new PautaRequest("Carga - " + System.currentTimeMillis(), null);
        String body = post("/pautas", pauta).body();
        return objectMapper.readValue(body, PautaResponse.class).id().toString();
    }

    /** Abre a sessão de votação da pauta com a duração informada. */
    @SneakyThrows
    void abrirSessao(String pautaId, int duracaoSegundos) {
        SessaoVotacaoRequest sessao = new SessaoVotacaoRequest(duracaoSegundos);
        post("/pautas/" + pautaId + "/sessoes", sessao);
    }

    /**
     * Feeder infinito de associadoId únicos, um por usuário virtual. Precisam ser CPFs
     * válidos: a API valida o formato ({@code @CPF} em {@code VotoRequest.associadoId}).
     * CPF gerado pelo Datafaker, sem formatação (só dígitos).
     */
    Iterator<Map<String, Object>> associados() {
        Faker faker = new Faker();
        return Stream.generate((Supplier<Map<String, Object>>) () ->
                Map.of("associadoId", faker.cpf().valid(false))
        ).iterator();
    }

    @SneakyThrows
    private HttpResponse<String> post(String path, Object body) {
        return client.send(
                HttpRequest.newBuilder(URI.create(baseUrl + path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
