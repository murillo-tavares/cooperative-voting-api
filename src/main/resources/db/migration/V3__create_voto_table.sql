CREATE TABLE voto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pauta_id UUID NOT NULL,
    sessao_id UUID NOT NULL,
    associado_id VARCHAR(20) NOT NULL,
    opcao VARCHAR(10) NOT NULL,
    data_voto TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES pauta (id),
    CONSTRAINT fk_voto_sessao FOREIGN KEY (sessao_id) REFERENCES sessao_votacao (id),
    CONSTRAINT uk_voto_pauta_associado UNIQUE (pauta_id, associado_id)
);

-- pauta_id já é coberto pelo índice da unique acima; sessao_id não tem índice nenhum sem isso.
CREATE INDEX idx_voto_sessao ON voto (sessao_id);
