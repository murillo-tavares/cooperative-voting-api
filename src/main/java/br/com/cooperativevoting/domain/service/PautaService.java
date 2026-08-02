package br.com.cooperativevoting.domain.service;

import br.com.cooperativevoting.domain.exception.PautaNaoEncontradaException;
import br.com.cooperativevoting.domain.filter.PautaFilter;
import br.com.cooperativevoting.domain.mapper.PautaUpdateMapper;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Regras de negócio do CRUD de pauta. Trabalha só com o domínio ({@link Pauta}) —
 * conversão para/de DTO é responsabilidade da camada de API.
 */
@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final PautaUpdateMapper pautaUpdateMapper;

    /** Cria uma nova pauta. */
    public Pauta criar(Pauta pauta) {
        return pautaRepository.saveAndFlush(pauta);
    }

    /** Lista todas as pautas não excluídas. */
    public List<Pauta> listar() {
        return pautaRepository.findAll();
    }

    /** Lista pautas utilizando paginação e os filtros informados. */
    public Page<Pauta> listar(Pageable pageable, PautaFilter filtro) {
        return pautaRepository.findAll(filtro.toSpecification(), pageable);
    }

    /**
     * Busca uma pauta pelo id.
     *
     * @throws PautaNaoEncontradaException se não existir
     */
    public Pauta buscarPorId(UUID id) {
        return pautaRepository.findById(id).orElseThrow(() -> PautaNaoEncontradaException.id(id));
    }

    /** Atualiza título e descrição de uma pauta existente. */
    public Pauta atualizar(Pauta pauta, Pauta dadosAtualizados) {
        pautaUpdateMapper.atualizarCampos(pauta, dadosAtualizados);
        return pautaRepository.saveAndFlush(pauta);
    }

    /** Exclui (logicamente) uma pauta existente. */
    public void excluir(UUID id) {
        Pauta pauta = buscarPorId(id);
        pautaRepository.delete(pauta);
    }
}
