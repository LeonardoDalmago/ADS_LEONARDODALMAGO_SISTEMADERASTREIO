package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.ClienteEntity;
import br.upf.portalrastreamento.entity.PedidoEntity;
import br.upf.portalrastreamento.entity.TransportadoraEntity;
import br.upf.portalrastreamento.enumeration.StatusPedido;
import br.upf.portalrastreamento.util.FacesUtil;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class PedidoController extends CrudController<PedidoEntity> {

    private static final long serialVersionUID = 1L;

    private final GenericDAO<ClienteEntity> clienteDao = new GenericDAO<>(ClienteEntity.class);
    private final GenericDAO<TransportadoraEntity> transportadoraDao = new GenericDAO<>(TransportadoraEntity.class);

    private List<ClienteEntity> clientes;
    private List<TransportadoraEntity> transportadoras;
    private StatusPedido statusFiltro;
    private String novoHistorico;

    public PedidoController() {
        super(PedidoEntity.class);
    }

    @Override
    protected PedidoEntity novaInstancia() {
        PedidoEntity p = new PedidoEntity();
        p.setStatus(StatusPedido.EM_PRODUCAO);
        return p;
    }

    @Override
    protected String[] camposBusca() {
        return new String[]{"numeroPedido", "descricao"};
    }

    @Override
    public void novo() {
        super.novo();
        novoHistorico = null;
        carregarCombos();
    }

    @Override
    public void editar(PedidoEntity item) {
        super.editar(item);
        novoHistorico = null;
        carregarCombos();
    }

    private void carregarCombos() {
        clientes = clienteDao.listarTodos();
        transportadoras = transportadoraDao.listarTodos();
    }
    @Override
    public void buscar() {
        List<String> condicoes = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        if (!vazio(termoBusca)) {
            condicoes.add("(LOWER(p.numeroPedido) LIKE :termo OR LOWER(p.descricao) LIKE :termo "
                    + "OR LOWER(p.cliente.razaoSocial) LIKE :termo)");
            params.put("termo", "%" + termoBusca.trim().toLowerCase() + "%");
        }
        if (statusFiltro != null) {
            condicoes.add("p.status = :status");
            params.put("status", statusFiltro);
        }

        String jpql = "SELECT p FROM PedidoEntity p"
                + (condicoes.isEmpty() ? "" : " WHERE " + String.join(" AND ", condicoes))
                + " ORDER BY p.dataCriacao DESC";
        lista = dao.consultar(jpql, params);
    }

    @Override
    public void listar() {
        buscar();
    }

    @Override
    public void limpar() {
        statusFiltro = null;
        super.limpar();
    }

    @Override
    protected void validar(PedidoEntity p) {
        if (vazio(p.getNumeroPedido())) {
            throw new IllegalArgumentException("Informe o número do pedido.");
        }
        if (vazio(p.getDescricao())) {
            throw new IllegalArgumentException("Informe a descrição do pedido.");
        }
        if (p.getCliente() == null) {
            throw new IllegalArgumentException("Escolha o cliente.");
        }
        if (p.getStatus() == null) {
            throw new IllegalArgumentException("Escolha o status.");
        }
    }

    @Override
    protected void antesDeSalvar(PedidoEntity p) {
        if (p.getId() == null) {
            p.setDataCriacao(LocalDateTime.now());
            p.adicionarHistorico("Pedido registrado com status: " + p.getStatus().getDescricao());
        } else {
            PedidoEntity atual = dao.buscarPorId(p.getId());
            if (atual != null && atual.getStatus() != p.getStatus()) {
                p.adicionarHistorico("Status alterado para: " + p.getStatus().getDescricao());
            }
        }
    }
    public void adicionarHistorico() {
        if (selecionado == null || selecionado.getId() == null) {
            FacesUtil.aviso("Salve o pedido antes de adicionar o histórico.");
            return;
        }
        if (vazio(novoHistorico)) {
            FacesUtil.aviso("Digite a descrição da movimentação.");
            return;
        }
        try {
            selecionado.adicionarHistorico(novoHistorico.trim());
            selecionado = dao.salvar(selecionado);
            novoHistorico = null;
            buscar();
            FacesUtil.info("Movimentação registrada.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.erro("Não foi possível registrar a movimentação.");
        }
    }

    public StatusPedido[] getStatusLista() {
        return StatusPedido.values();
    }

    public List<ClienteEntity> getClientes() {
        return clientes;
    }

    public List<TransportadoraEntity> getTransportadoras() {
        return transportadoras;
    }

    public StatusPedido getStatusFiltro() {
        return statusFiltro;
    }

    public void setStatusFiltro(StatusPedido statusFiltro) {
        this.statusFiltro = statusFiltro;
    }

    public String getNovoHistorico() {
        return novoHistorico;
    }

    public void setNovoHistorico(String novoHistorico) {
        this.novoHistorico = novoHistorico;
    }
}
