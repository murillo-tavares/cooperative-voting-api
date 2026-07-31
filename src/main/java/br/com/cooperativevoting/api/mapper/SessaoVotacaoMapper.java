package br.com.cooperativevoting.api.mapper;

import br.com.cooperativevoting.api.dto.response.SessaoVotacaoResponse;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import org.mapstruct.Mapper;

/**
 * Conversão entre {@link SessaoVotacao} e seu DTO de saída.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {

    SessaoVotacaoResponse toResponse(SessaoVotacao sessaoVotacao);
}
