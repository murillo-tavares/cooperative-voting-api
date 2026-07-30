package br.com.cooperativevoting.support.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

/**
 * Desserializa o corpo de uma resposta MockMvc pra um DTO, evitando repetir
 * {@code objectMapper.readValue(...)} em todo teste que precisa inspecionar o corpo.
 */
@TestComponent
@RequiredArgsConstructor
public class MvcResultUtils {

    private final ObjectMapper objectMapper;

    public <T> T corpo(MvcResult resultado, Class<T> tipo) throws Exception {
        return objectMapper.readValue(resultado.getResponse().getContentAsString(), tipo);
    }
}
