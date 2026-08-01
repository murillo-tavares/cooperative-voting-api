package br.com.cooperativevoting.domain.client;

import br.com.cooperativevoting.domain.exception.AssociadoNaoAptoException;
import br.com.cooperativevoting.domain.exception.client.VotoAptidaoIndisponivelException;

/**
 * Contrato para consulta externa de aptidão de um associado para votar.
 * <p>
 * Isola o domínio da API concreta usada por trás. A implementação hoje é apenas um
 * substituto de teste (o serviço originalmente definido para o desafio está fora do ar).
 * Trocar de fornecedor no futuro demanda apenas uma nova implementação
 * desta interface, sem alterar quem a consome.
 */
public interface VotoAptidaoClient {

    /**
     * Consulta se o associado identificado pelo CPF está apto a votar.
     *
     * @throws VotoAptidaoIndisponivelException se a consulta externa falhar ou responder
     *         de forma inesperada
     */
    boolean podeVotar(String cpf);

    /**
     * Precondição pra votar: consulta {@link #podeVotar(String)} e lança se o associado não
     * estiver apto.
     *
     * @throws AssociadoNaoAptoException se o associado não estiver apto a votar
     */
    default void requirePodeVotar(String cpf) {
        if (!podeVotar(cpf)) {
            throw AssociadoNaoAptoException.associado(cpf);
        }
    }
}
