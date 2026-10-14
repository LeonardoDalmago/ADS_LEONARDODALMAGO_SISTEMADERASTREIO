CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    perfil TEXT NOT NULL
);

CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE
);

CREATE TABLE transportadora (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20)
);

CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    numero_pedido VARCHAR(30) NOT NULL UNIQUE,
    descricao VARCHAR(255) NOT NULL,
    status TEXT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    previsao_entrega DATE,
    observacoes TEXT,
    cliente_id INTEGER NOT NULL REFERENCES cliente(id),
    transportadora_id INTEGER REFERENCES transportadora(id)
);

CREATE TABLE historico_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    data TIMESTAMP NOT NULL,
    descricao VARCHAR(255) NOT NULL
);

INSERT INTO usuario (nome, login, senha, perfil)
VALUES ('Administrador', 'admin', 'admin123', 'ADMIN');
