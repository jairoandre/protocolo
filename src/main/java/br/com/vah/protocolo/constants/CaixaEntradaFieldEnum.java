package br.com.vah.protocolo.constants;

import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Jairoportela on 25/03/2017.
 */
public enum CaixaEntradaFieldEnum {
  CONTAS("Contas"),
  CONVENIOS("Convênios"),
  SETOR("Setor"),
  VINCULADOS("Vinculados");

  private String label;

  CaixaEntradaFieldEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return String.format("<b>%s:</b>", label);
  }

  private String formatedText(String string) {
    return String.format("%s %s", getLabel(), string);
  }

  public String getText(Object object) {
    if (object == null) {
      return "";
    } else {
      switch (this) {
        case CONTAS:
          List<Long> contas = (List<Long>) object;
          StringJoiner joiner = new StringJoiner(";");
          if (contas != null) {
            for (Long conta : contas) {
              joiner.add(conta.toString());
            }
          }
          return formatedText(joiner.toString());
        case CONVENIOS:
          List<Convenio> convenios = (List<Convenio>) object;
          StringJoiner conveniosJoiner = new StringJoiner(", ");
          for (Convenio convenio : convenios) {
            conveniosJoiner.add(convenio.getTitle());
          }
          return formatedText(conveniosJoiner.toString());
        case SETOR:
          SetorProtocolo setor = (SetorProtocolo) object;
          return formatedText(setor.getTitle());
        case VINCULADOS:
          Boolean vinculados = (Boolean) object;
          return formatedText(vinculados ? "Sim" : "Não");
        default:
          return "";
      }
    }

  }

}
