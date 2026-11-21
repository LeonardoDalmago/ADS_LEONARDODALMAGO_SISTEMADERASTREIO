package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.Transportadora;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class TransportadoraDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    @Transactional
    public void salvar(Transportadora transportadora) {
        if (transportadora.getId() == null) {
            em.persist(transportadora);
        } else {
            em.merge(transportadora);
        }
    }

    @Transactional
    public void excluir(Transportadora transportadora) {
        Transportadora gerenciada = em.find(Transportadora.class, transportadora.getId());
        if (gerenciada != null) {
            em.remove(gerenciada);
        }
    }

    public Transportadora buscarPorId(Long id) {
        return em.find(Transportadora.class, id);
    }

    public List<Transportadora> listarTodos() {
        return em.createQuery("SELECT t FROM Transportadora t ORDER BY t.nome", Transportadora.class)
                .getResultList();
    }

    /** Busca pelo campo nome (parte do nome, sem diferenciar maiúsculas). */
    public List<Transportadora> buscarPorNome(String termo) {
        return em.createQuery(
                "SELECT t FROM Transportadora t WHERE LOWER(t.nome) LIKE :termo ORDER BY t.nome", Transportadora.class)
                .setParameter("termo", "%" + termo.trim().toLowerCase() + "%")
                .getResultList();
    }

    public long contarPedidos(Long idTransportadora) {
        return em.createQuery(
                "SELECT COUNT(p) FROM Pedido p WHERE p.transportadora.id = :id", Long.class)
                .setParameter("id", idTransportadora)
                .getSingleResult();
    }

    /** Quantidade de pedidos de cada transportadora, em uma consulta só (id -> total). */
    public Map<Long, Long> contarPedidosPorTransportadora() {
        Map<Long, Long> resultado = new HashMap<>();
        List<Object[]> linhas = em.createQuery(
                "SELECT p.transportadora.id, COUNT(p) FROM Pedido p WHERE p.transportadora IS NOT NULL "
                + "GROUP BY p.transportadora.id", Object[].class)
                .getResultList();
        for (Object[] linha : linhas) {
            resultado.put((Long) linha[0], (Long) linha[1]);
        }
        return resultado;
    }
}
