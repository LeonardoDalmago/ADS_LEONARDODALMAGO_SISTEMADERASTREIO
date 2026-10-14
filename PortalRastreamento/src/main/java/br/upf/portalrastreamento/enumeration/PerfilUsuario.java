package br.upf.portalrastreamento.enumeration;

public enum PerfilUsuario {
    ADMIN("Administrador"),
    COMUM("Funcionário");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
