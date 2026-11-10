package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;

@RequestScoped
public class UsuarioDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    public Usuario autenticar(String login, String senha) {
        try {
            return em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class)
                .setParameter("login", login)
                .setParameter("senha", senha)
                .getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }
}