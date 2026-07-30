package br.com.cooperativevoting.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.cooperativevoting.domain.util.CodigoGeradorUtils;

import java.time.LocalDateTime;

/**
 * Pauta submetida para votação em assembleia.
 */
@Entity
@Table(name = "pauta")
@SQLDelete(sql = "UPDATE pauta SET data_exclusao = now() WHERE id = ?")
@SQLRestriction("data_exclusao IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** {@code id} é interno (chave técnica); a API e os DTOs trabalham só com {@code codigo}. */
    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Exclusão é lógica (soft delete): {@code delete} vira um UPDATE que preenche este campo,
     * e toda consulta já ignora os registros excluídos.
     */
    @Column(name = "data_exclusao")
    private LocalDateTime dataExclusao;

    /** Gera o código público antes do insert; é interno à entidade porque só ela sabe seu prefixo. */
    @PrePersist
    public void prePersist() {
        this.codigo = CodigoGeradorUtils.gerar("pt_");
    }
}
