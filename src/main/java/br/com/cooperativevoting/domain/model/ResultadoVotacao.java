package br.com.cooperativevoting.domain.model;

import java.util.UUID;

/**
 * Resultado apurado da votação de uma pauta. {@code totalVotos} e {@code resultado} são
 * calculados uma vez em {@link #calcular} — mesma ideia do status cacheado em {@link SessaoVotacao},
 * evita recalcular a cada leitura.
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
