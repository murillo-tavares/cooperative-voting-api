package br.com.cooperativevoting.domain.specification;

import br.com.cooperativevoting.domain.model.Pauta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications da {@link Pauta}, uma por campo.
 * Mantidas separadas para permitir compor filtros (and/or) sem precisar de um método por combinação.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PautaSpecifications {

    /** Filtra pela data de criação maior que a informada. */
    public static Specification<Pauta> comDataCriacaoMaiorQue(LocalDateTime dataCriacao) {
        return (root, query, cb) -> cb.greaterThan(root.get("dataCriacao"), dataCriacao);
    }

    /** Filtra pela data de criação menor que a informada. */
    public static Specification<Pauta> comDataCriacaoMenorQue(LocalDateTime dataCriacao) {
        return (root, query, cb) -> cb.lessThan(root.get("dataCriacao"), dataCriacao);
    }
}
