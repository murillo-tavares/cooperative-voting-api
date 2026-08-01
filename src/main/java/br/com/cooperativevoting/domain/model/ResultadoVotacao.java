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
        Resultado resultado
) {

    public static ResultadoVotacao calcular(UUID pautaId, long totalVotosSim, long totalVotosNao) {
        Resultado resultado = Resultado.calcular(totalVotosSim, totalVotosNao);
        return new ResultadoVotacao(
                pautaId,
                totalVotosSim,
                totalVotosNao,
                totalVotosSim + totalVotosNao,
                resultado);
    }

    public enum Resultado {
        APROVADA,
        REPROVADA,
        EMPATE;

        public static Resultado calcular(long totalVotosSim, long totalVotosNao) {
            if (totalVotosSim > totalVotosNao) {
                return APROVADA;
            }
            if (totalVotosNao > totalVotosSim) {
                return REPROVADA;
            }
            return EMPATE;
        }
    }
}
