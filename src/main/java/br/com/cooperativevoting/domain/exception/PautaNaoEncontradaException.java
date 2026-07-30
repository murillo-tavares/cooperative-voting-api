package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Pauta não encontrada pelo código informado.
 */
public final class PautaNaoEncontradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "PAUTA_NAO_ENCONTRADA";

    private PautaNaoEncontradaException(String detail) {
        super(null, CODIGO, Status.NOT_FOUND, detail);
    }

    public static PautaNaoEncontradaException codigo(String codigo) {
        return new PautaNaoEncontradaException("Pauta não encontrada: " + codigo);
    }
}
