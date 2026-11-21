package br.com.portalrastreamento.filter;

import br.com.portalrastreamento.bean.LoginBean;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro administrativo: só o perfil Administrador entra nas telas de cadastro.
 * Sem login volta para a tela de login; logado sem ser administrador volta para o início.
 * Para proteger outra tela, acrescente o endereço em urlPatterns.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/pedidos.xhtml", "/transportadoras.xhtml", "/usuarios.xhtml"})
public class AdminFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!loginBean.isAutenticado()) {
            resp.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        if (!loginBean.isAdministrador()) {
            resp.sendRedirect(req.getContextPath() + "/index.xhtml?acessoNegado=true");
            return;
        }

        chain.doFilter(request, response);
    }
}
