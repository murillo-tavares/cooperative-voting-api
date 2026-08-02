package br.com.cooperativevoting.domain.service;

import br.com.cooperativevoting.domain.exception.SessaoVotacaoNaoEncontradaException;
import br.com.cooperativevoting.domain.exception.constraint.ConstraintViolationTranslator;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.repository.SessaoVotacaoRepository;
import br.com.cooperativevoting.domain.specification.SessaoVotacaoSpecifications;
import br.com.cooperativevoting.properties.SessaoVotacaoProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Regras de negócio de abertura de sessão de votação.
 */
@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final Logger log = LoggerFactory.getLogger(SessaoVotacaoService.class);

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final SessaoVotacaoProperties sessaoVotacaoProperties;
    private final ConstraintViolationTranslator constraintViolationTranslator;

    /** Abre uma sessão de votação para a pauta com a duração default configurada. */
    public SessaoVotacao abrir(Pauta pauta) {
        return abrir(pauta, sessaoVotacaoProperties.duracaoPadraoSegundos());
    }

    /**
     * Abre uma sessão de votação para a pauta com duração explícita, em segundos.
     * <p>
     * Sem SELECT prévio pra checar duplicidade: {@code uk_sessao_votacao_pauta} garante, e a
     * violação vira exceção via {@link ConstraintViolationTranslator}.
     */
    public SessaoVotacao abrir(Pauta pauta, int duracaoSegundos) {
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("duracaoSegundos deve ser positivo: " + duracaoSegundos);
        }

        LocalDateTime abertura = LocalDateTime.now();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .dataAbertura(abertura)
                .dataFechamento(abertura.plusSeconds(duracaoSegundos))
                .build();

        try {
            SessaoVotacao salva = sessaoVotacaoRepository.saveAndFlush(sessao);
            log.info("Sessão de votação aberta: pautaId={}, dataFechamento={}", pauta.getId(), salva.getDataFechamento());
            return salva;
        } catch (DataIntegrityViolationException exception) {
            throw constraintViolationTranslator.traduzir(exception);
        }
    }

    /**
     * Busca a sessão de votação da pauta.
     *
     * @throws SessaoVotacaoNaoEncontradaException se nenhuma sessão tiver sido aberta para ela
     */
    public SessaoVotacao buscarPorPautaId(UUID pautaId) {
        return sessaoVotacaoRepository.findOne(SessaoVotacaoSpecifications.comPautaId(pautaId))
                .orElseThrow(() -> SessaoVotacaoNaoEncontradaException.pautaId(pautaId));
    }
}
