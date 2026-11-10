package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.PedidoDAO;
import br.com.portalrastreamento.model.Pedido;
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

    @Inject
    private PedidoDAO pedidoDAO;

    @PostConstruct
    public void init() {
        pedido = new Pedido();
        pedidos = pedidoDAO.listarTodos();
    }

    public void salvar() {
        pedido.setDataAtualizacao(new Date());
        pedidoDAO.salvar(pedido);
        pedidos = pedidoDAO.listarTodos();
        pedido = new Pedido(); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pedido salvo com sucesso!"));
    }

    public void excluir(Pedido p) {
        pedidoDAO.excluir(p);
        pedidos = pedidoDAO.listarTodos();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pedido excluído!"));
    }

    public void prepararEdicao(Pedido p) {
        this.pedido = p;
    }
    
    public void prepararNovo() {
        this.pedido = new Pedido();
    }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public List<Pedido> getPedidos() { return pedidos; }
}