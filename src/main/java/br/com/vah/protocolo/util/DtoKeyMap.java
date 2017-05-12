package br.com.vah.protocolo.util;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.controllers.ProtocoloCtrl;
import br.com.vah.protocolo.dto.DocumentoDTO;

import java.io.Serializable;
import java.util.*;

/**
 * Created by jairoportela on 24/06/2016.
 */
public class DtoKeyMap implements Serializable {

  private Map<String, List<DocumentoDTO>> map = new HashMap<>();

  private Map<String, DocumentoDTO> dtoMap = new HashMap<>();

  private DtoKeyEntryList list = new DtoKeyEntryList();

  private ProtocoloCtrl ctrl;

  public DtoKeyEntryList getList() {
    return list;
  }

  private int compareKeys(DtoKeyEntry o1, DtoKeyEntry o2) {
    return o1.getEntry().getKey().compareTo(o2.getEntry().getKey());
  }

  public void compile(ProtocoloCtrl ctrl) {
    this.ctrl = ctrl;
    if (!map.isEmpty()) {
      list = new DtoKeyEntryList(map.entrySet(), ctrl);
      sortEverything();
      list.forEach((docEntry) -> docEntry.fillSelecteds());
    }
  }

  public void put(String key, List<DocumentoDTO> value) {
    map.put(key, value);
  }


  public void remove(String key) {
    map.remove(key);
  }

  public Integer getCountSelecteds() {
    if (list.isEmpty()) {
      return 0;
    } else {
      return list.stream().map(DtoKeyEntry::getSelectedSize).reduce(0, (a, b) -> a + b);
    }
  }

  public Integer getCountTotal() {
    if (list.isEmpty()) {
      return 0;
    } else {
      return list.stream().map(DtoKeyEntry::getListSize).reduce(0, (a, b) -> a + b);
    }

  }

  public static String textoGrupo(TipoDocumentoEnum tipo) {
    switch (tipo) {
      case PRESCRICAO:
      case EVOLUCAO:
        return "Prescrição/Evolução";
      default:
        if (tipo.isDocManual()) {
          return "Documento Manual";
        }
        return tipo.getLabel();
    }
  }

  private String rowKey(DocumentoDTO dto) {
    return String.format("%d%s", dto.getCodigo(), dto.getTipo());
  }

  public void add(DocumentoDTO dto) {
    add(dto, false);
  }

  public void add(DocumentoDTO dto, Boolean selected) {
    String rowKey = rowKey(dto);
    DocumentoDTO included = dtoMap.get(rowKey);
    if (included == null) {
      dtoMap.put(rowKey, dto);
      String key = textoGrupo(dto.getTipo());
      List<DocumentoDTO> listaDoc = this.get(key);
      if (listaDoc == null) {
        listaDoc = new ArrayList<>();
        this.put(key, listaDoc);
      }
      listaDoc.add(dto);
      dto.setSelected(selected);
    } else if (selected) {
      included.setSelected(true);
    }
  }

  public void addAll(List<DocumentoDTO> dtos, Boolean selected) {
    if (dtos != null) {
      for (DocumentoDTO dto : dtos) {
        add(dto, selected);
      }
    }
  }

  public int sortDataHoraCriacao(DocumentoDTO dto1, DocumentoDTO dto2) {
    if (dto1.getDataHoraCriacao() != null) {
      if (dto2.getDataHoraCriacao() != null) {
        return dto1.getDataHoraCriacao().compareTo(dto2.getDataHoraCriacao());
      } else {
        return 1;
      }
    } else {
      return dto2.getDataHoraCriacao() == null ? 0 : -1;
    }
  }

  public void sortEverything() {
    map.values().forEach((list) -> Collections.sort(list, this::sortDataHoraCriacao));
    Collections.sort(list, this::compareKeys);
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }

  public ProtocoloCtrl getCtrl() {
    return ctrl;
  }
}
