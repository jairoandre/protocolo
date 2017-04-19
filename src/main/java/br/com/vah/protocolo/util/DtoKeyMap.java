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

  public DtoKeyEntryList getList() {
    return list;
  }

  private int compareKeys(DtoKeyEntry o1, DtoKeyEntry o2) {
    return o1.getEntry().getKey().compareTo(o2.getEntry().getKey());
  }

  public void compile(ProtocoloCtrl ctrl) {
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
      case DOC_MANUAL_EVOLUCAO:
      case DOC_MANUAL_PRESCRICAO:
      case DOC_MANUAL_EVOLUCAO_ANOTACAO:
      case DOC_MANUAL_DESCRICAO_CIRURGICA:
      case DOC_MANUAL_FOLHA_ANESTESICA:
      case DOC_MANUAL_HDA:
      case DOC_MANUAL_EVOLUCAO_ENFERMAGEM:
      case DOC_MANUAL_EXAME_FISICO_EVOLUCAO_ENFERMAGEM:
      case DOC_MANUAL_REGISTRO_SINAIS_VITAIS_PS:
      case DOC_MANUAL_CLASSIFICACAO_RISCO:
      case DOC_MANUAL_FOLHA_CLASSIFICACAO:
      case DOC_MANUAL_SADT:
      case DOC_MANUAL_AUTORIZACAO_PS:
        return "Documento Manual";
      default:
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

  public void sortEverything() {
    map.values().forEach((list) -> Collections.sort(list, (o1, o2) -> o1.getDataHoraCriacao().compareTo(o2.getDataHoraCriacao())));
    Collections.sort(list, this::compareKeys);
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }

}
