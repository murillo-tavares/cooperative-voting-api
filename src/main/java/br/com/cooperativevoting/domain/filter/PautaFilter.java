package br.com.cooperativevoting.domain.filter;

import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.specification.PautaSpecifications;
import br.com.cooperativevoting.domain.specification.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Filtros aceitos na listagem de {@link Pauta}. Chega pronto do controller (um campo por
 * parâmetro de query) e se resolve para uma única {@link Specification} — quem consome não
 * precisa conhecer {@link PautaSpecifications} nem {@link SpecificationBuilder}.
 */
public record PautaFilter(
        LocalDateTime dataCriacaoMaior,
        LocalDateTime dataCriacaoMenor
) {

    public Specification<Pauta> toSpecification() {
        return new SpecificationBuilder<Pauta>()
                .addIfPresent(dataCriacaoMaior, PautaSpecifications::comDataCriacaoMaiorQue)
                .addIfPresent(dataCriacaoMenor, PautaSpecifications::comDataCriacaoMenorQue)
                .build();
    }
}
