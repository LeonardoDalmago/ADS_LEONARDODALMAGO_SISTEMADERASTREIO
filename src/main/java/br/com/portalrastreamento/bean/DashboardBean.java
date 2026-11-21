package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.PedidoDAO;
import br.com.portalrastreamento.model.Pedido;
import br.com.portalrastreamento.model.StatusPedido;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dados do dashboard (tela inicial): contagem por status e últimos pedidos.
 */
@Named
@ViewScoped
public class DashboardBean implements Serializable {

    @Inject
    private PedidoDAO pedidoDAO;

    private final Map<StatusPedido, Long> contagem = new EnumMap<>(StatusPedido.class);
    private List<Pedido> recentes = new ArrayList<>();
    private long total;

    @PostConstruct
    public void init() {
        carregar();
    }

    public void carregar() {
        List<Pedido> todos = pedidoDAO.listarTodos();
        total = todos.size();

        contagem.clear();
        for (Pedido p : todos) {
            if (p.getStatus() != null) {
                contagem.merge(p.getStatus(), 1L, Long::sum);
            }
        }

        recentes = todos.stream()
                .sorted(Comparator.comparing(Pedido::getDataAtualizacao,
                        Comparator.nullsLast(Comparator.<Date>reverseOrder())))
                .limit(8)
                .collect(Collectors.toList());
    }

    public long quantidade(StatusPedido status) {
        return contagem.getOrDefault(status, 0L);
    }

    public int percentual(StatusPedido status) {
        return total == 0 ? 0 : (int) Math.round(quantidade(status) * 100.0 / total);
    }

    public List<StatusPedido> getStatusLista() { return List.of(StatusPedido.values()); }
    public long getTotal() { return total; }
    public List<Pedido> getRecentes() { return recentes; }
}
