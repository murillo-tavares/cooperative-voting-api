package br.com.cooperativevoting.api.dto.request;

import jakarta.validation.constraints.Positive;

public record SessaoVotacaoRequest(

        @Positive(message = "duracaoSegundos deve ser positivo")
        Integer duracaoSegundos
) {
}
