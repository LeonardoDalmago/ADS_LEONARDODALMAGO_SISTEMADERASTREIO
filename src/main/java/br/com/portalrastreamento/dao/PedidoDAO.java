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
        Pedido gerenciado = em.find(Pedido.class, pedido.getId());
        if (gerenciado != null) {
            em.remove(gerenciado);
        }
    }

    public List<Pedido> listarTodos() {
        return em.createQuery("SELECT p FROM Pedido p ORDER BY p.id DESC", Pedido.class).getResultList();
    }

    /** Busca pelo campo código de rastreio (parte do código, sem diferenciar maiúsculas). */
    public List<Pedido> buscarPorCodigo(String termo) {
        return em.createQuery(
                "SELECT p FROM Pedido p WHERE LOWER(p.codigoRastreio) LIKE :termo ORDER BY p.id DESC", Pedido.class)
                .setParameter("termo", "%" + termo.trim().toLowerCase() + "%")
                .getResultList();
    }

    /** Já existe outro pedido com esse código? (idIgnorar = pedido que está sendo editado, ou null) */
    public boolean existeCodigo(String codigo, Long idIgnorar) {
        String jpql = "SELECT COUNT(p) FROM Pedido p WHERE UPPER(p.codigoRastreio) = :codigo";
        if (idIgnorar != null) {
            jpql += " AND p.id <> :id";
        }
        jakarta.persistence.TypedQuery<Long> consulta = em.createQuery(jpql, Long.class)
                .setParameter("codigo", codigo.trim().toUpperCase());
        if (idIgnorar != null) {
            consulta.setParameter("id", idIgnorar);
        }
        return consulta.getSingleResult() > 0;
    }
}
