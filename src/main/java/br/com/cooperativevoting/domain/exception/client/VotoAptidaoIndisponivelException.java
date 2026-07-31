package br.com.cooperativevoting.domain.exception.client;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * A consulta de aptidão para votar não pôde ser concluída (serviço externo indisponível,
 * timeout ou resposta em formato inesperado). 503: a falha é da dependência externa, não
 * de algo que o cliente da API tenha feito de errado.
 */
public final class VotoAptidaoIndisponivelException extends AbstractThrowableProblem {

    public static final String CODIGO = "VOTO_APTIDAO_INDISPONIVEL";

    private VotoAptidaoIndisponivelException(String detail) {
        super(null, CODIGO, Status.SERVICE_UNAVAILABLE, detail);
    }

    public static VotoAptidaoIndisponivelException respostaInesperada(String resposta) {
        return new VotoAptidaoIndisponivelException("Resposta inesperada do serviço externo: '" + resposta + "'");
    }

    public static VotoAptidaoIndisponivelException falhaNaConsulta(Exception causa) {
        return new VotoAptidaoIndisponivelException("Falha ao consultar serviço externo de aptidão de voto: " + causa.getMessage());
    }
}
