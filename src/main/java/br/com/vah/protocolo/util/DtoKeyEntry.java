package br.com.vah.protocolo.util;

import br.com.vah.protocolo.controllers.ProtocoloCtrl;
import br.com.vah.protocolo.dto.DocumentoDTO;
import org.primefaces.event.SelectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jairoportela on 24/03/2017.
 */
public class DtoKeyEntry {

  private Map.Entry<String, List<DocumentoDTO>> entry;
  private List<DocumentoDTO> selecteds;
  private List<DocumentoDTO> filtereds;
  private List<DocumentoDTO> list;
  private String filter;
  private ProtocoloCtrl ctrl;
  private Boolean hideSelecteds = false;

  public DtoKeyEntry(Map.Entry<String, List<DocumentoDTO>> entry, ProtocoloCtrl ctrl) {
    this.entry = entry;
    this.selecteds = new ArrayList<>();
    this.filtereds = new ArrayList<>();
    this.list = entry.getValue();
    this.ctrl = ctrl;
  }

  public void changeFilter() {
    compile();
  }

  private Boolean filterCodigo(DocumentoDTO dto) {
    return filter == null || filter.isEmpty() || codigoContains(dto);
  }

  private Boolean codigoContains(DocumentoDTO dto) {
    return dto.getCodigo().toString().contains(filter);
  }

  public void toggleRow(SelectEvent evt) {
    DocumentoDTO dto = (DocumentoDTO) evt.getObject();
    if (dto.getSelected()) {
      if (this.selecteds != null) {
        this.selecteds.remove(dto);
      }
      ctrl.uncheckDto(dto);
    } else {
      ctrl.checkDto(dto);
    }
    ctrl.contarDocumentos();
    compile();
  }

  public void toggleHideSelecteds() {
    hideSelecteds = !hideSelecteds;
    compile();
  }

  public void compile() {
    if (hideSelecteds) {
      this.list = new ArrayList<>();
      this.selecteds = null;
      entry.getValue().forEach((item) -> {
        if (!item.getSelected()) {
          if (filterCodigo(item)) {
            this.list.add(item);
          }
        }
      });
    } else {
      this.list = entry.getValue();
      if (filter != null && !filter.isEmpty()) {
        this.list = this.list.stream().filter(this::codigoContains).collect(Collectors.toList());
      }
      this.selecteds = this.list.stream().filter((doc) -> doc.getSelected()).collect(Collectors.toList());
    }
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

  public List<DocumentoDTO> getList() {
    return list;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public Boolean getHideSelecteds() {
    return hideSelecteds;
  }
}
