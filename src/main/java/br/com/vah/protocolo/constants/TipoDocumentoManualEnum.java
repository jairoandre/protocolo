package br.com.vah.protocolo.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairoportela on 15/06/2016.
 */
public enum TipoDocumentoManualEnum {
  EVOLUCAO("Evolução"),
  PRESCRICAO("Prescrição"),
  DOCUMENTO_PRONTUARIO("Documento de Prontuário"),
  EVOLUCAO_ANOTACAO("Evolução/Anotação"),
  DESCRICAO_CIRURGICA("Descrição Cirurgica"),
  FOLHA_ANESTESICA("Folha Anestésica"),
  HDA("HDA"),
  EVOLUCAO_ENFERMAGEM("Evolução de Enfermagem"),
  EXAME_FISICO_EVOLUCAO_ENFERMAGEM("Exame Físico e Evolução de Enfermagem"),
  REGISTRO_SINAIS_VITAIS_PS("Registro de Sinais Vitais - PS"),
  CLASSIFICACAO_RISCO("Classificação de Risco"),
  FOLHA_CLASSIFICACAO("Folha de Classificação"),
  SADT("SADT"),
  AUTORIZACAO_PS("Autorização (PS)");

  private String label;

  private TipoDocumentoManualEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems(Boolean addNullOption) {
    List<SelectItem> items = new ArrayList<>();
    if (addNullOption) {
      items.add(new SelectItem(null, "Selecione..."));
    }
    for (TipoDocumentoManualEnum tipo : TipoDocumentoManualEnum.values()) {
      items.add(new SelectItem(tipo, tipo.getLabel()));
    }
    return items;
  }

}
