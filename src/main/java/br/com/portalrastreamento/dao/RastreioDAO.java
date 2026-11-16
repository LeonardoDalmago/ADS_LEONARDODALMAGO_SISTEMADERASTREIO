package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.Pedido;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Consulta de pedidos para o modo convidado (somente leitura).
 */
@RequestScoped
public class RastreioDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    public Pedido buscarPorCodigo(String codigo) {
        List<Pedido> achados = em
                .createQuery("SELECT p FROM Pedido p WHERE UPPER(p.codigoRastreio) = :codigo", Pedido.class)
                .setParameter("codigo", codigo.trim().toUpperCase())
                .setMaxResults(1)
                .getResultList();
        return achados.isEmpty() ? null : achados.get(0);
    }
}
