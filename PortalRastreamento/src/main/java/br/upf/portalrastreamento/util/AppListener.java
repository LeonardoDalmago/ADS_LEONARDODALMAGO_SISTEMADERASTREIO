package br.upf.portalrastreamento.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.fechar();
    }
}
