package br.com.cooperativevoting.api.mapper;

import br.com.cooperativevoting.api.dto.response.ResultadoVotacaoResponse;
import br.com.cooperativevoting.domain.model.ResultadoVotacao;
import org.mapstruct.Mapper;

/**
 * Conversão entre {@link ResultadoVotacao} e seu DTO de saída.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface ResultadoVotacaoMapper {

    ResultadoVotacaoResponse toResponse(ResultadoVotacao resultadoVotacao);
}
