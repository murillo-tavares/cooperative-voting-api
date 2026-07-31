package br.com.cooperativevoting.domain.service;

import br.com.cooperativevoting.domain.exception.SessaoVotacaoNaoEncontradaException;
import br.com.cooperativevoting.domain.exception.constraint.ConstraintViolationTranslator;
import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.model.SessaoVotacao;
import br.com.cooperativevoting.domain.repository.SessaoVotacaoRepository;
import br.com.cooperativevoting.properties.SessaoVotacaoProperties;
import lombok.RequiredArgsConstructor;
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
     * A unicidade de sessão por pauta não é validada com um SELECT prévio: sob concorrência,
     * duas requisições passariam no check e ambas tentariam inserir. A constraint
     * {@code uk_sessao_votacao_pauta} do banco é a fonte de verdade; a violação é traduzida
     * para exceção de negócio pelo {@link ConstraintViolationTranslator}.
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
            return sessaoVotacaoRepository.saveAndFlush(sessao);
        } catch (DataIntegrityViolationException exception) {
            throw constraintViolationTranslator.traduzir(exception);
        }
    }

    /** Busca uma sessão de votação pelo id. Lança 404 se não existir. */
    public SessaoVotacao buscarPorId(UUID id) {
        return sessaoVotacaoRepository.findById(id).orElseThrow(() -> SessaoVotacaoNaoEncontradaException.id(id));
    }
}
