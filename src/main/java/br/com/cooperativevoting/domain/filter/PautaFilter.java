package br.com.cooperativevoting.domain.filter;

import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.specification.PautaSpecifications;
import br.com.cooperativevoting.domain.specification.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Filtros aceitos na listagem de {@link Pauta}.
 * Se resolve para uma única {@link Specification}.
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
