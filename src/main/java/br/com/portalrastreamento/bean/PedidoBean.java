package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.PedidoDAO;
import br.com.portalrastreamento.dao.TransportadoraDAO;
import br.com.portalrastreamento.model.Pedido;
import br.com.portalrastreamento.model.StatusPedido;
import br.com.portalrastreamento.model.Transportadora;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class PedidoBean implements Serializable {

    private Pedido pedido;
    private List<Pedido> pedidos;
    private List<Transportadora> transportadoras;
    private String termoBusca;

    @Inject
    private PedidoDAO pedidoDAO;

    @Inject
    private TransportadoraDAO transportadoraDAO;

    @PostConstruct
    public void init() {
        pedido = new Pedido();
        carregar();
    }

    private void carregar() {
        if (termoBusca == null || termoBusca.isBlank()) {
            pedidos = pedidoDAO.listarTodos();
        } else {
            pedidos = pedidoDAO.buscarPorCodigo(termoBusca);
        }
        transportadoras = transportadoraDAO.listarTodos();
    }

    public void buscar() {
        carregar();
    }

    public void limparBusca() {
        termoBusca = null;
        carregar();
    }

    public void prepararNovo() {
        pedido = new Pedido();
        pedido.setStatus(StatusPedido.EM_PRODUCAO);
        transportadoras = transportadoraDAO.listarTodos();
    }

    public void prepararEdicao(Pedido p) {
        // trabalha em uma cópia: se o usuário cancelar, a lista não muda
        pedido = copiar(p);
        transportadoras = transportadoraDAO.listarTodos();
    }

    public void salvar() {
        String codigo = pedido.getCodigoRastreio() == null ? "" : pedido.getCodigoRastreio().trim().toUpperCase();
        pedido.setCodigoRastreio(codigo);

        if (pedidoDAO.existeCodigo(codigo, pedido.getId())) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Já existe um pedido com o código " + codigo + ".", null));
            contexto.validationFailed(); // mantém a janela aberta
            return;
        }

        pedido.setDataAtualizacao(new Date());
        pedidoDAO.salvar(pedido);
        carregar();
        pedido = new Pedido();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pedido salvo com sucesso!"));
    }

    public void excluir(Pedido p) {
        pedidoDAO.excluir(p);
        carregar();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pedido excluído!"));
    }

    private Pedido copiar(Pedido origem) {
        Pedido copia = new Pedido();
        copia.setId(origem.getId());
        copia.setCodigoRastreio(origem.getCodigoRastreio());
        copia.setProduto(origem.getProduto());
        copia.setCliente(origem.getCliente());
        copia.setStatus(origem.getStatus());
        copia.setDataAtualizacao(origem.getDataAtualizacao());
        copia.setTransportadora(origem.getTransportadora());
        return copia;
    }

    public StatusPedido[] getStatusOpcoes() { return StatusPedido.values(); }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public List<Pedido> getPedidos() { return pedidos; }
    public List<Transportadora> getTransportadoras() { return transportadoras; }
    public String getTermoBusca() { return termoBusca; }
    public void setTermoBusca(String termoBusca) { this.termoBusca = termoBusca; }
}
