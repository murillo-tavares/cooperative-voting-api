package br.com.cooperativevoting.domain.repository;

import br.com.cooperativevoting.domain.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados do {@link Voto}.
 * Unicidade de voto por associado em uma pauta é garantida pela constraint
 * {@code UNIQUE(pauta_id, associado_id)} no banco.
 */
public interface VotoRepository extends JpaRepository<Voto, UUID>, JpaSpecificationExecutor<Voto> {
}
