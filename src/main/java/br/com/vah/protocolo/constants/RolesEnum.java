package br.com.vah.protocolo.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public enum RolesEnum {
  SECRETARIA("Secretaria de clínica"),
  FATURAMENTO_SETORIAL("Faturamento setorial"),
  ENVIOS_AUDITORIA("Envios/Auditora"),
  SAME("Same"),
  ADMINISTRATOR("Administrador", SECRETARIA, SAME, ENVIOS_AUDITORIA, FATURAMENTO_SETORIAL);

  private String label;
  private RolesEnum[] subroles;

  private RolesEnum(String label, RolesEnum... subroles) {
    this.label = label;
    this.subroles = subroles;
  }

  public String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> items = new ArrayList<>();
    items.add(new SelectItem(null, "Selecione..."));
    for (RolesEnum role : RolesEnum.values()) {
      items.add(new SelectItem(role, role.getLabel()));
    }
    return items;
  }

  public boolean hasSubRole(RolesEnum role) {
    if (this.equals(role)) {
      return true;
    } else if (this.subroles != null) {
      for (RolesEnum subrole : this.subroles) {
        if (subrole.hasSubRole(role)) {
          return true;
        }
      }

    }
    return false;
  }
}
