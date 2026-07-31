package br.com.cooperativevoting.domain.mapper;

import br.com.cooperativevoting.domain.model.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Aplica os campos editáveis de {@code dadosAtualizados} sobre uma {@link Pauta} já persistida.
 * Só {@code domain} → {@code domain}, sem DTO — mantém o mapper de API isolado da camada de domínio.
 * Campos internos/gerados ficam explicitamente fora do merge para não serem sobrescritos com null.
 */
@Mapper(componentModel = "spring")
public interface PautaUpdateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "dataExclusao", ignore = true)
    void atualizarCampos(@MappingTarget Pauta pauta, Pauta dadosAtualizados);
}
