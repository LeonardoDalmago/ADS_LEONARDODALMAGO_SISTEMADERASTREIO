package br.upf.portalrastreamento.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public final class JPAUtil {

    private static final EntityManagerFactory FACTORY
            = Persistence.createEntityManagerFactory("PortalRastreamentoPU");

    private JPAUtil() {
    }

    public static EntityManager criarEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void fechar() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
