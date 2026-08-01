package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.dto.request.VotoRequest;
import br.com.cooperativevoting.api.dto.response.VotoResponse;
import br.com.cooperativevoting.api.mapper.VotoMapper;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.model.Voto;
import br.com.cooperativevoting.domain.service.SessaoVotacaoService;
import br.com.cooperativevoting.domain.service.VotoService;
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
 * Endpoint REST de recebimento de voto na sessão de votação de uma pauta.
 */
@RestController
@RequestMapping("/pautas/{pautaId}/votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final VotoMapper votoMapper;

    /** Registra o voto de um associado na pauta. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VotoResponse votar(@PathVariable UUID pautaId, @Valid @RequestBody VotoRequest request) {
        SessaoVotacao sessao = sessaoVotacaoService.buscarPorPautaId(pautaId);
        Voto voto = votoService.votar(sessao, request.associadoId(), request.opcao());
        return votoMapper.toResponse(voto);
    }
}
