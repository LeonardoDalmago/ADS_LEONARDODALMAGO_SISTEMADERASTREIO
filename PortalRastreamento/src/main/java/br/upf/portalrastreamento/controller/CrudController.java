package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.Identificavel;
import br.upf.portalrastreamento.util.FacesUtil;
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import org.primefaces.PrimeFaces;
public abstract class CrudController<T extends Identificavel> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final GenericDAO<T> dao;
    protected List<T> lista;
    protected T selecionado;
    protected String termoBusca;

    protected CrudController(Class<T> classe) {
        this.dao = new GenericDAO<>(classe);
    }
    protected abstract T novaInstancia();
    protected abstract String[] camposBusca();
    protected void validar(T entidade) {
    }
    protected void antesDeSalvar(T entidade) {
    }

    @PostConstruct
    public void init() {
        listar();
    }

    public void listar() {
        lista = dao.listarTodos();
    }

    public void buscar() {
        if (termoBusca == null || termoBusca.isBlank()) {
            listar();
        } else {
            lista = dao.buscarPorCampos(termoBusca, camposBusca());
        }
    }

    public void limpar() {
        termoBusca = null;
        listar();
    }

    public void novo() {
        selecionado = novaInstancia();
    }

    public void editar(T item) {
        // Busca uma copia nova no banco: se o usuario cancelar, a lista da tela nao muda
        selecionado = dao.buscarPorId(item.getId());
    }

    public void salvar() {
        try {
            validar(selecionado);
            antesDeSalvar(selecionado);
            selecionado = dao.salvar(selecionado);
            buscar();
            FacesUtil.info("Registro salvo com sucesso!");
            PrimeFaces.current().ajax().addCallbackParam("fechar", true);
        } catch (IllegalArgumentException e) {
            FacesUtil.erro(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.erro("Não foi possível salvar. Verifique se já existe um registro com os mesmos dados "
                    + "(login, CNPJ ou número do pedido).");
        }
    }

    public void excluir(T item) {
        try {
            dao.excluir(item);
            buscar();
            FacesUtil.info("Registro excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.erro("Não foi possível excluir. O registro está sendo usado em outro cadastro.");
        }
    }

    protected static boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }

    public List<T> getLista() {
        return lista;
    }

    public T getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(T selecionado) {
        this.selecionado = selecionado;
    }

    public String getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {
        this.termoBusca = termoBusca;
    }
}
