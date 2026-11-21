package br.com.portalrastreamento.config;

import br.com.portalrastreamento.model.PerfilUsuario;
import br.com.portalrastreamento.model.Usuario;
import br.com.portalrastreamento.util.SenhaUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ao iniciar a aplicação, se ainda não existe nenhum usuário, cria o administrador padrão
 * (login "tecnotri", senha "1234"). Troque a senha em Usuários depois do primeiro acesso.
 * Se a tabela ainda não existir, só registra um aviso e a aplicação sobe normalmente.
 */
@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class AdminInicial {

    private static final Logger LOG = Logger.getLogger(AdminInicial.class.getName());

    @PersistenceContext(unitName = "PortalRastreamentoPU")
    private EntityManager em;

    @Resource
    private UserTransaction transacao;

    @PostConstruct
    public void criarAdministradorPadrao() {
        try {
            transacao.begin();
            Long total = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            if (total == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setLogin("tecnotri");
                admin.setSalt(SenhaUtil.novoSalt());
                admin.setSenhaHash(SenhaUtil.hash(admin.getSalt(), "1234"));
                admin.setPerfil(PerfilUsuario.ADMINISTRADOR);
                admin.setAtivo(true);
                em.persist(admin);
                LOG.info("Administrador padrão criado: login 'tecnotri'. Troque a senha na tela de Usuários.");
            }
            transacao.commit();
        } catch (Exception e) {
            try {
                transacao.rollback();
            } catch (Exception ignorada) {
                // não havia transação ativa
            }
            LOG.log(Level.WARNING,
                    "Não foi possível verificar o administrador padrão. A tabela usuarios_sistema existe?", e);
        }
    }
}
