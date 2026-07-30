package br.com.cooperativevoting.domain.exception.constraint;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Sessão de votação já foi aberta para a pauta.
 */
@Component
public final class SessaoVotacaoJaAbertaException extends AbstractThrowableProblem implements ConstraintViolationMapper {

    public static final String CODIGO = "SESSAO_VOTACAO_JA_ABERTA";
    private static final String CONSTRAINT_NAME = "uk_sessao_votacao_pauta";

    SessaoVotacaoJaAbertaException() {
        super(null, CODIGO, Status.CONFLICT, "Sessão de votação já aberta para esta pauta");
    }

    @Override
    public String constraintName() {
        return CONSTRAINT_NAME;
    }

    @Override
    public SessaoVotacaoJaAbertaException mapear(DataIntegrityViolationException exception) {
        return new SessaoVotacaoJaAbertaException();
    }
}
