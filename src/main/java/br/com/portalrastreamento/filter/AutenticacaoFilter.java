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

@WebFilter(filterName = "AutenticacaoFilter", urlPatterns = {"/index.xhtml", "/pedidos.xhtml"})
public class AutenticacaoFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        if (uri.contains("login.xhtml")) {
            chain.doFilter(request, response);
            return;
        }

        if (loginBean == null || !loginBean.isAutenticado()) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}