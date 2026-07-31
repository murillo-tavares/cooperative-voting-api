package br.com.cooperativevoting.api.dto.response;

import br.com.cooperativevoting.domain.model.SessaoVotacao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dados de saída de uma sessão de votação. Reaproveita {@link SessaoVotacao.Status}:
 * é só vocabulário (sem persistência/comportamento), não custa acoplar API a ele.
 */
public record SessaoVotacaoResponse(
        UUID pautaId,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        SessaoVotacao.Status status
) {
}
