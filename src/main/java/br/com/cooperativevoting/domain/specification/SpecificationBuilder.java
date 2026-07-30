package br.com.cooperativevoting.domain.specification;

import br.com.cooperativevoting.domain.util.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Builder fluente para compor {@link Specification} de forma legível.
 * O padrão utilizado é encadear {@code add(...)} e {@code addIfPresent(...)} e, no fim,
 * gerar uma única specification combinada com {@code AND}.
 */
public final class SpecificationBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    /**
     * Adiciona uma specification à composição.
     *
     * @param specification specification a ser incluída na cadeia; valores nulos são ignorados
     * @return a própria instância para encadeamento
     */
    public SpecificationBuilder<T> add(Specification<T> specification) {
        if (specification != null) {
            specifications.add(specification);
        }
        return this;
    }

    /**
     * Adiciona uma specification apenas quando o valor informado estiver presente.
     *
     * @param value valor a ser verificado; se for {@code null}, a specification não é adicionada
     * @param mapper função que converte o valor em uma specification
     * @param <V> tipo do valor informado
     * @return a própria instância para encadeamento
     * @example addIfPresent(dataInicio, PautaSpecifications::comDataCriacaoMaiorQue)
     */
    public <V> SpecificationBuilder<T> addIfPresent(V value, Function<V, Specification<T>> mapper) {
        if (value != null && mapper != null) {
            Specification<T> applied = mapper.apply(value);
            return add(applied);
        }
        return this;
    }

    /**
     * Produz a specification final combinando todas as entradas com {@code AND}.
     *
     * @return specification pronta para ser usada em consultas JPA
     */
    public Specification<T> build() {
        return SpecificationUtils.andAll(specifications);
    }
}
