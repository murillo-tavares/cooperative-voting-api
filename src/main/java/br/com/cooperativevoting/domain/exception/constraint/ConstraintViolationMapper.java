package br.com.cooperativevoting.domain.exception.constraint;

import org.springframework.dao.DataIntegrityViolationException;
import org.zalando.problem.AbstractThrowableProblem;

/**
 * Traduz a violação de uma constraint específica do banco para uma exceção de negócio.
 * Implementações são descobertas automaticamente pelo Spring (ver {@link ConstraintViolationTranslator}) —
 * mapear uma nova constraint é só criar uma nova implementação, sem tocar em código existente.
 */
public interface ConstraintViolationMapper {

    /** Nome da constraint (banco) que esta implementação sabe traduzir. */
    String constraintName();

    /** Constrói a exceção de negócio correspondente à violação. */
    AbstractThrowableProblem mapear(DataIntegrityViolationException exception);
}
