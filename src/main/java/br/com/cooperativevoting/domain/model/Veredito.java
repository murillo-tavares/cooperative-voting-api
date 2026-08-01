package br.com.cooperativevoting.domain.model;

public enum Veredito {
    APROVADA,
    REPROVADA,
    EMPATE;

    public static Veredito calcular(long totalVotosSim, long totalVotosNao) {
        if (totalVotosSim > totalVotosNao) {
            return APROVADA;
        }
        if (totalVotosNao > totalVotosSim) {
            return REPROVADA;
        }
        return EMPATE;
    }
}
