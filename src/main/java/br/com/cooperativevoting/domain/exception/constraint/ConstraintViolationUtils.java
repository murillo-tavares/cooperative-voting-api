package br.com.cooperativevoting.domain.exception.constraint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Extração de dados de {@link DataIntegrityViolationException}, reutilizável por quem
 * precisa saber qual constraint do banco foi violada.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstraintViolationUtils {

    /** Nome da constraint violada, ou {@code null} se a causa não for uma violação de constraint. */
    public static String nomeConstraint(DataIntegrityViolationException exception) {
        return exception.getCause() instanceof ConstraintViolationException constraintViolation
                ? constraintViolation.getConstraintName()
                : null;
    }
}
