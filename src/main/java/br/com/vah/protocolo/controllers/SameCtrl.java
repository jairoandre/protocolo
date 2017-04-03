package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.SetorNivelEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.Documento;
import br.com.vah.protocolo.entities.usrdbvah.CaixaEntrada;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.service.CaixaEntradaSrv;
import br.com.vah.protocolo.service.ProtocoloSrv;
import br.com.vah.protocolo.util.DtoKeyEntry;
import br.com.vah.protocolo.util.DtoKeyEntryList;
import br.com.vah.protocolo.util.DtoKeyMap;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jairoportela on 01/04/2017.
 */
@Named
@ViewScoped
public class SameCtrl implements Serializable {

  private Integer route = 0;

  private
  @Inject
  CaixaEntradaSrv caixaEntradaSrv;

  private
  @Inject
  ProtocoloSrv protocoloSrv;

  private List<DocumentoDTO> pendentes;

  private List<DocumentoDTO> selecteds;

  private List<DocumentoDTO> filtereds;

  private Protocolo protocoloToVisualize;

  private DtoKeyMap documentosToVisualize;

  private String listaContas;

  private Convenio convenio;

  public void navigate(Integer route) {
    this.route = route;
  }

  public void buscarPendentes() {
    List<CaixaEntrada> naoVinculados = caixaEntradaSrv.buscarDocumentosPendentes(null);
    final List<DocumentoDTO> dtos = new ArrayList<>();
    naoVinculados.forEach((caixa) -> dtos.add(new DocumentoDTO(caixa)));
    pendentes = dtos;
  }

  public void pendentesSelectRow(SelectEvent evt) {
    DocumentoDTO dto = (DocumentoDTO) evt.getObject();
    if (dto.getSelected()) {
      if (selecteds != null) {
        selecteds.remove(dto);
      }
      dto.setSelected(false);
    } else {
      dto.setSelected(true);
    }
  }

  public void unselectRow(UnselectEvent evt) {
    DocumentoDTO dto = (DocumentoDTO) evt.getObject();
    dto.setSelected(false);
  }

  public void visualizarDoc(DocumentoDTO dto) {
    Protocolo att = protocoloSrv.initializeLists(dto.getFilho());
    protocoloToVisualize = att;
    documentosToVisualize = protocoloSrv.gerarDocumentosSelecionados(att, true);
  }

  public void closeDocumentosDlg() {
    protocoloToVisualize = null;
    documentosToVisualize = null;
  }

  // GETTERS AND SETTERS

  public Integer getPendentesSelectCount() {
    if (pendentes == null) {
      return 0;
    } else {
      return pendentes.stream().map(DocumentoDTO::getSelectedInteger).reduce(0, (a, b) -> a + b);
    }
  }

  public Integer getRoute() {
    return route;
  }

  public List<DocumentoDTO> getPendentes() {
    return pendentes;
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

  public Protocolo getProtocoloToVisualize() {
    return protocoloToVisualize;
  }

  public void setProtocoloToVisualize(Protocolo protocoloToVisualize) {
    this.protocoloToVisualize = protocoloToVisualize;
  }

  public DtoKeyMap getDocumentosToVisualize() {
    return documentosToVisualize;
  }

  public void setDocumentosToVisualize(DtoKeyMap documentosToVisualize) {
    this.documentosToVisualize = documentosToVisualize;
  }

  public String getListaContas() {
    return listaContas;
  }

  public void setListaContas(String listaContas) {
    this.listaContas = listaContas;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }
}
