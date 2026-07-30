package br.com.cooperativevoting.domain.service;

import br.com.cooperativevoting.domain.exception.PautaNaoEncontradaException;
import br.com.cooperativevoting.domain.mapper.PautaUpdateMapper;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.repository.PautaRepository;
import br.com.cooperativevoting.domain.specification.PautaSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return pautaRepository.save(pauta);
    }

    /** Lista todas as pautas não excluídas. */
    public List<Pauta> listar() {
        return pautaRepository.findAll();
    }

    /** Lista pautas utilizando paginação e uma specification já resolvida. */
    public Page<Pauta> listar(Pageable pageable, Specification<Pauta> specification) {
        return pautaRepository.findAll(specification, pageable);
    }

    /** Busca uma pauta pelo código. Lança 404 se não existir. */
    public Pauta buscarPorCodigo(String codigo) {
        Specification<Pauta> specification = PautaSpecifications.comCodigo(codigo);
        Optional<Pauta> pautaEncontrada = pautaRepository.findOne(specification);
        return pautaEncontrada.orElseThrow(() -> PautaNaoEncontradaException.codigo(codigo));
    }

    /** Atualiza título e descrição de uma pauta existente. */
    public Pauta atualizar(Pauta pauta, Pauta dadosAtualizados) {
        pautaUpdateMapper.atualizarCampos(pauta, dadosAtualizados);
        return pautaRepository.save(pauta);
    }

    /** Exclui (logicamente) uma pauta existente. */
    public void excluir(String codigo) {
        Pauta pauta = buscarPorCodigo(codigo);
        pautaRepository.delete(pauta);
    }
}
