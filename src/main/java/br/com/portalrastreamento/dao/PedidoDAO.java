package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.Pedido;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@RequestScoped
public class PedidoDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    @Transactional
    public void salvar(Pedido pedido) {
        if (pedido.getId() == null) {
            em.persist(pedido);
        } else {
            em.merge(pedido);
        }
    }

    @Transactional
    public void excluir(Pedido pedido) {
        pedido = em.merge(pedido);
        em.remove(pedido);
    }

    public List<Pedido> listarTodos() {
        return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
    }
}