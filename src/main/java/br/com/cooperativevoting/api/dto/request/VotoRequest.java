package br.com.cooperativevoting.api.dto.request;

import br.com.cooperativevoting.domain.model.SimNao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VotoRequest(

        @NotBlank(message = "associadoId é obrigatório")
        String associadoId,

        @NotNull(message = "opcao é obrigatório")
        SimNao opcao
) {
}
