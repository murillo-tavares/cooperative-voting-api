package br.com.cooperativevoting.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sessão de votação de uma pauta, com janela de tempo em que os votos são aceitos.
 */
@Entity
@Table(name = "sessao_votacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao {

    /** UUID gerado em memória (não sequencial, não adivinhável) — é o próprio identificador público. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Sem {@code @ManyToOne}/{@code @OneToOne} para {@link Pauta}: usar o id evita o custo de
     * mapear/hidratar a entidade relacionada (proxy, lazy loading, N+1) quando só o identificador
     * é necessário. A constraint {@code UNIQUE(pauta_id)} no banco garante uma única sessão por pauta.
     */
    @Column(name = "pauta_id", nullable = false, updatable = false)
    private UUID pautaId;

    @Column(name = "data_abertura", nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    /** {@code CHECK(data_fechamento > data_abertura)} no banco garante que a janela persistida nunca seja inválida. */
    @Column(name = "data_fechamento", nullable = false, updatable = false)
    private LocalDateTime dataFechamento;

    /**
     * Não é coluna, calculado uma vez (na carga ou na criação) e cacheado aqui, pra
     * getStatus() ser imutável dentro do ciclo de vida do objeto — chamadas
     * repetidas no mesmo objeto sempre retornam o mesmo valor, mesmo que o tempo passe.
     */
    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Status status;

    /**
     * Validação fica em memória, não no banco: uma restrição no banco (ex.: CHECK contra o
     * horário atual) rejeitaria operações iniciadas dentro da janela válida mas que terminam
     * depois dela por lentidão/delay.
     */
    @PostLoad
    @PostPersist
    private void calcularStatus() {
        status = LocalDateTime.now().isBefore(dataFechamento) ? Status.ABERTA : Status.ENCERRADA;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        ABERTA,
        ENCERRADA
    }
}
