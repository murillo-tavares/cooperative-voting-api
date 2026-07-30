package br.com.cooperativevoting.api.dto.response;

import java.time.LocalDateTime;

/**
 * Dados de saída de uma pauta.
 */
public record PautaResponse(
        String codigo,
        String titulo,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
