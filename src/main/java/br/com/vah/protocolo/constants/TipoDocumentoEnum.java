package br.com.vah.protocolo.constants;

public enum TipoDocumentoEnum {
  PRESCRICAO("Prescrição"),
  DOCUMENTO_PRONTUARIO("Prontuário"),
  EVOLUCAO("Evolução"),
  FOLHA_ANESTESICA("Folha Anest."),
  DESCRICAO_CIRURGICA("Desc. Cirúr."),
  REGISTRO_DOCUMENTO("Reg. Doc."),
  PROTOCOLO("Protocolo"),
  CONTA("Conta Fat."),
  DOCUMENTO_MANUAL("Doc. Manual");

  private String label;

  TipoDocumentoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
