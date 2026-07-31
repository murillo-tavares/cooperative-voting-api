package br.com.cooperativevoting.domain.exception.constraint;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Associado já registrou voto nesta pauta.
 */
@Component
public final class VotoJaRegistradoException extends AbstractThrowableProblem implements ConstraintViolationMapper {

    public static final String CODIGO = "VOTO_JA_REGISTRADO";
    private static final String CONSTRAINT_NAME = "uk_voto_pauta_associado";

    VotoJaRegistradoException() {
        super(null, CODIGO, Status.CONFLICT, "Associado já votou nesta pauta");
    }

    @Override
    public String constraintName() {
        return CONSTRAINT_NAME;
    }

    @Override
    public VotoJaRegistradoException mapear(DataIntegrityViolationException exception) {
        return new VotoJaRegistradoException();
    }
}
