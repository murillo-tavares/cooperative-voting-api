package br.com.cooperativevoting.api.mapper;

import br.com.cooperativevoting.api.dto.response.VotoResponse;
import br.com.cooperativevoting.domain.model.Voto;
import org.mapstruct.Mapper;

/**
 * Conversão entre {@link Voto} e seu DTO de saída.
 */
@Mapper(componentModel = "spring")
public interface VotoMapper {

    VotoResponse toResponse(Voto voto);
}
