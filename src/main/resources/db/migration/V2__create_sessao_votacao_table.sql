CREATE TABLE sessao_votacao (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    pauta_id BIGINT NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP NOT NULL,
    CONSTRAINT fk_sessao_votacao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta (id),
    CONSTRAINT uk_sessao_votacao_pauta UNIQUE (pauta_id),
    CONSTRAINT uk_sessao_votacao_codigo UNIQUE (codigo),
    CONSTRAINT ck_sessao_votacao_janela CHECK (data_fechamento > data_abertura)
);
