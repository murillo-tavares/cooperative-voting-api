package br.com.cooperativevoting.api.dto.response;

import br.com.cooperativevoting.domain.model.Veredito;

import java.util.UUID;

/**
 * Dados de saída da apuração de uma pauta. Reaproveita {@link Veredito}:
 * é só vocabulário (sem persistência/comportamento), não custa acoplar API a ele.
 */
public record ResultadoVotacaoResponse(
        UUID pautaId,
        long totalVotosSim,
        long totalVotosNao,
        long totalVotos,
        Veredito resultado
) {
}
