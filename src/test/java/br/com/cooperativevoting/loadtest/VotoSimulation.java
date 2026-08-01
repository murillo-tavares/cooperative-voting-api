package br.com.cooperativevoting.loadtest;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * Carga concorrente em POST /pautas/{id}/votos: todos os usuários virtuais votam na mesma
 * pauta/sessão, cada um com um associadoId diferente.
 * <p>
 * Precisa da API rodando com o profile "loadtest" ativo (cliente de aptidão fake, sem
 * chamar o random.org de verdade) — ver {@code docker-compose.yml}.
 * Roda via {@code mvn gatling:test -DbaseUrl=http://localhost:8080/api/v1}.
 */
public class VotoSimulation extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080/api/v1");
    private static final VotoLoadTestSetup SETUP = new VotoLoadTestSetup(BASE_URL);
    private static final String PAUTA_ID = SETUP.criarPauta();

    static {
        SETUP.abrirSessao(PAUTA_ID, 600);
    }

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private final ScenarioBuilder votar = scenario("Votar")
            .feed(SETUP.associados())
            .exec(
                    http("POST /pautas/{pautaId}/votos")
                            .post("/pautas/" + PAUTA_ID + "/votos")
                            .body(StringBody("{\"associadoId\":\"#{associadoId}\",\"opcao\":\"SIM\"}"))
                            .check(status().is(201))
            );

    {
        setUp(
                votar.injectOpen(rampUsers(200).during(Duration.ofSeconds(30)))
        ).protocols(httpProtocol);
    }
}
