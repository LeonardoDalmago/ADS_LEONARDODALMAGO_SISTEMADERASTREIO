package br.com.portalrastreamento.bean;

import br.com.portalrastreamento.dao.MensagemSuporteDAO;
import br.com.portalrastreamento.model.MensagemSuporte;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SuporteConvidadoBean implements Serializable {

    @Inject
    private MensagemSuporteDAO mensagemSuporteDAO;

    private String nome;
    private String email;
    private String codigoPedido;
    private String assunto;
    private String mensagem;

    public void enviar() {
        MensagemSuporte msg = new MensagemSuporte();
        msg.setNome(nome.trim());
        msg.setEmail(email.trim());
        msg.setCodigoPedido(codigoPedido == null || codigoPedido.isBlank()
                ? null : codigoPedido.trim().toUpperCase());
        msg.setAssunto(assunto.trim());
        msg.setMensagem(mensagem.trim());

        try {
            mensagemSuporteDAO.salvar(msg);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Mensagem enviada! Nossa equipe vai responder no e-mail informado.", null));
            limpar();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Não foi possível enviar sua mensagem agora. Tente novamente.", null));
        }
    }

    private void limpar() {
        nome = null;
        email = null;
        codigoPedido = null;
        assunto = null;
        mensagem = null;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }
    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
