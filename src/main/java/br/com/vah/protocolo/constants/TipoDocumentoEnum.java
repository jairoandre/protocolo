package br.com.vah.protocolo.constants;

public enum TipoDocumentoEnum {
  PRESCRICAO("Prescrição"),
  DOCUMENTO_PRONTUARIO("Prontuário"),
  EVOLUCAO("Evolução"),
  FOLHA_ANESTESICA("Folha Anestésica"),
  DESCRICAO_CIRURGICA("Descrição Cirúrgica"),
  REGISTRO_DOCUMENTO("Registro Documento"),
  DOCUMENTO_MANUAL("Documento Manual");

  private String label;

  private TipoDocumentoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
