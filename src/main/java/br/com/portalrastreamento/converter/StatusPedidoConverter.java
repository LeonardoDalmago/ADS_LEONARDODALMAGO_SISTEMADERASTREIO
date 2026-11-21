package br.com.portalrastreamento.converter;

import br.com.portalrastreamento.model.StatusPedido;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Grava o status como nome do Enum e lê tanto o formato novo ("EM_TRANSITO")
 * quanto o antigo ("Em Trânsito"). Assim, dados antigos não derrubam a tela.
 */
@Converter
public class StatusPedidoConverter implements AttributeConverter<StatusPedido, String> {

    @Override
    public String convertToDatabaseColumn(StatusPedido status) {
        return status == null ? null : status.name();
    }

    @Override
    public StatusPedido convertToEntityAttribute(String valor) {
        return StatusPedido.de(valor);
    }
}
