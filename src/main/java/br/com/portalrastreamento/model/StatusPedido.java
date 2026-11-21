package br.com.portalrastreamento.model;

import java.text.Normalizer;

/**
 * Etapas do pedido, na ordem do andamento (produção até a entrega).
 */
public enum StatusPedido {

    EM_PRODUCAO("Em Produção", "pi-clock", "warning"),
    EXPEDIDO("Expedido", "pi-send", "info"),
    EM_TRANSITO("Em Trânsito", "pi-map-marker", "info"),
    ENTREGUE("Entregue", "pi-check-circle", "success");

    private final String descricao;
    private final String icone;
    private final String severidade;

    StatusPedido(String descricao, String icone, String severidade) {
        this.descricao = descricao;
        this.icone = icone;
        this.severidade = severidade;
    }

    public String getDescricao() { return descricao; }
    public String getIcone() { return icone; }
    public String getSeveridade() { return severidade; }

    /** Nome usado nas classes CSS (ex.: EM_TRANSITO vira "em-transito"). */
    public String getSlug() { return name().toLowerCase().replace('_', '-'); }

    /**
     * Converte um texto em status. Aceita o nome do Enum ("EM_TRANSITO") e a descrição
     * ("Em Trânsito"), sem diferenciar acento ou maiúscula. Devolve null se não reconhecer.
     */
    public static StatusPedido de(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String chave = normalizar(valor);
        for (StatusPedido s : values()) {
            if (normalizar(s.name()).equals(chave) || normalizar(s.descricao).equals(chave)) {
                return s;
            }
        }
        return null;
    }

    private static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
    }
}
