package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.dto.request.SessaoVotacaoRequest;
import br.com.cooperativevoting.api.dto.response.SessaoVotacaoResponse;
import br.com.cooperativevoting.api.mapper.SessaoVotacaoMapper;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.service.PautaService;
import br.com.cooperativevoting.domain.service.SessaoVotacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Endpoint REST de abertura de sessão de votação em uma pauta.
 */
@RestController
@RequestMapping("/pautas/{pautaId}/sessoes")
@RequiredArgsConstructor
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;
    private final PautaService pautaService;
    private final SessaoVotacaoMapper sessaoVotacaoMapper;

    /** Abre uma sessão de votação para a pauta. Corpo é opcional (duração default de 1 minuto). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoVotacaoResponse abrir(
            @PathVariable UUID pautaId,
            @Valid @RequestBody(required = false) SessaoVotacaoRequest request) {

        Pauta pauta = pautaService.buscarPorId(pautaId);

        SessaoVotacao sessao = request != null && request.duracaoSegundos() != null
                ? sessaoVotacaoService.abrir(pauta, request.duracaoSegundos())
                : sessaoVotacaoService.abrir(pauta);

        return sessaoVotacaoMapper.toResponse(sessao);
    }
}
