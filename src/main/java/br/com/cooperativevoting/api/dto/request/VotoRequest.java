package br.com.cooperativevoting.api.dto.request;

import br.com.cooperativevoting.domain.model.SimNao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record VotoRequest(

        @NotBlank(message = "associadoId é obrigatório")
        @CPF(message = "associadoId inválido (CPF)")
        String associadoId,

        @NotNull(message = "opcao é obrigatório")
        SimNao opcao
) {
}
