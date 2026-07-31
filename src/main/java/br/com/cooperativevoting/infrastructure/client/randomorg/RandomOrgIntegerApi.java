package br.com.cooperativevoting.infrastructure.client.randomorg;

import org.springframework.web.service.annotation.GetExchange;

/**
 * Contrato HTTP da API do random.org.
 */
interface RandomOrgIntegerApi {

    /**
     * Retorna um único dígito, "0" ou "1", como texto puro.
     * A query string fixa (quantidade, faixa, formato) já está na base URL configurada
     * em {@link br.com.cooperativevoting.properties.RandomOrgProperties}.
     */
    @GetExchange
    String buscarBit();
}
