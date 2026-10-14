package br.upf.portalrastreamento.converter;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.TransportadoraEntity;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
@FacesConverter(forClass = TransportadoraEntity.class)
public class TransportadoraConverter implements Converter<TransportadoraEntity> {

    private final GenericDAO<TransportadoraEntity> dao = new GenericDAO<>(TransportadoraEntity.class);

    @Override
    public TransportadoraEntity getAsObject(FacesContext context, UIComponent component, String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return dao.buscarPorId(Integer.valueOf(valor));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TransportadoraEntity transportadora) {
        if (transportadora == null || transportadora.getId() == null) {
            return "";
        }
        return String.valueOf(transportadora.getId());
    }
}
