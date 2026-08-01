package br.com.cooperativevoting.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Voto de um associado em uma pauta.
 */
@Entity
@Table(name = "voto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Sem relação JPA pra {@link Pauta}: evita proxy/lazy loading. Unicidade: {@code UNIQUE(pauta_id, associado_id)}. */
    @Column(name = "pauta_id", nullable = false, updatable = false)
    private UUID pautaId;

    /** Sessão em que o voto foi aceito — janela de tempo específica, não a pauta em si. */
    @Column(name = "sessao_id", nullable = false, updatable = false)
    private UUID sessaoId;

    @Column(name = "associado_id", nullable = false, updatable = false, length = 20)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 10)
    private SimNao opcao;

    @CreationTimestamp
    @Column(name = "data_voto", nullable = false, updatable = false)
    private LocalDateTime dataVoto;

    /** Monta o voto a partir da sessão em que foi aceito — {@code pautaId}/{@code sessaoId} vêm dela. */
    public static Voto novo(SessaoVotacao sessao, String associadoId, SimNao opcao) {
        return Voto.builder()
                .pautaId(sessao.getPautaId())
                .sessaoId(sessao.getId())
                .associadoId(associadoId)
                .opcao(opcao)
                .build();
    }
}
