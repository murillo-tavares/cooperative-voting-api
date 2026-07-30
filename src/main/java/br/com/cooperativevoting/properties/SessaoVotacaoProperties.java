package br.com.cooperativevoting.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração de sessão de votação. {@code duracaoPadraoSegundos} é usada quando a
 * abertura da sessão não informa uma duração explícita.
 */
@ConfigurationProperties(prefix = "votacao.sessao")
public record SessaoVotacaoProperties(
        @DefaultValue("60") int duracaoPadraoSegundos
) {
}
