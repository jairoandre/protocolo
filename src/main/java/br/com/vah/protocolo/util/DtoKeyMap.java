package br.com.vah.protocolo.util;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
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

  public void put(String key, List<DocumentoDTO> value) {
    remove("Sem documentos");
    map.put(key, value);
    list = new DtoKeyEntryList(map.entrySet());
    Collections.sort(list, (o1, o2) -> compareKeys(o1, o2));
  }

  public void clearSelecteds() {
    list.forEach((item) -> item.getSelecteds().clear());
  }

  public Integer getCountSelecteds() {
    if (list.isEmpty()) {
      return 0;
    } else {
      return list.stream().map(DtoKeyEntry::getSelectedSize).reduce(0, (a, b) -> a + b);
    }
  }

  public Integer getTotalSize() {
    if (list.isEmpty()) {
      return 0;
    } else {
      return list.stream().map(DtoKeyEntry::getListSize).reduce(0, (a, b) -> a + b);
    }

  }

  public void remove(String key) {
    map.remove(key);
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

  public List<DocumentoDTO> getSelecionados() {
    List<DocumentoDTO> selecionados = new ArrayList<>();
    if (list != null) {
      list.forEach((dtoKeyEntry) -> {
        if (dtoKeyEntry.getEntry().getValue() != null) {
          dtoKeyEntry.getEntry().getValue().forEach((doc) -> {
            if (doc.getSelected()) {
              selecionados.add(doc);
            }
          });
        }
      });
    }
    return selecionados;
  }

  private String rowKey(DocumentoDTO dto) {
    return String.format("%d%s", dto.getCodigo(), dto.getTipo());
  }

  public void addDto(DocumentoDTO dto) {
    String key = textoGrupo(dto.getTipo());
    dtoMap.put(rowKey(dto), dto);
    List<DocumentoDTO> listaDoc = this.get(key);
    if (listaDoc == null) {
      listaDoc = new ArrayList<>();
      this.put(key, listaDoc);
    }
    listaDoc.add(dto);
    dto.setSelected(false);
    Collections.sort(listaDoc, (o1, o2) -> o1.getDataHoraCriacao().compareTo(o2.getDataHoraCriacao()));
    list = new DtoKeyEntryList(map.entrySet());
    Collections.sort(list, (o1, o2) -> compareKeys(o1, o2));
  }

  public DtoKeyMap getNotSelectedMap() {
    DtoKeyMap notSelectedMap = new DtoKeyMap();
    if (list != null) {
      list.forEach((docEntry) -> {
        List<DocumentoDTO> notSelecteds = new ArrayList<>();
        docEntry.getEntry().getValue().forEach((item) -> {
          if (!item.getSelected()) {
            notSelecteds.add(item);
          }
        });
        if (!notSelecteds.isEmpty()) {
          notSelectedMap.put(docEntry.getEntry().getKey(), notSelecteds);
        }
      });
    }
    return notSelectedMap;
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }

}
