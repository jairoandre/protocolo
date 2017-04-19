package br.com.vah.protocolo.constants;

/**
 * Created by Jairoportela on 28/03/2017.
 */
public enum SetorNivelEnum {
  SECRETARIA("Secretaria"),
  FATURAMENTO_CENTRAL("Faturamento Central"),
  ENVIOS("Envios"),
  AUDITORIA("Auditoria"),
  SAME("SAME"),
  PRONTO_SOCORRO("Pronto Socorro");

  private String label;

  SetorNivelEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
