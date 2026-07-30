package br.com.cooperativevoting.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para criar ou atualizar uma pauta.
 */
public record PautaRequest(

        @NotBlank(message = "titulo é obrigatório")
        @Size(max = 120, message = "titulo deve ter no máximo 120 caracteres")
        String titulo,

        @Size(max = 500, message = "descricao deve ter no máximo 500 caracteres")
        String descricao
) {
}
