CREATE TABLE sessao_votacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pauta_id UUID NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP NOT NULL,
    CONSTRAINT fk_sessao_votacao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta (id),
    CONSTRAINT uk_sessao_votacao_pauta UNIQUE (pauta_id),
    CONSTRAINT ck_sessao_votacao_janela CHECK (data_fechamento > data_abertura)
);
