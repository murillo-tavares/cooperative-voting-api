package br.com.cooperativevoting.domain.exception.constraint;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Traduz {@link DataIntegrityViolationException} para exceção de negócio, com base na
 * constraint violada no banco. Mapeadores são descobertos automaticamente via injeção de
 * lista do Spring — adicionar um novo caso não exige alterar esta classe.
 */
@Component
public class ConstraintViolationTranslator {

    private final Map<String, ConstraintViolationMapper> mappersPorConstraint;

    public ConstraintViolationTranslator(List<ConstraintViolationMapper> mappers) {
        this.mappersPorConstraint = mappers.stream()
                .collect(Collectors.toMap(ConstraintViolationMapper::constraintName, Function.identity()));
    }

    /**
     * Traduz a exceção se a constraint violada for conhecida; senão relança a original,
     * já que uma violação não mapeada pode ter causa completamente diferente.
     */
    public RuntimeException traduzir(DataIntegrityViolationException exception) {
        String constraintName = ConstraintViolationUtils.nomeConstraint(exception);
        ConstraintViolationMapper mapper = constraintName != null ? mappersPorConstraint.get(constraintName) : null;
        return mapper != null ? mapper.mapear(exception) : exception;
    }
}
