package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.RastreioDAO;
import br.com.portalrastreamento.model.Pedido;
import br.com.portalrastreamento.model.StatusPedido;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class RastreioConvidadoBean implements Serializable {

    // Etapas na ordem do Enum StatusPedido
    private static final List<String> ETAPAS = Arrays.stream(StatusPedido.values())
            .map(StatusPedido::getDescricao)
            .toList();

    @Inject
    private RastreioDAO rastreioDAO;

    private String codigo;
    private Pedido pedido;

    public void rastrear() {
        pedido = null;

        if (codigo == null || codigo.isBlank()) {
            mensagem(FacesMessage.SEVERITY_WARN, "Informe o código de rastreio.");
            return;
        }

        codigo = codigo.trim().toUpperCase();
        try {
            pedido = rastreioDAO.buscarPorCodigo(codigo);
            if (pedido == null) {
                mensagem(FacesMessage.SEVERITY_WARN,
                        "Nenhum pedido encontrado com o código " + codigo + ". Confira e tente de novo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensagem(FacesMessage.SEVERITY_ERROR, "Não foi possível consultar o pedido agora. Tente novamente.");
        }
    }

    /** Posição do status atual na lista de etapas (-1 se o pedido não tiver status). */
    public int getIndiceStatus() {
        if (pedido == null || pedido.getStatus() == null) {
            return -1;
        }
        return pedido.getStatus().ordinal();
    }

    /** Esconde o nome do cliente (ex.: "Maria Souza" vira "M***** S*****"). */
    public String getClienteMascarado() {
        if (pedido == null || pedido.getCliente() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String parte : pedido.getCliente().trim().split("\\s+")) {
            if (parte.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(parte.charAt(0)).append("*****");
        }
        return sb.toString();
    }

    private void mensagem(FacesMessage.Severity tipo, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, texto, null));
    }

    public List<String> getEtapas() { return ETAPAS; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Pedido getPedido() { return pedido; }
}
