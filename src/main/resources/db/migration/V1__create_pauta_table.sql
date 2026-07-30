CREATE TABLE pauta (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    titulo VARCHAR(120) NOT NULL,
    descricao VARCHAR(500),
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMP,
    data_exclusao TIMESTAMP,
    CONSTRAINT uk_pauta_codigo UNIQUE (codigo)
);

-- Cobre o padrão de acesso mais comum: listar/consultar pautas não excluídas.
-- Índice parcial fica pequeno (só linhas ativas) e já serve pra ordenar por mais recente.
CREATE INDEX idx_pauta_ativa_data_criacao ON pauta (data_criacao) WHERE data_exclusao IS NULL;
