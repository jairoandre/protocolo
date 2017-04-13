package br.com.vah.protocolo.util;

import br.com.vah.protocolo.dto.DocumentoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jairoportela on 24/03/2017.
 */
public class DtoKeyEntry {

  private Map.Entry<String, List<DocumentoDTO>> entry;
  private List<DocumentoDTO> selecteds;
  private List<DocumentoDTO> filtereds;

  public DtoKeyEntry(Map.Entry<String, List<DocumentoDTO>> entry) {
    this.entry = entry;
    this.selecteds = new ArrayList<>();
    this.filtereds = new ArrayList<>();
  }

  public void fillSelecteds() {
    List<DocumentoDTO> dtos = entry.getValue();
    if (dtos != null) {
      for (DocumentoDTO dto : dtos) {
        if (dto.getSelected()) {
          this.selecteds.add(dto);
        }
      }
    }
  }

  public Integer getSelectedSize() {
    return this.selecteds == null ? 0 : this.selecteds.size();
  }

  public Integer getListSize() {
    return entry == null ? 0 : (entry.getValue() == null ? 0 : entry.getValue().size());
  }

  public Map.Entry<String, List<DocumentoDTO>> getEntry() {
    return entry;
  }

  public void setEntry(Map.Entry<String, List<DocumentoDTO>> entry) {
    this.entry = entry;
  }

  public List<DocumentoDTO> getSelecteds() {
    return selecteds;
  }

  public void setSelecteds(List<DocumentoDTO> selecteds) {
    this.selecteds = selecteds;
  }

  public List<DocumentoDTO> getFiltereds() {
    return filtereds;
  }

  public void setFiltereds(List<DocumentoDTO> filtereds) {
    this.filtereds = filtereds;
  }
}
