package br.com.cooperativevoting.api.util;

import br.com.cooperativevoting.api.dto.response.ErroResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * Extrai os campos inválidos de exceções de validação do Spring MVC.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidacaoUtils {

    /** {@code null} se {@code ex} não for uma exceção de validação de campos. */
    public static List<ErroResponse.CampoInvalido> extrairCampos(Exception ex) {
        if (!(ex instanceof MethodArgumentNotValidException validacao)) {
            return null;
        }
        return validacao.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroResponse.CampoInvalido(erro.getField(), erro.getDefaultMessage()))
                .toList();
    }
}
