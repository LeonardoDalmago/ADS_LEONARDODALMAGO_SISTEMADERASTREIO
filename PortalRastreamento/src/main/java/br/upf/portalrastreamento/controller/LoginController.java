package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.UsuarioEntity;
import br.upf.portalrastreamento.enumeration.PerfilUsuario;
import br.upf.portalrastreamento.util.FacesUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String ATRIBUTO_SESSAO = "usuarioLogado";

    private final GenericDAO<UsuarioEntity> dao = new GenericDAO<>(UsuarioEntity.class);

    private String login;
    private String senha;
    private UsuarioEntity usuarioLogado;

    public String entrar() {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login == null ? "" : login.trim());
        params.put("senha", senha == null ? "" : senha);
        List<UsuarioEntity> encontrados = dao.consultar(
                "SELECT u FROM UsuarioEntity u WHERE u.login = :login AND u.senha = :senha", params);

        if (encontrados.isEmpty()) {
            FacesUtil.erro("Login ou senha inválidos.");
            return null;
        }
        usuarioLogado = encontrados.get(0);
        senha = null;
        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put(ATRIBUTO_SESSAO, usuarioLogado);
        return "/app/index.xhtml?faces-redirect=true";
    }

    public String sair() {
        ExternalContext externo = FacesContext.getCurrentInstance().getExternalContext();
        externo.invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isAdmin() {
        return usuarioLogado != null && usuarioLogado.getPerfil() == PerfilUsuario.ADMIN;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }
}
