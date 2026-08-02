package br.com.cooperativevoting.api.dto.response;

import br.com.cooperativevoting.domain.model.StatusSessao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dados de saída de uma sessão de votação.
 */
public record SessaoVotacaoResponse(
        UUID pautaId,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        StatusSessao status
) {
}
