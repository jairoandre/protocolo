package br.com.vah.protocolo.util;

import br.com.vah.protocolo.dto.DocumentoDTO;

import java.io.Serializable;
import java.util.*;

/**
 * Created by jairoportela on 24/06/2016.
 */
public class DtoKeyMap implements Serializable {

  private Map<String, List<DocumentoDTO>> map = new HashMap<>();
  private List<Map.Entry<String, List<DocumentoDTO>>> list;

  public DtoKeyMap() {
    map = new HashMap<>();
  }

  public List<Map.Entry<String, List<DocumentoDTO>>> getList() {
    return list;
  }

  public void put(String key, List<DocumentoDTO> value) {
    map.put(key, value);
    list = new ArrayList<>(map.entrySet());
    Collections.sort(list, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
  }

  public List<DocumentoDTO> getSelecionados() {
    List<DocumentoDTO> selecionados = new ArrayList<>();
    if (list != null) {
      list.forEach((docEntry) -> {
        if (docEntry.getValue() != null) {
          docEntry.getValue().forEach((doc) -> {
            if (doc.getSelected()) {
              selecionados.add(doc);
            }
          });
        }
      });
    }
    return selecionados;
  }

  public DtoKeyMap getNotSelectedMap() {
    DtoKeyMap notSelectedMap = new DtoKeyMap();
    if (list != null) {
      list.forEach((docEntry) -> {
        List<DocumentoDTO> notSelecteds = new ArrayList<>();
        docEntry.getValue().forEach((item) -> {
          if (!item.getSelected()) {
            notSelecteds.add(item);
          }
        });
        notSelectedMap.put(docEntry.getKey(), notSelecteds);
      });
    }
    return notSelectedMap;
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }
}
