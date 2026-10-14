package br.upf.portalrastreamento.filter;

import br.upf.portalrastreamento.controller.LoginController;
import br.upf.portalrastreamento.entity.UsuarioEntity;
import br.upf.portalrastreamento.enumeration.PerfilUsuario;
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
@WebFilter(urlPatterns = "/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession sessao = req.getSession(false);

        UsuarioEntity usuario = (sessao == null)
                ? null : (UsuarioEntity) sessao.getAttribute(LoginController.ATRIBUTO_SESSAO);

        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else if (usuario.getPerfil() != PerfilUsuario.ADMIN) {
            res.sendRedirect(req.getContextPath() + "/app/index.xhtml?negado=true");
        } else {
            res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            chain.doFilter(request, response);
        }
    }
}
