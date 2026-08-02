package br.com.cooperativevoting.api.mapper;

import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.api.dto.request.PautaRequest;
import br.com.cooperativevoting.api.dto.response.PautaResponse;
import org.mapstruct.Mapper;

/**
 * Conversão entre {@link Pauta} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface PautaMapper {

    Pauta toEntity(PautaRequest request);

    PautaResponse toResponse(Pauta pauta);
}
