package br.com.vah.protocolo.constants;

public enum TipoDocumentoEnum {
  PRESCRICAO("Prescrição"),
  DOCUMENTO_PRONTUARIO("Prontuário"),
  EVOLUCAO("Evolução"),
  FOLHA_ANESTESICA("Folha Anestésica"),
  DESCRICAO_CIRURGICA("Descrição Cirúrgica"),
  REGISTRO_DOCUMENTO("Registro Documento"),
  PROTOCOLO("Protocolo"),
  EVOLUCAO_ENFERMAGEM("Evolução/Anotação"),
  DOC_MANUAL_EVOLUCAO("Evolução"),
  DOC_MANUAL_PRESCRICAO("Prescrição"),
  DOC_MANUAL_EVOLUCAO_ANOTACAO("Evolução"),
  DOC_MANUAL_DESCRICAO_CIRURGICA("Descrição Cirurgica"),
  DOC_MANUAL_FOLHA_ANESTESICA("Folha Anestésica"),
  DOC_MANUAL_HDA("HDA"),
  DOC_MANUAL_EVOLUCAO_ENFERMAGEM("Evolução/Anotação"),
  DOC_MANUAL_EXAME_FISICO_EVOLUCAO_ENFERMAGEM("Exame Físico e Evolução de Enfermagem"),
  DOC_MANUAL_REGISTRO_SINAIS_VITAIS_PS("Registro de Sinais Vitais - PS"),
  DOC_MANUAL_CLASSIFICACAO_RISCO("Classificação de Risco"),
  DOC_MANUAL_FOLHA_CLASSIFICACAO("Folha de Classificação"),
  DOC_MANUAL_SADT("SADT"),
  DOC_MANUAL_AUTORIZACAO_PS("Autorização (PS)"),
  HDA("HDA"),
  CLASSIFICACAO_DE_RISCO("Classificação de Risco"),
  DIAGNOSTICO_DO_ATENDIMENTO("Diagnóstico do Atendimento"),
  GUIA_SADT("Guia SADT"),
  ATENDIMENTO("Atendimento");


  private String label;

  TipoDocumentoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public static TipoDocumentoEnum getByTitle(String title) {
    switch (title) {
      case "Documento de Prontuário":
        return DOCUMENTO_PRONTUARIO;
      case "Evolução/Anotação":
        return EVOLUCAO_ENFERMAGEM;
      case "Prescrição":
        return PRESCRICAO;
      case "Evolução":
        return EVOLUCAO;
      case "Descrição Cirurgica":
        return DESCRICAO_CIRURGICA;
      case "Folha Anestésica":
        return FOLHA_ANESTESICA;
      default:
        return null;
    }
  }

  public static TipoDocumentoEnum[] documentosManuais = {
      DOC_MANUAL_EVOLUCAO,
      DOC_MANUAL_PRESCRICAO,
      DOC_MANUAL_EVOLUCAO_ANOTACAO,
      DOC_MANUAL_DESCRICAO_CIRURGICA,
      DOC_MANUAL_FOLHA_ANESTESICA,
      DOC_MANUAL_HDA,
      DOC_MANUAL_EVOLUCAO_ENFERMAGEM,
      DOC_MANUAL_EXAME_FISICO_EVOLUCAO_ENFERMAGEM,
      DOC_MANUAL_REGISTRO_SINAIS_VITAIS_PS,
      DOC_MANUAL_CLASSIFICACAO_RISCO,
      DOC_MANUAL_FOLHA_CLASSIFICACAO,
      DOC_MANUAL_SADT,
      DOC_MANUAL_AUTORIZACAO_PS
  };

}
