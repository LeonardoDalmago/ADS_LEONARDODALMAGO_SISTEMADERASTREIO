package br.com.portalrastreamento.dao;

import br.com.portalrastreamento.model.PerfilUsuario;
import br.com.portalrastreamento.model.Usuario;
import br.com.portalrastreamento.util.SenhaUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@RequestScoped
public class UsuarioDAO implements Serializable {

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    @Transactional
    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    @Transactional
    public void excluir(Usuario usuario) {
        Usuario gerenciado = em.find(Usuario.class, usuario.getId());
        if (gerenciado != null) {
            em.remove(gerenciado);
        }
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u ORDER BY u.nome", Usuario.class).getResultList();
    }

    /** Busca pelo campo nome (parte do nome, sem diferenciar maiúsculas). */
    public List<Usuario> buscarPorNome(String termo) {
        return em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE :termo ORDER BY u.nome", Usuario.class)
                .setParameter("termo", "%" + termo.trim().toLowerCase() + "%")
                .getResultList();
    }

    public Usuario buscarPorLogin(String login) {
        List<Usuario> achados = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.login) = :login", Usuario.class)
                .setParameter("login", login.trim().toLowerCase())
                .setMaxResults(1)
                .getResultList();
        return achados.isEmpty() ? null : achados.get(0);
    }

    /** Já existe outro usuário com esse login? (idIgnorar = usuário em edição, ou null) */
    public boolean existeLogin(String login, Long idIgnorar) {
        String jpql = "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.login) = :login";
        if (idIgnorar != null) {
            jpql += " AND u.id <> :id";
        }
        TypedQuery<Long> consulta = em.createQuery(jpql, Long.class)
                .setParameter("login", login.trim().toLowerCase());
        if (idIgnorar != null) {
            consulta.setParameter("id", idIgnorar);
        }
        return consulta.getSingleResult() > 0;
    }

    public long contarAdministradoresAtivos() {
        return em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.perfil = :perfil AND u.ativo = true", Long.class)
                .setParameter("perfil", PerfilUsuario.ADMINISTRADOR)
                .getSingleResult();
    }

    /** Devolve o usuário se login e senha conferem e a conta está ativa; senão, null. */
    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null) {
            return null;
        }
        Usuario usuario = buscarPorLogin(login);
        if (usuario == null || !usuario.isAtivo()) {
            return null;
        }
        return SenhaUtil.confere(usuario.getSalt(), senha, usuario.getSenhaHash()) ? usuario : null;
    }
}
