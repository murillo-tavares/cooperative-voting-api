package br.com.cooperativevoting.api.dto.response;

import br.com.cooperativevoting.domain.model.SimNao;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dados de saída de um voto registrado.
 */
public record VotoResponse(
        UUID pautaId,
        UUID sessaoId,
        String associadoId,
        SimNao opcao,
        LocalDateTime dataVoto
) {
}
