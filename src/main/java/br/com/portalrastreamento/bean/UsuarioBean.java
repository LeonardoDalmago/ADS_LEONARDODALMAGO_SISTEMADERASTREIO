package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.UsuarioDAO;
import br.com.portalrastreamento.model.PerfilUsuario;
import br.com.portalrastreamento.model.Usuario;
import br.com.portalrastreamento.util.SenhaUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * CRUD de usuários (somente administrador, protegido pelo AdminFilter).
 */
@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    private Usuario usuario;
    private List<Usuario> usuarios;
    private String termoBusca;
    private String novaSenha;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        carregar();
    }

    private void carregar() {
        if (termoBusca == null || termoBusca.isBlank()) {
            usuarios = usuarioDAO.listarTodos();
        } else {
            usuarios = usuarioDAO.buscarPorNome(termoBusca);
        }
    }

    public void buscar() {
        carregar();
    }

    public void limparBusca() {
        termoBusca = null;
        carregar();
    }

    public void prepararNovo() {
        usuario = new Usuario();
        usuario.setPerfil(PerfilUsuario.OPERADOR);
        usuario.setAtivo(true);
        novaSenha = null;
    }

    public void prepararEdicao(Usuario u) {
        // trabalha em uma cópia: se o usuário cancelar, a lista não muda
        Usuario copia = new Usuario();
        copia.setId(u.getId());
        copia.setNome(u.getNome());
        copia.setLogin(u.getLogin());
        copia.setSalt(u.getSalt());
        copia.setSenhaHash(u.getSenhaHash());
        copia.setPerfil(u.getPerfil());
        copia.setAtivo(u.isAtivo());
        usuario = copia;
        novaSenha = null;
    }

    public void salvar() {
        usuario.setNome(usuario.getNome().trim());
        usuario.setLogin(usuario.getLogin().trim().toLowerCase());

        if (usuarioDAO.existeLogin(usuario.getLogin(), usuario.getId())) {
            erro("Já existe um usuário com o login " + usuario.getLogin() + ".");
            return;
        }

        boolean novo = usuario.getId() == null;
        boolean trocarSenha = novaSenha != null && !novaSenha.isBlank();

        if (novo && !trocarSenha) {
            erro("Informe a senha do novo usuário.");
            return;
        }
        if (trocarSenha && novaSenha.length() < 4) {
            erro("A senha precisa ter pelo menos 4 caracteres.");
            return;
        }

        // o último administrador ativo não pode perder o perfil nem ser desativado
        if (!novo) {
            Usuario noBanco = usuarioDAO.buscarPorId(usuario.getId());
            boolean eraAdminAtivo = noBanco != null && noBanco.isAtivo()
                    && noBanco.getPerfil() == PerfilUsuario.ADMINISTRADOR;
            boolean continuaAdminAtivo = usuario.isAtivo() && usuario.getPerfil() == PerfilUsuario.ADMINISTRADOR;
            if (eraAdminAtivo && !continuaAdminAtivo && usuarioDAO.contarAdministradoresAtivos() <= 1) {
                erro("Este é o único administrador ativo. Ele não pode virar operador nem ser desativado.");
                return;
            }
        }

        if (trocarSenha) {
            usuario.setSalt(SenhaUtil.novoSalt());
            usuario.setSenhaHash(SenhaUtil.hash(usuario.getSalt(), novaSenha));
        }

        usuarioDAO.salvar(usuario);
        carregar();
        usuario = new Usuario();
        novaSenha = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário salvo com sucesso!"));
    }

    public void excluir(Usuario u) {
        Usuario logado = loginBean.getUsuarioLogado();
        if (logado != null && logado.getId() != null && logado.getId().equals(u.getId())) {
            aviso("Você não pode excluir o usuário com o qual está logado.");
            return;
        }
        if (u.isAtivo() && u.getPerfil() == PerfilUsuario.ADMINISTRADOR
                && usuarioDAO.contarAdministradoresAtivos() <= 1) {
            aviso("Este é o único administrador ativo e não pode ser excluído.");
            return;
        }
        usuarioDAO.excluir(u);
        carregar();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário excluído!"));
    }

    private void erro(String texto) {
        FacesContext contexto = FacesContext.getCurrentInstance();
        contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
        contexto.validationFailed(); // mantém a janela aberta
    }

    private void aviso(String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, texto, null));
    }

    public PerfilUsuario[] getPerfilOpcoes() { return PerfilUsuario.values(); }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public String getTermoBusca() { return termoBusca; }
    public void setTermoBusca(String termoBusca) { this.termoBusca = termoBusca; }
    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
}
