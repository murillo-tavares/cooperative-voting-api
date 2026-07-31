package br.com.cooperativevoting.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração do cliente HTTP para a API pública do random.org, usada hoje como
 * substituto de teste para a consulta de aptidão de voto (ver {@code RandomOrgVotoAptidaoClient}).
 * {@code baseUrl} já inclui a query string fixa (1 inteiro entre 0 e 1) porque essa é a
 * única chamada feita por este cliente.
 */
@ConfigurationProperties(prefix = "integracao.random-org")
public record RandomOrgProperties(
        @DefaultValue("https://www.random.org/integers/?num=1&min=0&max=1&col=1&base=10&format=plain&rnd=new")
        String baseUrl,
        @DefaultValue("2000") int connectTimeoutMillis,
        @DefaultValue("2000") int readTimeoutMillis
) {
}
