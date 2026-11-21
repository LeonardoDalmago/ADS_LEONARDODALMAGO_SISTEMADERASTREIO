package br.com.portalrastreamento.model;

/**
 * Perfil de acesso: o administrador cadastra pedidos, transportadoras e usuários;
 * o operador só acompanha o painel inicial.
 */
public enum PerfilUsuario {

    ADMINISTRADOR("Administrador", "warning"),
    OPERADOR("Operador", "info");

    private final String descricao;
    private final String severidade;

    PerfilUsuario(String descricao, String severidade) {
        this.descricao = descricao;
        this.severidade = severidade;
    }

    public String getDescricao() { return descricao; }
    public String getSeveridade() { return severidade; }
}
