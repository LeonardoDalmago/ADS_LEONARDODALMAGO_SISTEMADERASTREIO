package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.entity.ClienteEntity;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ClienteController extends CrudController<ClienteEntity> {

    private static final long serialVersionUID = 1L;

    public ClienteController() {
        super(ClienteEntity.class);
    }

    @Override
    protected ClienteEntity novaInstancia() {
        return new ClienteEntity();
    }

    @Override
    protected String[] camposBusca() {
        return new String[]{"razaoSocial", "cnpj"};
    }

    @Override
    protected void validar(ClienteEntity c) {
        if (vazio(c.getRazaoSocial())) {
            throw new IllegalArgumentException("Informe a razão social.");
        }
        if (c.getCnpj() == null || c.getCnpj().length() != 14) {
            throw new IllegalArgumentException("O CNPJ deve ter 14 números.");
        }
    }
}
