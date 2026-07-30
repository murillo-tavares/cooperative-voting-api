package br.com.cooperativevoting.domain.specification;

import br.com.cooperativevoting.domain.model.Pauta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications da {@link Pauta}, uma por campo.
 * Mantidas separadas para permitir compor filtros (and/or) sem precisar de um método por combinação.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PautaSpecifications {

    /** Filtra pelo código público da pauta. */
    public static Specification<Pauta> comCodigo(String codigo) {
        return (root, query, cb) -> cb.equal(root.get("codigo"), codigo);
    }
}
