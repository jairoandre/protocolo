package br.com.vah.protocolo.dto;

import br.com.vah.protocolo.entities.dbamv.Documento;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * Created by Jairoportela on 23/03/2017.
 */
public class DocumentoDataModel extends ListDataModel<DocumentoDTO> implements SelectableDataModel<DocumentoDTO> {

  public DocumentoDataModel() {
  }

  public DocumentoDataModel(List<DocumentoDTO> data) {
    super(data);
  }

  private String rowKey(DocumentoDTO dto) {
    if (dto != null) {
      String.format("%d%s", dto.getCodigo(), dto.getTipo());
    }
    return "";
  }

  @Override
  public Object getRowKey(DocumentoDTO dto) {
    return rowKey(dto);
  }

  @Override
  public DocumentoDTO getRowData(String rowKey) {
    List<DocumentoDTO> dtos = (List<DocumentoDTO>) getWrappedData();

    for(DocumentoDTO dto : dtos) {
      if(rowKey(dto).equals(rowKey))
        return dto;
    }
    return null;
  }

  public void add(DocumentoDTO dto) {
    ((List<DocumentoDTO>) getWrappedData()).add(dto);
  }

  public List<DocumentoDTO> getList() {
    return (List<DocumentoDTO>) getWrappedData();
  }
}