package br.com.cooperativevoting.properties;

import br.com.cooperativevoting.domain.exception.DuracaoSessaoInvalidaException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração de sessão de votação. {@code duracaoPadraoSegundos} é usada quando a
 * abertura da sessão não informa uma duração explícita; {@code duracaoMinimaSegundos} é o
 * piso aceito quando informa.
 */
@ConfigurationProperties(prefix = "votacao.sessao")
public record SessaoVotacaoProperties(
        @DefaultValue("60") int duracaoPadraoSegundos,
        @DefaultValue("5") int duracaoMinimaSegundos
) {

    /** Precondição pra abrir sessão com duração explícita. */
    public void requireDuracaoValida(int duracaoSegundos) {
        if (duracaoSegundos < duracaoMinimaSegundos) {
            throw DuracaoSessaoInvalidaException.minima(duracaoSegundos, duracaoMinimaSegundos);
        }
    }
}
