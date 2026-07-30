package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.dto.request.SessaoVotacaoRequest;
import br.com.cooperativevoting.api.dto.response.SessaoVotacaoResponse;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.support.fixture.PautaTestDataFactory;
import br.com.cooperativevoting.support.suite.IntegrationTest;
import br.com.cooperativevoting.support.util.MvcResultUtils;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({PautaTestDataFactory.class, MvcResultUtils.class})
class SessaoVotacaoControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaTestDataFactory pautaTestDataFactory;

    @Autowired
    private MvcResultUtils mvcResultUtils;

    // ---- abrir ----

    @Test
    void deveAbrirSessaoComDuracaoPadraoQuandoSemCorpo() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();

        MvcResult resultado = mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", pauta.getCodigo()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pautaCodigo").value(pauta.getCodigo()))
                .andExpect(jsonPath("$.status").value("ABERTA"))
                .andExpect(jsonPath("$.dataAbertura").exists())
                .andExpect(jsonPath("$.dataFechamento").exists())
                .andReturn();

        SessaoVotacaoResponse response = mvcResultUtils.corpo(resultado, SessaoVotacaoResponse.class);
        long duracaoSegundos = ChronoUnit.SECONDS.between(response.dataAbertura(), response.dataFechamento());

        assertThat(duracaoSegundos).isEqualTo(60);
    }

    @Test
    void deveAbrirSessaoComDuracaoInformada() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(120);

        MvcResult resultado = mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", pauta.getCodigo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        SessaoVotacaoResponse response = mvcResultUtils.corpo(resultado, SessaoVotacaoResponse.class);
        long duracaoSegundos = ChronoUnit.SECONDS.between(response.dataAbertura(), response.dataFechamento());

        assertThat(duracaoSegundos).isEqualTo(120);
    }

    @Test
    void naoDeveAbrirSessaoComDuracaoZeroOuNegativa() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(0);

        mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", pauta.getCodigo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveAbrirSessaoParaPautaInexistente() throws Exception {
        mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", "pt_inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveAbrirSessaoDuplicadaParaMesmaPauta() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();

        mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", pauta.getCodigo()))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/pautas/{codigoPauta}/sessoes", pauta.getCodigo()))
                .andExpect(status().isConflict());
    }
}
