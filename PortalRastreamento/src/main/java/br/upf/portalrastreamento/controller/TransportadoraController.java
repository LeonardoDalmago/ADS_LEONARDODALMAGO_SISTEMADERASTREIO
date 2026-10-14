package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.entity.TransportadoraEntity;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TransportadoraController extends CrudController<TransportadoraEntity> {

    private static final long serialVersionUID = 1L;

    public TransportadoraController() {
        super(TransportadoraEntity.class);
    }

    @Override
    protected TransportadoraEntity novaInstancia() {
        return new TransportadoraEntity();
    }

    @Override
    protected String[] camposBusca() {
        return new String[]{"nome"};
    }

    @Override
    protected void validar(TransportadoraEntity t) {
        if (vazio(t.getNome())) {
            throw new IllegalArgumentException("Informe o nome da transportadora.");
        }
    }
}
