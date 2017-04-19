package br.com.vah.protocolo.util;

import br.com.vah.protocolo.controllers.ProtocoloCtrl;
import br.com.vah.protocolo.dto.DocumentoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jairoportela on 24/03/2017.
 */
public class DtoKeyEntryList extends ArrayList<DtoKeyEntry> {

  public DtoKeyEntryList() {
    super();
  }

  public DtoKeyEntryList (Set<Map.Entry<String, List<DocumentoDTO>>> entrySet, ProtocoloCtrl ctrl) {
    super();
    entrySet.forEach((entry) -> add(new DtoKeyEntry(entry, ctrl)));
  }
}
