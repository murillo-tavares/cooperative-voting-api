package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.dto.request.VotoRequest;
import br.com.cooperativevoting.domain.client.VotoAptidaoClient;
import br.com.cooperativevoting.domain.exception.AssociadoNaoAptoException;
import br.com.cooperativevoting.domain.exception.SessaoVotacaoEncerradaException;
import br.com.cooperativevoting.domain.exception.SessaoVotacaoNaoEncontradaException;
import br.com.cooperativevoting.domain.exception.constraint.VotoJaRegistradoException;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.model.Voto;
import br.com.cooperativevoting.domain.repository.SessaoVotacaoRepository;
import br.com.cooperativevoting.domain.service.SessaoVotacaoService;
import br.com.cooperativevoting.support.fixture.PautaTestDataFactory;
import br.com.cooperativevoting.support.suite.IntegrationTest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(PautaTestDataFactory.class)
class VotoControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PautaTestDataFactory pautaTestDataFactory;

    @Autowired
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @MockitoBean
    private VotoAptidaoClient votoAptidaoClient;

    @BeforeEach
    void aptidaoLiberadaPorPadrao() {
        when(votoAptidaoClient.podeVotar(any())).thenReturn(true);
    }

    // ---- votar ----

    @Test
    void deveRegistrarVoto() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        SessaoVotacao sessao = sessaoVotacaoService.abrir(pauta);
        VotoRequest request = new VotoRequest("11111111111", Voto.Opcao.SIM);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pautaId").value(pauta.getId().toString()))
                .andExpect(jsonPath("$.sessaoId").value(sessao.getId().toString()))
                .andExpect(jsonPath("$.associadoId").value("11111111111"))
                .andExpect(jsonPath("$.opcao").value("SIM"))
                .andExpect(jsonPath("$.dataVoto").exists());
    }

    @Test
    void naoDeveRegistrarVotoComCamposInvalidos() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        sessaoVotacaoService.abrir(pauta);
        VotoRequest request = new VotoRequest(" ", null);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveRegistrarVotoParaPautaSemSessaoAberta() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        VotoRequest request = new VotoRequest("11111111111", Voto.Opcao.SIM);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(SessaoVotacaoNaoEncontradaException.CODIGO));
    }

    @Test
    void naoDeveRegistrarVotoComSessaoEncerrada() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        LocalDateTime abertura = LocalDateTime.now().minusMinutes(2);
        sessaoVotacaoRepository.saveAndFlush(SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .dataAbertura(abertura)
                .dataFechamento(abertura.plusMinutes(1))
                .build());
        VotoRequest request = new VotoRequest("11111111111", Voto.Opcao.SIM);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value(SessaoVotacaoEncerradaException.CODIGO));
    }

    @Test
    void naoDeveRegistrarVotoDuplicadoDoMesmoAssociado() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        sessaoVotacaoService.abrir(pauta);
        VotoRequest request = new VotoRequest("11111111111", Voto.Opcao.SIM);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value(VotoJaRegistradoException.CODIGO));
    }

    @Test
    void naoDeveRegistrarVotoDeAssociadoNaoApto() throws Exception {
        Pauta pauta = pautaTestDataFactory.persistirPauta();
        sessaoVotacaoService.abrir(pauta);
        when(votoAptidaoClient.podeVotar(any())).thenReturn(false);
        VotoRequest request = new VotoRequest("11111111111", Voto.Opcao.SIM);

        mockMvc.perform(post("/pautas/{pautaId}/votos", pauta.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value(AssociadoNaoAptoException.CODIGO));
    }
}
