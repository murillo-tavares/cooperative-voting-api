package br.com.cooperativevoting.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.time.LocalDateTime;
import java.util.UUID;

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

    /** UUID gerado em memória (não sequencial, não adivinhável) — é o próprio identificador público. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
}
