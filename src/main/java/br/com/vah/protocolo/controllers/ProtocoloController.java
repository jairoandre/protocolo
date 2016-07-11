package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.exceptions.ProtocoloPersistException;
import br.com.vah.protocolo.service.AtendimentoService;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.ProtocoloService;
import br.com.vah.protocolo.util.DtoKeyMap;
import br.com.vah.protocolo.util.ViewUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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

  public static final String[] RELATIONS = {"itens", "historico", "comentarios"};

  private Setor setor;

  private EstadosProtocoloEnum[] estados = EstadosProtocoloEnum.values();

  private EstadosProtocoloEnum[] selectedEstados;

  private Date inicioDate;

  private Date terminoDate;

  private String comentario;

  private DtoKeyMap documentosNaoSelecionados;

  private DtoKeyMap documentosSelecionados;

  private Boolean showSumario;

  private Integer totalDocumentos;

  private Integer totalPrescricoes;

  private Integer totalEvolucoes;

  private Integer totalDescricoes;

  private Integer totalFolha;

  private Integer totalRegistros;

  private Integer totalDocumentosManuais;

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

  public void buscarDocumentosNaoSelecionados() {
    documentosNaoSelecionados = service.buscarDocumentosNaoSelecionados(getItem().getAtendimento(), inicioDate, terminoDate, session.getSetor(), getItem());
  }

  public void recuperarDadosRascunho() {
    Protocolo rascunho = service.buscarDadosRascunho(getItem().getAtendimento());
    if (rascunho == null) {
      if (session.getSetor() != null) {
        getItem().setOrigem(session.getSetor());
      }
    } else {
      setItem(rascunho);
    }
    Integer[] totais = service.contarDocumentos(getItem());
    showSumario = true;
    totalDocumentos = totais[0];
    totalPrescricoes = totais[1];
    totalEvolucoes = totais[2];
    totalFolha = totais[3];
    totalDescricoes = totais[4];
    totalRegistros = totais[5];
    totalDocumentosManuais = totais[6];
    documentosSelecionados = service.gerarDocumentosSelecionados(getItem());
    documentosNaoSelecionados = null;
  }

  public String getEditTitle() {
    if (getItem().getId() == null) {
      return "Enviar Documentos";
    } else {
      return String.format("Protocolo Nº. %d", getItem().getId());
    }
  }

  public void enviarProtocolo() {
    try{
      service.enviarProtocolo(getItem());
    } catch (ProtocoloBusinessException pbe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", pbe.getMessage()), false);
    }
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

  public Boolean getShowSumario() {
    return showSumario;
  }

  public Integer getTotalDocumentos() {
    return totalDocumentos;
  }

  public Integer getTotalPrescricoes() {
    return totalPrescricoes;
  }

  public Integer getTotalEvolucoes() {
    return totalEvolucoes;
  }

  public Integer getTotalDescricoes() {
    return totalDescricoes;
  }

  public Integer getTotalFolha() {
    return totalFolha;
  }

  public Integer getTotalRegistros() {
    return totalRegistros;
  }

  public Integer getTotalDocumentosManuais() {
    return totalDocumentosManuais;
  }

  public void salvarParcial() {
    List<DocumentoDTO> docs = new ArrayList<>();
    if (documentosNaoSelecionados != null) {
      docs = documentosNaoSelecionados.getSelecionados();
    }
    try {
      setItem(service.salvarParcial(getItem(), docs, session.getUser()));
      recuperarDadosRascunho();
      buscarDocumentosNaoSelecionados();
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Rascunho salvo protocolo nº <b>%s</b>.", getItem().getId())), true);
    }catch (ProtocoloPersistException ppe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro durante a persistência de dados.")), true);
    }

  }

  @Override
  public void onLoad() {
    super.onLoad();
    recuperarDadosRascunho();
  }

  public DtoKeyMap getDocumentosNaoSelecionados() {
    return documentosNaoSelecionados;
  }

  public void setDocumentosNaoSelecionados(DtoKeyMap documentosNaoSelecionados) {
    this.documentosNaoSelecionados = documentosNaoSelecionados;
  }

  public DtoKeyMap getDocumentosSelecionados() {
    return documentosSelecionados;
  }

  public void setDocumentosSelecionados(DtoKeyMap documentosSelecionados) {
    this.documentosSelecionados = documentosSelecionados;
  }
}
