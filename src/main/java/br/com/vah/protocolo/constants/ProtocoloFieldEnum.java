package br.com.vah.protocolo.constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jairoportela on 02/03/2017.
 */
public enum ProtocoloFieldEnum {
  ATENDIMENTO("<b>Atendimento: </b>"),
  DATA("<b>Data: </b>"),
  PACIENTE("<b>Paciente: </b>"),
  CONVENIO("<b>Convênio: </b>"),
  ESTADO("<b>Estado: </b>"),
  SETOR("<b>Setor: </b>");

  private String label;

  ProtocoloFieldEnum(String label) {
    this.label = label;
  }

  public static String getText(Map<ProtocoloFieldEnum, Object> map, ProtocoloFieldEnum field) {
    switch (field) {
      case ATENDIMENTO:
        return String.format("%s %s", field.label, map.get(field) == null ? "" : map.get(field).toString());
      case DATA:
        Date[] range = (Date[]) map.get(field);
        if (range != null) {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          String text = String.format("de %s a %s", sdf.format(range[0]), sdf.format(range[1]));
          return String.format("%s %s", field.label, text);
        }
        break;
      case PACIENTE:
      case SETOR:
        return String.format("%s %s", field.label, map.get(field) == null ? "" : map.get(field).toString());
      case CONVENIO:
        break;
      case ESTADO:
        if (map.get(field) != null) {
          EstadosProtocoloEnum[] estados = (EstadosProtocoloEnum[]) map.get(field);
          List<String> estadosStr = new ArrayList<>();
          for (EstadosProtocoloEnum estado : estados) {
            estadosStr.add(estado.getLabel());
          }
          return String.format("%s %s", field.label, String.join(", ", estadosStr));
        }
        break;
      default:
        break;
    }
    return "";
  }


}
