package br.com.vah.protocolo.util;

import java.util.List;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class DtoKey<T> {

  private String key;
  private List<T> selectedItems;

  public DtoKey(String key) {
    this.key = key;
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return key.equals(((DtoKey) obj).key);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public List<T> getSelectedItems() {
    return selectedItems;
  }

  public void setSelectedItems(List<T> selectedItems) {
    this.selectedItems = selectedItems;
  }
}
