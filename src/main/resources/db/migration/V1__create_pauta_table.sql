CREATE TABLE pauta (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(120) NOT NULL,
    descricao VARCHAR(500),
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMP,
    data_exclusao TIMESTAMP
);

-- Cobre o padrão de acesso mais comum: listar/consultar pautas não excluídas.
-- Índice parcial fica pequeno (só linhas ativas) e já serve pra ordenar por mais recente.
CREATE INDEX idx_pauta_ativa_data_criacao ON pauta (data_criacao) WHERE data_exclusao IS NULL;
