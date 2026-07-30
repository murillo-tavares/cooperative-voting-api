package br.com.cooperativevoting.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;

/**
 * Auxilia a composição de {@link Specification} em consultas JPA.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationUtils {

    /**
     * Combina todas as specifications recebidas em uma única specification aplicada com {@code AND}.
     *
     * @param specifications lista de specifications a ser combinada; valores nulos são ignorados
     * @param <T> tipo da entidade sobre a qual a specification opera
     * @return uma specification pronta para ser usada em consultas JPA
     */
    public static <T> Specification<T> andAll(List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(
                        Specification.<T>where((root, query, cb) -> cb.conjunction()),
                        Specification::and
                );
    }
}
