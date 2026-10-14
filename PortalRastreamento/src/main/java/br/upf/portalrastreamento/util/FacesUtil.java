package br.upf.portalrastreamento.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
public final class FacesUtil {

    private FacesUtil() {
    }

    public static void info(String mensagem) {
        adicionar(FacesMessage.SEVERITY_INFO, mensagem);
    }

    public static void aviso(String mensagem) {
        adicionar(FacesMessage.SEVERITY_WARN, mensagem);
    }

    public static void erro(String mensagem) {
        adicionar(FacesMessage.SEVERITY_ERROR, mensagem);
    }

    private static void adicionar(FacesMessage.Severity tipo, String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, mensagem, null));
    }
}
