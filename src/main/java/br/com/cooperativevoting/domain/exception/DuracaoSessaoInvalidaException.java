package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Duração de sessão de votação abaixo do mínimo permitido.
 */
public final class DuracaoSessaoInvalidaException extends AbstractThrowableProblem {

    public static final String CODIGO = "DURACAO_SESSAO_INVALIDA";

    private DuracaoSessaoInvalidaException(String detail) {
        super(null, CODIGO, Status.BAD_REQUEST, detail);
    }

    public static DuracaoSessaoInvalidaException minima(int duracaoSegundos, int duracaoMinimaSegundos) {
        return new DuracaoSessaoInvalidaException(
                "duracaoSegundos deve ser >= " + duracaoMinimaSegundos + ": " + duracaoSegundos);
    }
}
