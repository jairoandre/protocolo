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
    Collections.sort(list, new Comparator<Map.Entry<String, List<DocumentoDTO>>>() {
      @Override
      public int compare(Map.Entry<String, List<DocumentoDTO>> o1, Map.Entry<String, List<DocumentoDTO>> o2) {
        return o1.getKey().compareTo(o2.getKey());
      }
    });
  }

  public List<DocumentoDTO> getSelecionados() {
    List<DocumentoDTO> selecionados = new ArrayList<>();
    if (getList() != null) {
      for (Map.Entry<String, List<DocumentoDTO>> docEntry : getList()) {
        if (docEntry.getValue() != null) {
          for (DocumentoDTO doc : docEntry.getValue()) {
            if (doc.getSelected()) {
              selecionados.add(doc);
            }
          }
        }
      }
    }
    return selecionados;
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }
}
