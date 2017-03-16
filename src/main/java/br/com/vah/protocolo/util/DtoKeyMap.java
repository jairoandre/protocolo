package br.com.vah.protocolo.util;

import br.com.vah.protocolo.dto.DocumentoDTO;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  private int compareKeys(Map.Entry<String, List<DocumentoDTO>> o1, Map.Entry<String, List<DocumentoDTO>> o2)  {
    try {
      Date d1 = sdf.parse(o1.getKey());
      Date d2 = sdf.parse(o2.getKey());
      return d1.compareTo(d2);
    } catch (ParseException pe) {
      return 0;
    }
  }

  public void put(String key, List<DocumentoDTO> value) {
    map.put(key, value);
    list = new ArrayList<>(map.entrySet());
    Collections.sort(list, (o1, o2) -> compareKeys(o1, o2));
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

  public void addDto(DocumentoDTO dto) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String key = sdf.format(dto.getDataHoraCriacao());
    List<DocumentoDTO> listaDoc = this.get(key);
    if (listaDoc == null) {
      listaDoc = new ArrayList<>();
      this.put(key, listaDoc);
    }
    listaDoc.add(dto);
    Collections.sort(listaDoc, (o1, o2) -> o1.getDataHoraCriacao().compareTo(o2.getDataHoraCriacao()));
    list = new ArrayList<>(map.entrySet());
    Collections.sort(list, (o1, o2) -> compareKeys(o1, o2));
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
        if (!notSelecteds.isEmpty()) {
          notSelectedMap.put(docEntry.getKey(), notSelecteds);
        }
      });
    }
    return notSelectedMap;
  }

  public List<DocumentoDTO> get(String key) {
    return map.get(key);
  }
}
