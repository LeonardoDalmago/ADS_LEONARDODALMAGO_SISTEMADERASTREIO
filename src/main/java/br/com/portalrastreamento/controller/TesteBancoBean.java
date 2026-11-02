package br.com.portalrastreamento.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Named
@RequestScoped
public class TesteBancoBean {

    private String resultado = "Ainda nao testado";

    public void testar() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("rastreamentoPU");
            em = emf.createEntityManager();
            Object versao = em.createNativeQuery("SELECT version()").getSingleResult();
            resultado = "Conectado: " + versao;
        } catch (Exception e) {
            resultado = "Erro: " + e.getMessage();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }

    public String getResultado() {
        return resultado;
    }
}