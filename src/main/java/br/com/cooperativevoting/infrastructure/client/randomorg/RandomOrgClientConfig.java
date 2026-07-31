package br.com.cooperativevoting.infrastructure.client.randomorg;

import br.com.cooperativevoting.properties.RandomOrgProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * Monta o proxy de {@link RandomOrgIntegerApi} sobre um {@link RestClient} próprio,
 * com timeout de conexão/leitura curtos.
 */
@Configuration
class RandomOrgClientConfig {

    @Bean
    RandomOrgIntegerApi randomOrgIntegerApi(RandomOrgProperties properties) {
        var settings = HttpClientSettings.defaults()
                .withTimeouts(
                        Duration.ofMillis(properties.connectTimeoutMillis()),
                        Duration.ofMillis(properties.readTimeoutMillis()));

        var restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(ClientHttpRequestFactoryBuilder.simple().build(settings))
                .build();

        var proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(RandomOrgIntegerApi.class);
    }
}
