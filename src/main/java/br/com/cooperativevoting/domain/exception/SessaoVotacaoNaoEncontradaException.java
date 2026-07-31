package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.UUID;

/**
 * Sessão de votação não encontrada pelo id informado.
 */
public final class SessaoVotacaoNaoEncontradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "SESSAO_VOTACAO_NAO_ENCONTRADA";

    private SessaoVotacaoNaoEncontradaException(String detail) {
        super(null, CODIGO, Status.NOT_FOUND, detail);
    }

    public static SessaoVotacaoNaoEncontradaException id(UUID id) {
        return new SessaoVotacaoNaoEncontradaException("Sessão de votação não encontrada: " + id);
    }
}
