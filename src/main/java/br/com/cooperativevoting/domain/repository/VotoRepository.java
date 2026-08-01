package br.com.cooperativevoting.domain.repository;

import br.com.cooperativevoting.domain.model.SimNao;
import br.com.cooperativevoting.domain.model.ResultadoVotacao;
import br.com.cooperativevoting.domain.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Acesso a dados do {@link Voto}.
 * Unicidade de voto por associado em uma pauta é garantida pela constraint
 * {@code UNIQUE(pauta_id, associado_id)} no banco.
 */
public interface VotoRepository extends JpaRepository<Voto, UUID> {

    long countByPautaIdAndOpcao(UUID pautaId, SimNao opcao);

    /** Apura o resultado da pauta a partir da contagem por opção — só {@code SIM}/{@code NAO} existem. */
    default ResultadoVotacao resultado(UUID pautaId) {
        long totalSim = countByPautaIdAndOpcao(pautaId, SimNao.SIM);
        long totalNao = countByPautaIdAndOpcao(pautaId, SimNao.NAO);
        return ResultadoVotacao.calcular(pautaId, totalSim, totalNao);
    }
}
