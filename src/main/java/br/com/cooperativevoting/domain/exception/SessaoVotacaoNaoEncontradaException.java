package br.com.cooperativevoting.domain.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.UUID;

/**
 * Sessão de votação não encontrada para a pauta informada.
 */
public final class SessaoVotacaoNaoEncontradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "SESSAO_VOTACAO_NAO_ENCONTRADA";

    private SessaoVotacaoNaoEncontradaException(String detail) {
        super(null, CODIGO, Status.NOT_FOUND, detail);
    }

    public static SessaoVotacaoNaoEncontradaException pautaId(UUID pautaId) {
        return new SessaoVotacaoNaoEncontradaException("Sessão de votação não encontrada para a pauta: " + pautaId);
    }
}
