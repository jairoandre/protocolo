package br.com.vah.protocolo.controllers;

import java.util.*;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.service.AtendimentoService;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.PrescricaoMedicaService;
import br.com.vah.protocolo.service.ProtocoloService;
import br.com.vah.protocolo.util.ViewUtils;

/**
 * Created by jairoportela on 15/06/2016.
 */
@Named
@ViewScoped
public class ProtocoloController extends AbstractController<Protocolo> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ProtocoloService service;

  private
  @Inject
  SessionController session;

  private
  @Inject
  AtendimentoService atendimentoService;

  public static final String[] RELATIONS = {"prescricoes", "avisos", "registros", "historico", "comentarios"};

  private Setor setor;

  private EstadosProtocoloEnum[] estados = EstadosProtocoloEnum.values();

  private EstadosProtocoloEnum[] selectedEstados;

  private Date inicioDate;

  private Date terminoDate;

  private String comentario;

  private Map<String, List<DocumentoDTO>> mapSelectedDocumentos = new HashMap<>();

  List<DocumentoDTO> selectedDocumentos;

  private List<Map.Entry<String, List<DocumentoDTO>>> documentos;

  private List<Map.Entry<String, List<DocumentoDTO>>> documentosSelecionados;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service, RELATIONS);
    prepareSearch();
    Date[] range = ViewUtils.getDateRange2Days();
    inicioDate = range[0];
    terminoDate = range[1];
  }

  @Override
  public void prepareSearch() {
    resetSearchParams();
    String regex = "[0-9]+";
    if (getSearchTerm() != null && getSearchTerm().matches(regex)) {
      setSearchParam("atendimento", Long.valueOf(getSearchTerm()));
    } else {
      setSearchParam("paciente", getSearchTerm());
    }
    if (setor != null) {
      setSearchParam("setor", setor);
    }
    if (session.getSetor() != null) {
      setSearchParam("setor", session.getSetor());
    }
    if (selectedEstados != null && selectedEstados.length > 0) {
      setSearchParam("estados", selectedEstados);
    }
    if (inicioDate != null || terminoDate != null) {
      setSearchParam("dateRange", new Date[]{inicioDate, terminoDate});
    }
  }

  public void filterCurrentMonth() {
    Date[] thisMonth = ViewUtils.getDateRangeForThisMonth();
    inicioDate = thisMonth[0];
    terminoDate = thisMonth[1];
    prepareSearch();
  }

  public void clearSelectedEstados() {
    this.selectedEstados = null;
    prepareSearch();
  }

  public void clearSetor() {
    setor = null;
    prepareSearch();
  }

  public void receber(Protocolo protocolo) {

  }

  public Boolean showRefuseButton(Protocolo protocolo) {
    // TODO: Implementar verificação de exibição
    return true;
  }

  public Boolean showEditButton(Protocolo protocolo) {
    return true;
  }

  public Boolean showDeleteButton(Protocolo protocolo) {
    return true;
  }

  public void salvarNovoComentario() {

  }

  public void buscarDocumentos() {
    documentos = service.buscarMapaDocumentos(getItem().getAtendimento(), inicioDate, terminoDate, session.getSetor(), getItem());
    mapSelectedDocumentos = new HashMap<>();
    for (Map.Entry<String, List<DocumentoDTO>> entryDoc : documentos) {
      mapSelectedDocumentos.put(entryDoc.getKey(), new ArrayList<DocumentoDTO>());
    }
  }

  public List<Map.Entry<String, List<DocumentoDTO>>> getDocumentos() {
    return this.documentos;
  }

  public void recuperarDadosRascunho() {

    Protocolo rascunho = service.buscarDadosRascunho(getItem().getAtendimento());
    if(rascunho != null){
      setItem(rascunho);
    }

    documentosSelecionados = service.mapearDocumentosSelecionados(getItem());

  }

  @Override
  public DataAccessService<Protocolo> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Protocolo createNewItem() {
    return new Protocolo();
  }

  @Override
  public String path() {
    return "protocolo";
  }

  @Override
  public String getEntityName() {
    return "Protocolo";
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public EstadosProtocoloEnum[] getEstados() {
    return estados;
  }

  public void setEstados(EstadosProtocoloEnum[] estados) {
    this.estados = estados;
  }

  public EstadosProtocoloEnum[] getSelectedEstados() {
    return selectedEstados;
  }

  public void setSelectedEstados(EstadosProtocoloEnum[] selectedEstados) {
    this.selectedEstados = selectedEstados;
  }

  public Date getInicioDate() {
    return inicioDate;
  }

  public void setInicioDate(Date inicioDate) {
    this.inicioDate = inicioDate;
  }

  public Date getTerminoDate() {
    return terminoDate;
  }

  public void setTerminoDate(Date terminoDate) {
    this.terminoDate = terminoDate;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public Map<String, List<DocumentoDTO>> getMapSelectedDocumentos() {
    return mapSelectedDocumentos;
  }

  public void setMapSelectedDocumentos(Map<String, List<DocumentoDTO>> mapSelectedDocumentos) {
    this.mapSelectedDocumentos = mapSelectedDocumentos;
  }

  public List<Map.Entry<String, List<DocumentoDTO>>> getDocumentosSelecionados() {
    return documentosSelecionados;
  }

  public List<DocumentoDTO> getSelectedDocumentos() {
    return selectedDocumentos;
  }

  public void setSelectedDocumentos(List<DocumentoDTO> selectedDocumentos) {
    this.selectedDocumentos = selectedDocumentos;
  }

  public void salvarParcial() {
//    List<DocumentoDTO> documentosSelecionados = new ArrayList<>();
//    for (List<DocumentoDTO> documentosArray : mapSelectedDocumentos.values()) {
//      documentosSelecionados.addAll(documentosArray);
//    }
    Protocolo savedProtocolo = service.salvarParcial(getItem(), selectedDocumentos);
    setItem(savedProtocolo);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    recuperarDadosRascunho();
  }
}
