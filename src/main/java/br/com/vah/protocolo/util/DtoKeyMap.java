package br.com.vah.protocolo.util;

import br.com.vah.protocolo.dto.DocumentoDTO;

import java.io.Serializable;
import java.util.*;

/**
 * Created by jairoportela on 24/06/2016.
 */
public class DtoKeyMap<T extends DocumentoDTO> implements Serializable {

  private Map<DtoKey<T>, List<T>> map;
  private List<Map.Entry<DtoKey<T>, List<T>>> list;

  public DtoKeyMap() {
    map = new HashMap<>();
  }

  public List<Map.Entry<DtoKey<T>, List<T>>> getList() {
    return list;
  }

  public void put(DtoKey<T> key, List<T> value) {
    map.put(key, value);
    list = new ArrayList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<DtoKey<T>, List<T>>>() {
      @Override
      public int compare(Map.Entry<DtoKey<T>, List<T>> o1, Map.Entry<DtoKey<T>, List<T>> o2) {
        return o1.getKey().getValue().compareTo(o2.getKey().getValue());
      }
    });
  }

  public List<T> getSelecionados() {
    List<T> selecionados = new ArrayList<>();
    for (Map.Entry<DtoKey<T>, List<T>> docEntry : getList()) {
      for (DocumentoDTO doc : docEntry.getValue()) {
        if (doc.getSelected()) {
          selecionados.add((T) doc);
        }
      }
    }
    return selecionados;
  }

  public List<T> get(DtoKey<T> key) {
    return map.get(key);
  }

}
