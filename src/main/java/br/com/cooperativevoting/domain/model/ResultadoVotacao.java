package br.com.cooperativevoting.domain.model;

import java.util.UUID;

/**
 * Resultado apurado da votação de uma pauta.
 */
public record ResultadoVotacao(
        UUID pautaId,
        long totalVotosSim,
        long totalVotosNao,
        long totalVotos,
        Veredito resultado
) {

    public static ResultadoVotacao calcular(UUID pautaId, long totalVotosSim, long totalVotosNao) {
        Veredito resultado = Veredito.calcular(totalVotosSim, totalVotosNao);
        return new ResultadoVotacao(
                pautaId,
                totalVotosSim,
                totalVotosNao,
                totalVotosSim + totalVotosNao,
                resultado);
    }
}
