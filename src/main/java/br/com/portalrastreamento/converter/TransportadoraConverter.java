package br.com.portalrastreamento.converter;

import br.com.portalrastreamento.dao.TransportadoraDAO;
import br.com.portalrastreamento.model.Transportadora;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Faz o select de transportadora funcionar com objetos:
 * mostra o id na tela e busca a transportadora no banco ao enviar o formulário.
 * Uso: converter="#{transportadoraConverter}"
 */
@Named("transportadoraConverter")
@ApplicationScoped
public class TransportadoraConverter implements Converter<Transportadora> {

    @Inject
    private TransportadoraDAO transportadoraDAO;

    @Override
    public Transportadora getAsObject(FacesContext context, UIComponent component, String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return transportadoraDAO.buscarPorId(Long.valueOf(valor));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Transportadora transportadora) {
        if (transportadora == null || transportadora.getId() == null) {
            return "";
        }
        return String.valueOf(transportadora.getId());
    }
}
