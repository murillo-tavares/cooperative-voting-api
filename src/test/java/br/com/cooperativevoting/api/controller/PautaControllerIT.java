package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.dto.request.PautaRequest;
import br.com.cooperativevoting.domain.exception.PautaNaoEncontradaException;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.support.fixture.PautaTestDataFactory;
import br.com.cooperativevoting.domain.util.RegexUtils;
import br.com.cooperativevoting.support.suite.IntegrationTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(PautaTestDataFactory.class)
class PautaControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaTestDataFactory pautaTestDataFactory;

    // ---- criar ----

    @Test
    void deveCriarPauta() throws Exception {
        PautaRequest request = new PautaRequest("Alterar estatuto", "Proposta de alteração do estatuto social");

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", matchesPattern(RegexUtils.UUID_PATTERN)))
                .andExpect(jsonPath("$.titulo").value("Alterar estatuto"))
                .andExpect(jsonPath("$.descricao").value("Proposta de alteração do estatuto social"))
                .andExpect(jsonPath("$.dataCriacao").exists());
    }

    @Test
    void naoDeveCriarPautaComTituloEmBranco() throws Exception {
        PautaRequest request = new PautaRequest(" ", "Descrição qualquer");

        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ---- buscar por id ----

    @Test
    void deveBuscarPautaPorId() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();

        mockMvc.perform(get("/pautas/{id}", pauta.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pauta.getId().toString()))
                .andExpect(jsonPath("$.titulo").value(pauta.getTitulo()));
    }

    @Test
    void deveRetornar404AoBuscarPautaInexistente() throws Exception {
        mockMvc.perform(get("/pautas/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(PautaNaoEncontradaException.CODIGO));
    }

    // ---- listar ----

    @Test
    void deveListarPautasPaginado() throws Exception {
        pautaTestDataFactory.persistirPautas(3);

        mockMvc.perform(get("/pautas").param("size", "2").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void deveListarPautasFiltrandoPorDataCriacao() throws Exception {
        pautaTestDataFactory.persistirPautas(2);
        LocalDateTime noFuturo = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

        mockMvc.perform(get("/pautas").param("dataCriacaoMaior", noFuturo.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // ---- atualizar ----

    @Test
    void deveAtualizarPauta() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        PautaRequest request = new PautaRequest("Título atualizado", "Descrição atualizada");

        mockMvc.perform(put("/pautas/{id}", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pauta.getId().toString()))
                .andExpect(jsonPath("$.titulo").value("Título atualizado"))
                .andExpect(jsonPath("$.descricao").value("Descrição atualizada"));
    }

    @Test
    void deveRetornar404AoAtualizarPautaInexistente() throws Exception {
        PautaRequest request = new PautaRequest("Título", "Descrição");

        mockMvc.perform(put("/pautas/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(PautaNaoEncontradaException.CODIGO));
    }

    // ---- excluir ----

    @Test
    void deveExcluirPauta() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();

        mockMvc.perform(delete("/pautas/{id}", pauta.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/pautas/{id}", pauta.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(PautaNaoEncontradaException.CODIGO));
    }

    @Test
    void deveRetornar404AoExcluirPautaInexistente() throws Exception {
        mockMvc.perform(delete("/pautas/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(PautaNaoEncontradaException.CODIGO));
    }
}
