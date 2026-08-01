package br.com.cooperativevoting.domain.service;

import br.com.cooperativevoting.domain.client.VotoAptidaoClient;
import br.com.cooperativevoting.domain.exception.constraint.ConstraintViolationTranslator;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.model.ResultadoVotacao;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.model.SimNao;
import br.com.cooperativevoting.domain.model.Voto;
import br.com.cooperativevoting.domain.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Regras de negócio de recebimento de voto.
 */
@Service
@RequiredArgsConstructor
public class VotoService {

    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final VotoAptidaoClient votoAptidaoClient;
    private final ConstraintViolationTranslator constraintViolationTranslator;

    /**
     * Sem SELECT prévio pra checar duplicidade — {@code uk_voto_pauta_associado} garante,
     * e a violação vira exceção via {@link ConstraintViolationTranslator}.
     */
    public Voto votar(SessaoVotacao sessao, String associadoId, SimNao opcao) {
        sessao.requireNaoEncerrada();
        votoAptidaoClient.requirePodeVotar(associadoId);

        Voto voto = Voto.novo(sessao, associadoId, opcao);

        try {
            Voto salvo = votoRepository.saveAndFlush(voto);
            log.info("Voto registrado: sessaoId={}, opcao={}", sessao.getId(), opcao);
            return salvo;
        } catch (DataIntegrityViolationException exception) {
            throw constraintViolationTranslator.traduzir(exception);
        }
    }

    /** Apura o total de votos de uma pauta. */
    public ResultadoVotacao resultado(Pauta pauta) {
        return votoRepository.resultado(pauta.getId());
    }
}
