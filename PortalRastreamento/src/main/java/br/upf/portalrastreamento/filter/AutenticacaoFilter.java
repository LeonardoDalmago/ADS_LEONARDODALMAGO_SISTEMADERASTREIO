package br.upf.portalrastreamento.filter;

import br.upf.portalrastreamento.controller.LoginController;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebFilter(urlPatterns = "/app/*")
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession sessao = req.getSession(false);

        boolean logado = sessao != null && sessao.getAttribute(LoginController.ATRIBUTO_SESSAO) != null;
        if (!logado) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }
        // Evita que o botao "voltar" mostre paginas guardadas depois do logout
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        chain.doFilter(request, response);
    }
}
