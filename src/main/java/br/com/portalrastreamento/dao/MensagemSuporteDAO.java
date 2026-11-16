package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.MensagemSuporte;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;

@RequestScoped
public class MensagemSuporteDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    @Transactional
    public void salvar(MensagemSuporte mensagem) {
        em.persist(mensagem);
    }
}
