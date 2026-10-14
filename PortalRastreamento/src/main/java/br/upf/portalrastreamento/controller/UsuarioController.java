package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.entity.UsuarioEntity;
import br.upf.portalrastreamento.enumeration.PerfilUsuario;
import br.upf.portalrastreamento.util.FacesUtil;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class UsuarioController extends CrudController<UsuarioEntity> {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginController loginController;

    public UsuarioController() {
        super(UsuarioEntity.class);
    }

    @Override
    protected UsuarioEntity novaInstancia() {
        UsuarioEntity u = new UsuarioEntity();
        u.setPerfil(PerfilUsuario.COMUM);
        return u;
    }

    @Override
    protected String[] camposBusca() {
        return new String[]{"nome"};
    }

    @Override
    protected void validar(UsuarioEntity u) {
        if (vazio(u.getNome())) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        if (vazio(u.getLogin())) {
            throw new IllegalArgumentException("Informe o login.");
        }
        if (vazio(u.getSenha())) {
            throw new IllegalArgumentException("Informe a senha.");
        }
        if (u.getPerfil() == null) {
            throw new IllegalArgumentException("Escolha o perfil.");
        }
    }

    @Override
    public void excluir(UsuarioEntity item) {
        UsuarioEntity logado = loginController.getUsuarioLogado();
        if (logado != null && logado.getId().equals(item.getId())) {
            FacesUtil.erro("Você não pode excluir o usuário que está logado.");
            return;
        }
        super.excluir(item);
    }
    public PerfilUsuario[] getPerfis() {
        return PerfilUsuario.values();
    }
}
