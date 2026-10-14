package br.upf.portalrastreamento.enumeration;

public enum StatusPedido {
    EM_PRODUCAO("Em produção"),
    EM_TRANSPORTE("Em transporte"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
