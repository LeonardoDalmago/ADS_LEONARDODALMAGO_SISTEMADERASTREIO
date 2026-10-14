package br.upf.portalrastreamento.converter;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.ClienteEntity;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
@FacesConverter(forClass = ClienteEntity.class)
public class ClienteConverter implements Converter<ClienteEntity> {

    private final GenericDAO<ClienteEntity> dao = new GenericDAO<>(ClienteEntity.class);

    @Override
    public ClienteEntity getAsObject(FacesContext context, UIComponent component, String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return dao.buscarPorId(Integer.valueOf(valor));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ClienteEntity cliente) {
        if (cliente == null || cliente.getId() == null) {
            return "";
        }
        return String.valueOf(cliente.getId());
    }
}
