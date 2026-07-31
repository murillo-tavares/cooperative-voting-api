package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.UUID;

/**
 * Sessão de votação encerrada (janela de tempo passada) pelo id informado.
 */
public final class SessaoVotacaoEncerradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "SESSAO_VOTACAO_ENCERRADA";

    private SessaoVotacaoEncerradaException(String detail) {
        super(null, CODIGO, Status.CONFLICT, detail);
    }

    public static SessaoVotacaoEncerradaException id(UUID id) {
        return new SessaoVotacaoEncerradaException("Sessão de votação encerrada: " + id);
    }
}
