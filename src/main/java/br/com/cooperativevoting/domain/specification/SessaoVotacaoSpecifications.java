package br.com.cooperativevoting.domain.specification;

import br.com.cooperativevoting.domain.model.SessaoVotacao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * Specifications da {@link SessaoVotacao}, uma por campo.
 * Mantidas separadas para permitir compor filtros (and/or) sem precisar de um método por combinação.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessaoVotacaoSpecifications {

    /** Filtra pela pauta a que a sessão pertence — relação 1:1, garantida por {@code UNIQUE(pauta_id)}. */
    public static Specification<SessaoVotacao> comPautaId(UUID pautaId) {
        return (root, query, cb) -> cb.equal(root.get("pautaId"), pautaId);
    }
}
