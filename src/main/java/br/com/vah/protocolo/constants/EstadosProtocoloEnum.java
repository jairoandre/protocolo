package br.com.vah.protocolo.constants;

/**
 * Created by jairoportela on 14/06/2016.
 */
public enum EstadosProtocoloEnum {
  ENVIADO("Enviado"),
  RECEBIDO("Recebido"),
  RECUSADO("Recusado"),
  CORRIGIDO("Corrigido"),
  ARQUIVADO("Arquivado"),
  RASCUNHO("Rascunho"),
  COMENTARIO("Comentário"),
  REPASSADO("Repassado"),
  PRONTO_SOCORRO("Pronto Socorro");

  private String label;

  EstadosProtocoloEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
