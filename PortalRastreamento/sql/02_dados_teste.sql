INSERT INTO usuario (nome, login, senha, perfil)
VALUES ('Funcionario Teste', 'func', 'func123', 'COMUM');

INSERT INTO cliente (razao_social, cnpj) VALUES ('Industria Exemplo Ltda', '11222333000181');
INSERT INTO transportadora (nome, telefone) VALUES ('Transportes Rapidos', '(54) 3333-0000');

INSERT INTO pedido (numero_pedido, descricao, status, data_criacao, previsao_entrega, cliente_id, transportadora_id)
VALUES ('PED-0001', '100 pallets plasticos', 'EM_TRANSPORTE', NOW(), CURRENT_DATE + 5, 1, 1);

INSERT INTO historico_pedido (pedido_id, data, descricao) VALUES
(1, NOW() - INTERVAL '3 days', 'Pedido registrado com status: Em producao'),
(1, NOW() - INTERVAL '1 day',  'Producao finalizada'),
(1, NOW(),                     'Pedido saiu para entrega');
