package br.com.cooperativevoting.domain.repository;

import br.com.cooperativevoting.domain.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados da {@link Pauta}.
 * A exclusão lógica é aplicada automaticamente pelo {@code @SQLRestriction} da entidade.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification} (ver {@code PautaSpecifications}).
 */
public interface PautaRepository extends JpaRepository<Pauta, UUID>, JpaSpecificationExecutor<Pauta> {
}
