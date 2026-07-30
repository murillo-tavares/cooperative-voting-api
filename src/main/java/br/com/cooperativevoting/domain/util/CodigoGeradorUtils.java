package br.com.cooperativevoting.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Geração de código público curto (ex.: {@code pt_9f3ka2x1}), reutilizável por qualquer entidade.
 * Não sequencial, seguro para expor na API.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodigoGeradorUtils {

    public static String gerar(String prefixo) {
        String sufixo = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefixo + sufixo;
    }
}
