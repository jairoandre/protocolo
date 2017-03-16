package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.*;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.exceptions.ProtocoloPersistException;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.AtendimentoSrv;
import br.com.vah.protocolo.service.ProtocoloSrv;
import br.com.vah.protocolo.util.DtoKeyMap;
import br.com.vah.protocolo.util.ViewUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jairoportela on 15/06/2016.
 */
@Named
@ViewScoped
public class ProtocoloCtrl extends AbstractCtrl<Protocolo> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ProtocoloSrv service;

  private
  @Inject
  SessionController session;

  private
  @Inject
  AtendimentoSrv atendimentoSrv;

  private TipoDocumentoEnum[] tiposDocManual = TipoDocumentoEnum.documentosManuais;

  public static final String[] RELATIONS = {"itens", "historico", "comentarios"};

  private SetorProtocolo setor;

  private EstadosProtocoloEnum[] estados = EstadosProtocoloEnum.values();

  private EstadosProtocoloEnum[] selectedEstados;

  private Map<ProtocoloFieldEnum, Object> mapFiltros = new HashMap<>();

  private Date inicioDate;

  private Date terminoDate;

  private String comentario;

  private DtoKeyMap documentosNaoSelecionados;

  private DtoKeyMap documentosSelecionados;

  private DtoKeyMap documentosToVisualize;

  private Boolean showSumario;

  private Integer totalDocumentos;

  private Integer totalPrescricoes;

  private Integer totalEvolucoes;

  private Integer totalDescricoes;

  private Integer totalFolha;

  private Integer totalRegistros;

  private Integer totalDocumentosManuais;

  private EstadosProtocoloEnum acaoComentario;

  private Boolean renderComentariosDlg = false;

  private Boolean renderHistoricoDlg = false;

  private Boolean renderReceberDlg = false;

  private Boolean renderDocumentosDlg = false;

  private Protocolo protocoloToVisualize;

  private String listaContas;

  private Convenio convenio;

  private DocumentoProtocolo docManualToAdd;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service, RELATIONS);
    prepareSearch();
  }

  @Override
  public void prepareSearch() {
    resetSearchParams();
    String regex = "[0-9]+";
    if (getSearchTerm() != null && getSearchTerm().matches(regex)) {
      mapFiltros.put(ProtocoloFieldEnum.ATENDIMENTO, Long.valueOf(getSearchTerm()));
      setSearchParam("atendimento", Long.valueOf(getSearchTerm()));
    } else if (getSearchTerm() != null && !getSearchTerm().isEmpty()) {
      mapFiltros.put(ProtocoloFieldEnum.PACIENTE, getSearchTerm());
      setSearchParam("paciente", getSearchTerm());
    }
    if (setor != null) {
      mapFiltros.put(ProtocoloFieldEnum.SETOR, setor.getTitle());
      setSearchParam("setor", setor);
    }
    if (session.getSetor() != null) {
      setSearchParam("setor", session.getSetor());
    }
    if (selectedEstados != null && selectedEstados.length > 0) {
      mapFiltros.put(ProtocoloFieldEnum.ESTADO, selectedEstados);
      setSearchParam("estados", selectedEstados);
    }
    if (inicioDate != null || terminoDate != null) {
      mapFiltros.put(ProtocoloFieldEnum.DATA, new Date[]{inicioDate, terminoDate});
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

  public void changeOrigem() {
    getItem().getItens().clear();
    documentosNaoSelecionados = null;
    contarDocumentos();
  }

  public void definirContaFaturamento(RegFaturamento conta) {
    if (conta.equals(getItem().getContaFaturamento())) {
      getItem().setContaFaturamento(null);
    } else {
      getItem().setContaFaturamento(conta);
    }
  }

  public void clearSetor() {
    setor = null;
    prepareSearch();
  }

  public void saveAddingHistory(EstadosProtocoloEnum acao) {
    getItem().setEstado(acao);
    getItem().setDataResposta(new Date());
    service.addHistorico(getItem(), session.getUser(), acao);
    doSave();
  }

  public void receber() {
    renderReceberDlg = false;
    saveAddingHistory(EstadosProtocoloEnum.RECEBIDO);
    setItem(createNewItem());
  }

  public void preReceber(Protocolo protocolo) {
    Protocolo att = service.initializeLists(protocolo);
    documentosSelecionados = service.gerarDocumentosSelecionados(att, false);
    setItem(att);
    renderReceberDlg = true;
  }

  public void visualizarDoc(DocumentoDTO dto) {
    Protocolo att = service.initializeLists(dto.getFilho());
    protocoloToVisualize = att;
    documentosToVisualize = service.gerarDocumentosSelecionados(att, false);
  }

  public void closeReceberDlg() {
    renderReceberDlg = false;
  }

  public void preRecusar(Protocolo protocolo) {
    acaoComentario = EstadosProtocoloEnum.RECUSADO;
    setItem(service.initializeLists(protocolo));
  }

  public void preComentario(Protocolo protocolo) {
    acaoComentario = EstadosProtocoloEnum.COMENTARIO;
    setItem(service.initializeLists(protocolo));
  }

  public void preComentarioEdicao() {
    acaoComentario = EstadosProtocoloEnum.COMENTARIO;
  }

  public void preOpenDocumentosDlg(Protocolo protocolo) {
    Protocolo att = service.initializeLists(protocolo);
    documentosSelecionados = service.gerarDocumentosSelecionados(att, false);
    renderDocumentosDlg = true;
    setItem(att);
  }

  public void preOpenComentariosDlg(Protocolo protocolo) {
    Protocolo att = service.initializeLists(protocolo);
    renderComentariosDlg = true;
    setItem(att);
  }

  public void closeDocumentosItemDlg() {
    renderDocumentosDlg = false;
  }


  public void closeDocumentosDlg() {
    renderDocumentosDlg = false;
    setItem(createNewItem());
  }

  public void closeComentariosDlg() {
    renderComentariosDlg = false;
    setItem(createNewItem());
  }


  public void preHistorico(Protocolo protocolo) {
    renderHistoricoDlg = true;
    setItem(service.initializeLists(protocolo));
  }

  public void closeHistorico() {
    renderHistoricoDlg = false;
  }

  public void recuperarDadosRascunho() {
    Protocolo rascunho = service.buscarDadosRascunho(getItem());
    if (rascunho == null) {
      if (session.getSetor() != null) {
        getItem().setOrigem(session.getSetor());
      }
    } else {
      setItem(service.initializeLists(rascunho));
    }
    prepareDocumentos();
  }

  public void prepareDocumentos() {
    contarDocumentos();
    documentosSelecionados = service.gerarDocumentosSelecionados(getItem(), false);
  }

  public void salvarNovoComentarioEdit() {
    Comentario newComment = new Comentario();
    newComment.setProtocolo(getItem());
    newComment.setAutor(session.getUser());
    newComment.setComentario(comentario);
    newComment.setData(new Date());
    getItem().getComentarios().add(newComment);
    if (EstadosProtocoloEnum.RECUSADO.equals(acaoComentario)) {
      saveAddingHistory(EstadosProtocoloEnum.RECUSADO);
    } else {
      saveAddingHistory(getItem().getEstado());
    }
  }

  public void salvarNovoComentario() {
    salvarNovoComentarioEdit();
    setItem(createNewItem());
  }

  public void searchDocumentos() {
    if (getItem().getOrigem() != null) {
      documentosNaoSelecionados =
          service.buscarDocumentosNaoSelecionados(getItem(), inicioDate, terminoDate, convenio, listaContas);
    }
  }

  private List<RegFaturamento> contas;

  public List<RegFaturamento> getContas() {
    return contas;
  }

  private void contarDocumentos() {
    Integer[] totais = service.contarDocumentos(getItem());
    showSumario = true;
    totalDocumentos = totais[0];
    totalPrescricoes = totais[1];
    totalEvolucoes = totais[2];
    totalFolha = totais[3];
    totalDescricoes = totais[4];
    totalRegistros = totais[5];
    totalDocumentosManuais = totais[6];
    contas = new ArrayList<>(service.inferirContas(getItem()));
  }

  public String getEditTitle() {
    if (getItem().getId() == null) {
      return "Enviar Documentos";
    } else {
      return String.format("Protocolo Nº. %d", getItem().getId());
    }
  }

  public void enviarProtocolo() {
    try {
      service.addHistorico(getItem(), session.getUser(), EstadosProtocoloEnum.ENVIADO);
      service.enviarProtocolo(getItem());
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Protocolo enviado com sucesso."), false);
    } catch (ProtocoloBusinessException pbe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", pbe.getMessage()), false);
    }
  }

  public void removeDoc(DocumentoDTO doc) {
    getItem().getItens().remove(doc.getItemProtocolo());
    if (documentosNaoSelecionados != null) {
      documentosNaoSelecionados.addDto(doc);
    }
    prepareDocumentos();
  }

  @Override
  public AbstractSrv<Protocolo> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Protocolo createNewItem() {
    Protocolo protocolo = new Protocolo();
    SetorProtocolo setor = session.getSetor();
    if (setor != null) {
      protocolo.setOrigem(setor);
    }
    return protocolo;
  }

  public void changeInicio() {
    if (inicioDate != null) {
      Calendar cld = Calendar.getInstance();
      cld.setTime(inicioDate);
      cld.add(Calendar.DAY_OF_MONTH, 1);
      terminoDate = cld.getTime();
    }
  }

  public void vincularDocumentos() {
    if (documentosNaoSelecionados != null) {
      documentosNaoSelecionados.getSelecionados().forEach((dto) -> service.addIfNoExists(getItem(), dto));
      documentosNaoSelecionados = documentosNaoSelecionados.getNotSelectedMap();
      prepareDocumentos();
    }
  }

  public void salvarParcial() {
    try {
      setItem(service.salvarParcial(getItem(), session.getUser()));
      setItem(service.initializeLists(getItem()));
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Rascunho salvo protocolo nº <b>%s</b>.", getItem().getId())), true);
    } catch (ProtocoloPersistException ppe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro durante a persistência de dados.")), true);
    }

  }

  public void preAddDocManual() {
    docManualToAdd = new DocumentoProtocolo();
    docManualToAdd.setDataHoraCriacao(new Date());
  }

  public void addDocManual() {
    ItemProtocolo itemProtocolo = new ItemProtocolo();
    itemProtocolo.setProtocolo(getItem());
    itemProtocolo.setDocumento(docManualToAdd);
    getItem().getItens().add(itemProtocolo);
    prepareDocumentos();
    docManualToAdd = null;
  }

  public DocumentoProtocolo getDocManualToAdd() {
    return docManualToAdd;
  }

  public void setDocManualToAdd(DocumentoProtocolo docManualToAdd) {
    this.docManualToAdd = docManualToAdd;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() != null) {
      setItem(service.initializeLists(getItem()));
      prepareDocumentos();
    }
  }

  public void selecionarTodosRecebimento() {
    for (Map.Entry<String, List<DocumentoDTO>> docEntry : documentosSelecionados.getList()) {
      if (docEntry.getValue() != null) {
        for (DocumentoDTO doc : docEntry.getValue()) {
          doc.setSelected(true);
        }
      }
    }
  }

  public void removeFilterItem(ProtocoloFieldEnum filtro) {
    switch (filtro) {
      case ATENDIMENTO:
        mapFiltros.remove(ProtocoloFieldEnum.ATENDIMENTO);
        setSearchTerm(null);
        break;
      case DATA:
        inicioDate = null;
        terminoDate = null;
        mapFiltros.remove(ProtocoloFieldEnum.DATA);
        break;
      case PACIENTE:
        setSearchTerm(null);
        mapFiltros.remove(ProtocoloFieldEnum.PACIENTE);
        break;
      case ESTADO:
        selectedEstados = null;
        mapFiltros.remove(ProtocoloFieldEnum.ESTADO);
        break;
      case SETOR:
        setor = null;
        mapFiltros.remove(ProtocoloFieldEnum.SETOR);
        break;
      default:
        break;
    }
    prepareSearch();
  }

  @Override
  public String path() {
    return "protocolo";
  }

  @Override
  public String getEntityName() {
    return "Protocolo";
  }

  public Boolean showEditButton(Protocolo protocolo) {
    if (RolesEnum.ADMINISTRATOR.equals(session.getUser().getRole())) {
      return true;
    }
    if (session.getSetor() != null && session.getSetor().equals(protocolo.getOrigem())) {
      return EstadosProtocoloEnum.RASCUNHO.equals(protocolo.getEstado()) || EstadosProtocoloEnum.RECUSADO.equals(protocolo.getEstado());
    }
    return false;
  }

  public Boolean getCanSendOrSave() {
    return getEditing() && getItem().getOrigem() != null && (EstadosProtocoloEnum.RASCUNHO.equals(getItem().getEstado()) || EstadosProtocoloEnum.RECUSADO.equals(getItem().getEstado()));
  }

  public Boolean disableResponderItem(Protocolo item) {
    return !EstadosProtocoloEnum.ENVIADO.equals(item.getEstado());
  }

  public Boolean renderResponserItem(Protocolo item) {
    return session.getUser().getRole().equals(RolesEnum.ADMINISTRATOR) || session.getSetor().equals(item.getDestino());
  }

  public SetorProtocolo getSetor() {
    return setor;
  }

  public void setSetor(SetorProtocolo setor) {
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

  public DtoKeyMap getDocumentosToVisualize() {
    return documentosToVisualize;
  }

  public Boolean getRenderComentariosDlg() {
    return renderComentariosDlg;
  }

  public Boolean getRenderHistoricoDlg() {
    return renderHistoricoDlg;
  }

  public Boolean getRenderReceberDlg() {
    return renderReceberDlg;
  }

  public Boolean getRenderDocumentosDlg() {
    return renderDocumentosDlg;
  }

  public EstadosProtocoloEnum getAcaoComentario() {
    return acaoComentario;
  }

  public Protocolo getProtocoloToVisualize() {
    return protocoloToVisualize;
  }

  public void setProtocoloToVisualize(Protocolo protocoloToVisualize) {
    this.protocoloToVisualize = protocoloToVisualize;
  }

  public Boolean getRegistrarRecebBtnDisabled() {
    if (documentosSelecionados != null) {
      for (Map.Entry<String, List<DocumentoDTO>> docEntry : documentosSelecionados.getList()) {
        if (docEntry.getValue() != null) {
          for (DocumentoDTO doc : docEntry.getValue()) {
            if (!doc.getSelected()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public void selecionarTodosDocumentos() {
    documentosNaoSelecionados.getList().forEach((item) -> item.getValue().forEach((value) -> value.setSelected(true)));
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

  public String interpolarTextoMapFiltros(ProtocoloFieldEnum filtro) {
    return ProtocoloFieldEnum.getText(mapFiltros, filtro);
  }

  public List<ProtocoloFieldEnum> getFieldsMap() {
    return new ArrayList<>(mapFiltros.keySet());
  }

  public TipoDocumentoEnum[] getTiposDocManual() {
    return tiposDocManual;
  }
}
