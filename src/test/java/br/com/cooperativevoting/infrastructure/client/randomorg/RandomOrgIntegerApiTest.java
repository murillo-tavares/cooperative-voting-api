package br.com.cooperativevoting.infrastructure.client.randomorg;

import br.com.cooperativevoting.support.suite.UnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Verifica a interface {@link RandomOrgIntegerApi} montada via {@code HttpServiceProxyFactory}
 * sem bater na internet: um GET é feito na base URL configurada
 * e o corpo da resposta volta sem alterações.
 */
class RandomOrgIntegerApiTest implements UnitTest {

    private static final String BASE_URL = "https://mock.random.org/integers/";

    @Test
    void deveFazerGetNaBaseUrlERetornarCorpoComoTexto() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        MockRestServiceServer server = MockRestServiceServer.createServer(builder);
        RestClient restClient = builder.build();

        RandomOrgIntegerApi api = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(RandomOrgIntegerApi.class);

        server.expect(requestTo(BASE_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("1\n", MediaType.TEXT_PLAIN));

        assertThat(api.buscarBit()).isEqualTo("1\n");
        server.verify();
    }
}
