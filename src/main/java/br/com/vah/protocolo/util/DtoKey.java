package br.com.vah.protocolo.util;

import br.com.vah.protocolo.dto.DocumentoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class DtoKey<T extends DocumentoDTO> {

  private String value;
  private List<T> selectedItems = new ArrayList<>();

  public DtoKey(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return value.equals(((DtoKey) obj).value);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<T> getSelectedItems() {
    return selectedItems;
  }

  public void setSelectedItems(List<T> selectedItems) {
    this.selectedItems = selectedItems;
  }
}
