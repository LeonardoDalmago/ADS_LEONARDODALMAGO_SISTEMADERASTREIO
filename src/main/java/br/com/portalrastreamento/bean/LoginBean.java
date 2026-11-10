package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.UsuarioDAO;
import br.com.portalrastreamento.model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String login;
    private String senha;
    private Usuario usuarioLogado;

    @Inject
    private UsuarioDAO usuarioDAO;

    public String entrar() {
        Usuario usuario = usuarioDAO.autenticar(login, senha);

        if (usuario != null) {
            this.usuarioLogado = usuario;
            return "index?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso Negado", "Login ou senha incorretos."));
        return null;
    }

    public String deslogar() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    public boolean isAutenticado() {
        return usuarioLogado != null;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
}