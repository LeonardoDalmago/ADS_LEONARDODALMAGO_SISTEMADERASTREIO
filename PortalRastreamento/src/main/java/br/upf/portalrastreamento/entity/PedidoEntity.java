package br.upf.portalrastreamento.entity;

import br.upf.portalrastreamento.enumeration.StatusPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedido")
public class PedidoEntity implements Identificavel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_pedido", nullable = false, length = 30, unique = true)
    private String numeroPedido;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "previsao_entrega")
    private LocalDate previsaoEntrega;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Muitos pedidos para um cliente (lado "muitos" do one-to-many Cliente -> Pedido)
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "transportadora_id")
    private TransportadoraEntity transportadora;

    // Um pedido para muitos registros de historico (one-to-many)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("data DESC, id DESC")
    private List<HistoricoPedidoEntity> historico = new ArrayList<>();
    public void adicionarHistorico(String descricao) {
        HistoricoPedidoEntity registro = new HistoricoPedidoEntity();
        registro.setPedido(this);
        registro.setData(LocalDateTime.now());
        registro.setDescricao(descricao);
        this.historico.add(registro);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public TransportadoraEntity getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraEntity transportadora) {
        this.transportadora = transportadora;
    }

    public List<HistoricoPedidoEntity> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoPedidoEntity> historico) {
        this.historico = historico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoEntity)) {
            return false;
        }
        PedidoEntity outro = (PedidoEntity) o;
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
