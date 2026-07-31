package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * O associado não está apto a votar, segundo a consulta externa de aptidão.
 */
public final class AssociadoNaoAptoException extends AbstractThrowableProblem {

    public static final String CODIGO = "ASSOCIADO_NAO_APTO";

    private AssociadoNaoAptoException(String detail) {
        super(null, CODIGO, Status.FORBIDDEN, detail);
    }

    public static AssociadoNaoAptoException associado(String associadoId) {
        return new AssociadoNaoAptoException("Associado não apto a votar: " + associadoId);
    }
}
