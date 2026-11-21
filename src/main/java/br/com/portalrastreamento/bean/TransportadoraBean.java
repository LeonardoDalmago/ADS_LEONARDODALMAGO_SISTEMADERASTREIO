package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.TransportadoraDAO;
import br.com.portalrastreamento.model.Transportadora;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class TransportadoraBean implements Serializable {

    private Transportadora transportadora;
    private List<Transportadora> transportadoras;
    private Map<Long, Long> pedidosPorTransportadora = new HashMap<>();
    private String termoBusca;

    @Inject
    private TransportadoraDAO transportadoraDAO;

    @PostConstruct
    public void init() {
        transportadora = new Transportadora();
        carregar();
    }

    private void carregar() {
        if (termoBusca == null || termoBusca.isBlank()) {
            transportadoras = transportadoraDAO.listarTodos();
        } else {
            transportadoras = transportadoraDAO.buscarPorNome(termoBusca);
        }
        pedidosPorTransportadora = transportadoraDAO.contarPedidosPorTransportadora();
    }

    public void buscar() {
        carregar();
    }

    public void limparBusca() {
        termoBusca = null;
        carregar();
    }

    public void prepararNovo() {
        transportadora = new Transportadora();
    }

    public void prepararEdicao(Transportadora t) {
        // trabalha em uma cópia: se o usuário cancelar, a lista não muda
        Transportadora copia = new Transportadora();
        copia.setId(t.getId());
        copia.setNome(t.getNome());
        copia.setCnpj(t.getCnpj());
        copia.setTelefone(t.getTelefone());
        copia.setEmail(t.getEmail());
        transportadora = copia;
    }

    public void salvar() {
        transportadora.setNome(transportadora.getNome().trim());
        transportadoraDAO.salvar(transportadora);
        carregar();
        transportadora = new Transportadora();
        mensagem(FacesMessage.SEVERITY_INFO, "Transportadora salva com sucesso!");
    }

    public void excluir(Transportadora t) {
        long vinculados = transportadoraDAO.contarPedidos(t.getId());
        if (vinculados > 0) {
            mensagem(FacesMessage.SEVERITY_WARN, "A transportadora " + t.getNome() + " tem " + vinculados
                    + " pedido(s) vinculado(s). Troque a transportadora desses pedidos antes de excluir.");
            return;
        }
        transportadoraDAO.excluir(t);
        carregar();
        mensagem(FacesMessage.SEVERITY_INFO, "Transportadora excluída!");
    }

    /** Quantidade de pedidos dessa transportadora (usada na tabela). */
    public long quantidadePedidos(Transportadora t) {
        return pedidosPorTransportadora.getOrDefault(t.getId(), 0L);
    }

    private void mensagem(FacesMessage.Severity tipo, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, texto, null));
    }

    public Transportadora getTransportadora() { return transportadora; }
    public void setTransportadora(Transportadora transportadora) { this.transportadora = transportadora; }
    public List<Transportadora> getTransportadoras() { return transportadoras; }
    public String getTermoBusca() { return termoBusca; }
    public void setTermoBusca(String termoBusca) { this.termoBusca = termoBusca; }
}
