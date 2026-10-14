package br.upf.portalrastreamento.controller;

import br.upf.portalrastreamento.dao.GenericDAO;
import br.upf.portalrastreamento.entity.ClienteEntity;
import br.upf.portalrastreamento.entity.PedidoEntity;
import br.upf.portalrastreamento.util.FacesUtil;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Named
@ViewScoped
public class RastreioController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GenericDAO<ClienteEntity> clienteDao = new GenericDAO<>(ClienteEntity.class);
    private final GenericDAO<PedidoEntity> pedidoDao = new GenericDAO<>(PedidoEntity.class);

    private String cnpj;
    private ClienteEntity cliente;
    private List<PedidoEntity> pedidos;
    private boolean consultado;

    public void consultar() {
        consultado = false;
        cliente = null;
        pedidos = null;

        String numeros = (cnpj == null) ? "" : cnpj.replaceAll("\\D", "");
        if (numeros.length() != 14) {
            FacesUtil.erro("Informe um CNPJ válido, com 14 números.");
            return;
        }

        Map<String, Object> paramsCliente = new HashMap<>();
        paramsCliente.put("cnpj", numeros);
        List<ClienteEntity> encontrados = clienteDao.consultar(
                "SELECT c FROM ClienteEntity c WHERE c.cnpj = :cnpj", paramsCliente);

        if (encontrados.isEmpty()) {
            FacesUtil.aviso("Nenhum cliente encontrado para este CNPJ.");
            return;
        }

        cliente = encontrados.get(0);
        Map<String, Object> paramsPedido = new HashMap<>();
        paramsPedido.put("clienteId", cliente.getId());
        pedidos = pedidoDao.consultar(
                "SELECT p FROM PedidoEntity p WHERE p.cliente.id = :clienteId ORDER BY p.dataCriacao DESC",
                paramsPedido);
        consultado = true;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public List<PedidoEntity> getPedidos() {
        return pedidos;
    }

    public boolean isConsultado() {
        return consultado;
    }
}
