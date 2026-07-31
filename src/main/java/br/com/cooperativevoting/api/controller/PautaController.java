package br.com.cooperativevoting.api.controller;

import br.com.cooperativevoting.api.mapper.PautaMapper;
import br.com.cooperativevoting.domain.filter.PautaFilter;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.service.PautaService;
import br.com.cooperativevoting.api.dto.request.PautaRequest;
import br.com.cooperativevoting.api.dto.response.PautaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints REST do CRUD de pauta. Converte entre DTO e domínio usando {@link PautaMapper}.
 */
@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;
    private final PautaMapper pautaMapper;

    /** Cadastra uma nova pauta. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PautaResponse criar(@Valid @RequestBody PautaRequest request) {
        Pauta pauta = pautaMapper.toEntity(request);
        Pauta pautaCriada = pautaService.criar(pauta);
        return pautaMapper.toResponse(pautaCriada);
    }

    /** Lista pautas com paginação e filtros por data de criação. */
    @GetMapping
    public Page<PautaResponse> listar(Pageable pageable, PautaFilter filtro) {
        Page<Pauta> pautas = pautaService.listar(pageable, filtro);
        return pautas.map(pautaMapper::toResponse);
    }

    /** Busca uma pauta pelo código. */
    @GetMapping("/{codigo}")
    public PautaResponse buscarPorCodigo(@PathVariable String codigo) {
        Pauta pauta = pautaService.buscarPorCodigo(codigo);
        return pautaMapper.toResponse(pauta);
    }

    /** Atualiza uma pauta existente. */
    @PutMapping("/{codigo}")
    public PautaResponse atualizar(@PathVariable String codigo, @Valid @RequestBody PautaRequest request) {
        Pauta pauta = pautaService.buscarPorCodigo(codigo);
        Pauta dadosAtualizados = pautaMapper.toEntity(request);
        Pauta pautaAtualizada = pautaService.atualizar(pauta, dadosAtualizados);
        return pautaMapper.toResponse(pautaAtualizada);
    }

    /** Exclui (logicamente) uma pauta existente. */
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String codigo) {
        pautaService.excluir(codigo);
    }
}
