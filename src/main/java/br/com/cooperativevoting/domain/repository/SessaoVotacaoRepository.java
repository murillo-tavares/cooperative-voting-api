package br.com.cooperativevoting.domain.repository;

import br.com.cooperativevoting.domain.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Acesso a dados da {@link SessaoVotacao}.
 * Unicidade de sessão por pauta é garantida pela constraint {@code UNIQUE(pauta_id)} no banco,
 * não por consulta prévia (evita condição de corrida sob concorrência).
 */
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long>, JpaSpecificationExecutor<SessaoVotacao> {
}
