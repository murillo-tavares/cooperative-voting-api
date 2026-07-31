package br.com.cooperativevoting.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dados de saída de uma pauta.
 */
public record PautaResponse(
        UUID id,
        String titulo,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
