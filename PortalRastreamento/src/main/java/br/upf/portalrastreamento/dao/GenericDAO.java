package br.upf.portalrastreamento.dao;

import br.upf.portalrastreamento.entity.Identificavel;
import br.upf.portalrastreamento.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class GenericDAO<T extends Identificavel> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<T> classe;

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }
    public T salvar(T entidade) {
        EntityManager em = JPAUtil.criarEntityManager();
        try {
            em.getTransaction().begin();
            T salvo = em.merge(entidade);
            em.getTransaction().commit();
            return salvo;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void excluir(T entidade) {
        EntityManager em = JPAUtil.criarEntityManager();
        try {
            em.getTransaction().begin();
            T existente = em.find(classe, entidade.getId());
            if (existente != null) {
                em.remove(existente);
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public T buscarPorId(Integer id) {
        EntityManager em = JPAUtil.criarEntityManager();
        try {
            return em.find(classe, id);
        } finally {
            em.close();
        }
    }

    public List<T> listarTodos() {
        return consultar("SELECT e FROM " + classe.getSimpleName() + " e ORDER BY e.id", new HashMap<>());
    }
    public List<T> buscarPorCampos(String termo, String... campos) {
        List<String> condicoes = new ArrayList<>();
        for (String campo : campos) {
            condicoes.add("LOWER(e." + campo + ") LIKE :termo");
        }
        String jpql = "SELECT e FROM " + classe.getSimpleName() + " e WHERE "
                + String.join(" OR ", condicoes) + " ORDER BY e.id";
        Map<String, Object> params = new HashMap<>();
        params.put("termo", "%" + termo.trim().toLowerCase() + "%");
        return consultar(jpql, params);
    }
    public List<T> consultar(String jpql, Map<String, Object> parametros) {
        EntityManager em = JPAUtil.criarEntityManager();
        try {
            TypedQuery<T> consulta = em.createQuery(jpql, classe);
            parametros.forEach(consulta::setParameter);
            return consulta.getResultList();
        } finally {
            em.close();
        }
    }
}
