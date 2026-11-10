package br.com.portalrastreamento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String codigoRastreio;
    private String produto; // Ex: Palete Plástico, Caixa Agrícola
    private String cliente;
    private String status;  // EM_PRODUCAO, EXPEDIDO, ENTREGUE
    private Date dataAtualizacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigoRastreio() { return codigoRastreio; }
    public void setCodigoRastreio(String codigoRastreio) { this.codigoRastreio = codigoRastreio; }
    public String getProduto() { return produto; }
    public void setProduto(String produto) { this.produto = produto; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(Date dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}