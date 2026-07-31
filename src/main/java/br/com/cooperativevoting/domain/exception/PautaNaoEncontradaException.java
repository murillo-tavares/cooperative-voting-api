package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.UUID;

/**
 * Pauta não encontrada pelo id informado.
 */
public final class PautaNaoEncontradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "PAUTA_NAO_ENCONTRADA";

    private PautaNaoEncontradaException(String detail) {
        super(null, CODIGO, Status.NOT_FOUND, detail);
    }

    public static PautaNaoEncontradaException id(UUID id) {
        return new PautaNaoEncontradaException("Pauta não encontrada: " + id);
    }
}
