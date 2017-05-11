package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.*;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.exceptions.ProtocoloPersistException;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.AtendimentoSrv;
import br.com.vah.protocolo.service.ProtocoloSrv;
import br.com.vah.protocolo.util.DtoKeyEntry;
import br.com.vah.protocolo.util.DtoKeyMap;
import br.com.vah.protocolo.util.ViewUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
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

  private List<DocumentoDTO> documentos;

  private DtoKeyMap documentosKeyMap = new DtoKeyMap();

  private DtoKeyMap documentosKeyMapPS = new DtoKeyMap();

  private DtoKeyMap documentosToVisualize = new DtoKeyMap();

  private Boolean showSumario;

  private Integer totalDocumentos;

  private Integer totalPrescricoes;

  private Integer totalEvolucoes;

  private Integer totalDescricoes;

  private Integer totalFolha;

  private Integer totalRegistros;

  private Integer totalDocumentosManuais;

  private Integer totalEvolucaoAnotacao;

  private Integer totalDocumentosProntuarios;

  private EstadosProtocoloEnum acaoComentario;

  private Boolean renderComentariosDlg = false;

  private Boolean renderHistoricoDlg = false;

  private Boolean renderDocumentosDlg = false;

  private Boolean recebendo = false;

  private Protocolo protocoloToVisualize;

  private String listaAtendimentos;

  private String listaContas;

  private Convenio convenio;

  private DocumentoProtocolo docManualToAdd;

  private List<CaixaEntrada> caixasVinculadas = new ArrayList<>();

  private List<CaixaEntrada> caixasRemovidas = new ArrayList<>();

  private String atendimentos;

  private Boolean showSelecionarDocsPSDlg = false;

  private Protocolo protocoloPS;

  private Boolean visualizarRecebidos = false;

  private Map<Long, Protocolo> protocolosPSMap;

  private Boolean showAssinalarPendenciasDlg = false;

  public ProtocoloCtrl() {
  }

  public ProtocoloCtrl(Logger logger, ProtocoloSrv service, SessionController session, AtendimentoSrv atendimentoSrv) {
    super();
    this.logger = logger;
    this.service = service;
    this.session = session;
    this.atendimentoSrv = atendimentoSrv;
  }

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
    setSearchParam("visualizarRecebidos", visualizarRecebidos);
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

  public Boolean isSecretaria() {
    SetorProtocolo origem = getItem().getOrigem();
    if (origem == null) {
      return false;
    } else {
      return SetorNivelEnum.SECRETARIA.equals(origem.getNivel());
    }

  }

  /*PRONTO SOCORRO*/
  public Boolean isProntoSocorro() {
    SetorProtocolo origem = getItem().getOrigem();
    if (origem == null) {
      return false;
    } else {
      return SetorNivelEnum.PRONTO_SOCORRO.equals(origem.getNivel());
    }

  }

  public void changeOrigem() {
    getItem().getItens().clear();
    if (isSecretaria()) {
      contarDocumentos();
    }
    documentosKeyMap = new DtoKeyMap();
    documentosKeyMap.compile(this);
  }

  public void saveAddingHistory(AcaoHistoricoEnum acao) {
    setItem(service.salvarProtocolo(getItem(), session.getUser(), acao, caixasVinculadas, caixasRemovidas));
  }

  public void receber() {
    saveAddingHistory(AcaoHistoricoEnum.RECEBIMENTO);
    setItem(createNewItem());
    recebendo = false;
  }

  public void preReceber(Protocolo protocolo) {
    documentosKeyMap = new DtoKeyMap();
    Protocolo att = service.initializeLists(protocolo);
    documentosKeyMap.addAll(service.gerarListaDTO(att, false, false), false);
    documentosKeyMap.compile(this);
    setItem(att);
    contarDocumentos();
    recebendo = true;
    renderDocumentosDlg = true;
  }

  private Boolean isOrigemSecretaria() {
    return SetorNivelEnum.SECRETARIA.equals(getItem().getOrigem().getNivel());
  }

  public void visualizarDoc(DocumentoDTO dto) {
    Protocolo att = service.initializeLists(dto.getFilho());
    protocoloToVisualize = att;
    documentosToVisualize = new DtoKeyMap();
    documentosToVisualize.addAll(service.gerarListaDTO(att, isOrigemSecretaria(), false), false);
    documentosToVisualize.compile(this);
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
    setItem(att);
    contarDocumentos();
    documentosKeyMap = new DtoKeyMap();
    documentosKeyMap.addAll(service.gerarListaDTO(att, isOrigemSecretaria(), false), false);
    documentosKeyMap.compile(this);
    renderDocumentosDlg = true;
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
    recebendo = false;
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
    if (getItem().getId() == null) {
      Protocolo rascunho = service.buscarDadosRascunho(getItem());
      if (rascunho == null) {
        if (session.getSetor() != null) {
          getItem().setOrigem(session.getSetor());
        }
      } else {
        setItem(service.initializeLists(rascunho));
      }
    } else {
      Protocolo newProtocolo = createNewItem();
      newProtocolo.setOrigem(getItem().getOrigem());
      newProtocolo.setOrigem(getItem().getDestino());
      newProtocolo.setAtendimento(getItem().getAtendimento());
      setItem(newProtocolo);
    }
    prepareDocumentos();
  }

  public void prepareDocumentos() {
    if (isSecretaria()) {
      contarDocumentos();
    }
    documentosKeyMap.addAll(service.gerarListaDTO(getItem(), isOrigemSecretaria(), true), true);
    documentosKeyMap.compile(this);
  }

  public void salvarNovoComentarioEdit() {
    Comentario newComment = new Comentario();
    newComment.setProtocolo(getItem());
    newComment.setAutor(session.getUser());
    newComment.setComentario(comentario);
    newComment.setData(new Date());
    getItem().getComentarios().add(newComment);
    if (EstadosProtocoloEnum.RECUSADO.equals(acaoComentario)) {
      saveAddingHistory(AcaoHistoricoEnum.RECUSA);
    } else {
      saveAddingHistory(null);
    }
  }

  public void salvarNovoComentario() {
    salvarNovoComentarioEdit();
    setItem(createNewItem());
  }

  public void searchDocumentos() {
    if (getItem().getOrigem() != null) {
      try {
        if (SetorNivelEnum.PRONTO_SOCORRO.equals(getItem().getOrigem().getNivel())) {
          Map<String, Object> map = atendimentoSrv.buscarAtendimentosProntoSocorro(atendimentos);
          documentos = (List<DocumentoDTO>) map.get("dtos");
          documentosKeyMap.addAll(documentos, false);
          documentosKeyMap.compile(this);
        } else {
          documentos = service.buscarDocumentos(getItem(), convenio, listaContas, listaAtendimentos);
          aferirContas();
          documentosKeyMap.addAll(documentos, false);
          documentosKeyMap.compile(this);
        }
      } catch (ProtocoloBusinessException e) {
        addMsg(FacesMessage.SEVERITY_WARN, e.getMessage());
      }
    }
  }

  private List<RegFaturamento> contas;

  public List<RegFaturamento> getContas() {
    return contas;
  }

  public void aferirContas() {
    contas = new ArrayList<>(service.inferirContas(getItem(), documentos));
    if (contas.size() == 1) {
      getItem().setContaFaturamento(contas.iterator().next());
    }
  }

  public void contarDocumentos() {
    Integer[] totais = service.contarDocumentos(getItem());
    showSumario = true;
    totalDocumentos = totais[0];
    totalPrescricoes = totais[1];
    totalEvolucoes = totais[2];
    totalFolha = totais[3];
    totalDescricoes = totais[4];
    totalRegistros = totais[5];
    totalDocumentosManuais = totais[6];
    totalEvolucaoAnotacao = totais[7];
    totalDocumentosProntuarios = totais[8];
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
      Protocolo protocolo = service.enviarProtocolo(getItem(), session.getUser(), caixasVinculadas, caixasRemovidas);
      setItem(service.initializeLists(protocolo));
      setEditing(false);
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Protocolo enviado com sucesso."), false);
    } catch (ProtocoloBusinessException pbe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", pbe.getMessage()), false);
    }
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
    if (getItem().getInicio() != null) {
      Calendar cld = Calendar.getInstance();
      cld.setTime(getItem().getInicio());
      cld.add(Calendar.DAY_OF_MONTH, 1);
      getItem().setFim(cld.getTime());
    }
  }

  public void salvarParcial() {
    try {
      Protocolo protocolo = service.salvarProtocolo(getItem(), session.getUser(), null, caixasVinculadas, caixasRemovidas);
      setItem(service.initializeLists(protocolo));
      addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Rascunho salvo protocolo nº <b>%s</b>.", getItem().getId())), true);
    } catch (ProtocoloPersistException ppe) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro durante a persistência de dados.")), true);
    }

  }

  public void preAddDocManual() {
    docManualToAdd = new DocumentoProtocolo();
    docManualToAdd.setDataHoraCriacao(new Date());
  }

  private List<SelectItem> docsCombo;

  private String docText;

  public String getDocText() {
    return docText;
  }

  public void setDocText(String docText) {
    this.docText = docText;
  }

  public List<SelectItem> getDocsCombo() {
    return docsCombo;
  }

  public void fecharAssinalarPendenciasDlg() {
    showAssinalarPendenciasDlg = false;
  }

  public void preAssinalarPendencias(DocumentoDTO dto) {
    showAssinalarPendenciasDlg = true;
    preSelecionarDocumentosPS(dto);
    final List<SelectItem> docsSelectItem = new ArrayList<>();
    Protocolo protocoloPS = documentosKeyMapPS.getCtrl().getItem();
    if (protocoloPS != null) {
      documentosKeyMapPS.getList().forEach((keyEntry) -> {
        keyEntry.getList().forEach((i) -> {
          SelectItem selectItem = new SelectItem();
          DocumentoProtocolo doc = i.getDocumento();
          String val = String.format("%s - %d", doc.getTipo().getLabel(), doc.getCodigo());
          selectItem.setLabel(val);
          selectItem.setValue(val);
          docsSelectItem.add(selectItem);
        });
      });
    }
    docsCombo = docsSelectItem;
  }

  public void preAddDocManualPS(DocumentoDTO dto) {
    preSelecionarDocumentosPS(dto);
    documentosKeyMapPS.getCtrl().preAddDocManual();
  }

  public void addDocManual() {
    ItemProtocolo itemProtocolo = new ItemProtocolo();
    itemProtocolo.setProtocolo(getItem());
    itemProtocolo.setDocumento(docManualToAdd);
    getItem().getItens().add(itemProtocolo);
    documentosKeyMap.add(new DocumentoDTO(itemProtocolo, true));
    documentosKeyMap.compile(this);
    contarDocumentos();
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
    protocolosPSMap = new HashMap<>();
    if (getItem().getId() != null) {
      setItem(service.initializeLists(getItem()));
      contas = new ArrayList<>(service.inferirContas(getItem(), null));
      prepareDocumentos();
    }
  }

  public void checkDto(DocumentoDTO dto) {
    dto.setSelected(true);
    if (getEditing()) {
      CaixaEntrada caixa = dto.getCaixa();
      if (caixa != null) {
        caixasVinculadas.add(caixa);
        caixasRemovidas.remove(caixa);
      }
      getItem().getItens().add(dto.criarItemProtocoloSeNecessario(getItem()));
    }
  }

  public void uncheckDto(DocumentoDTO dto) {
    dto.setSelected(false);
    if (getEditing()) {
      CaixaEntrada caixa = dto.getCaixa();
      if (dto.getCaixa() != null) {
        caixasVinculadas.remove(caixa);
        caixasRemovidas.add(caixa);
      }
      getItem().getItens().remove(dto.getItemProtocolo());
    }
  }

  public void selectAll() {
    documentosKeyMap.getList().forEach((docEntry) -> {
      docEntry.getSelecteds().clear();
      if (docEntry.getEntry().getValue() != null) {
        for (DocumentoDTO dto : docEntry.getEntry().getValue()) {
          docEntry.getSelecteds().add(dto);
          checkDto(dto);
        }
      }
    });
    contarDocumentos();
  }

  public void unselectAll() {
    documentosKeyMap.getList().forEach((docEntry) -> {
      docEntry.getSelecteds().clear();
      if (docEntry.getEntry().getValue() != null) {
        for (DocumentoDTO doc : docEntry.getEntry().getValue()) {
          doc.setSelected(false);
          if (getEditing()) {
            uncheckDto(doc);
          }
        }
      }
    });
    contarDocumentos();
  }

  public void removeDocPendente() {
    String textoAtual = getItem().getDocsPendentes();
    StringJoiner joiner = new StringJoiner(";\n");
    if (textoAtual != null && textoAtual.length() > 0) {
      String[] values = textoAtual.split(";\n");
      for (String value : values) {
        if (!value.equals(docText)) {
          joiner.add(value);
        }
      }
    }
    getItem().setDocsPendentes(joiner.toString());
  }

  public void addDocPendente() {
    String textoAtual = getItem().getDocsPendentes();
    Boolean addText = true;
    StringJoiner joiner = new StringJoiner(";\n");
    if (textoAtual != null && textoAtual.length() > 0) {
      String[] values = textoAtual.split(";\n");
      for (String value : values) {
        joiner.add(value);
        if (value.equals(docText)) {
          addText = false;
        }
      }
    }
    if (addText) {
      joiner.add(docText);
    }
    getItem().setDocsPendentes(joiner.toString());
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

  public DtoKeyMap getDocumentosKeyMap() {
    return documentosKeyMap;
  }

  public void setDocumentosKeyMap(DtoKeyMap documentosKeyMap) {
    this.documentosKeyMap = documentosKeyMap;
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

  public Boolean getRenderDocumentosDlg() {
    return renderDocumentosDlg;
  }

  public Boolean getRecebendo() {
    return recebendo;
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
    if (documentosKeyMap != null) {
      for (DtoKeyEntry docEntry : documentosKeyMap.getList()) {
        if (docEntry.getEntry().getValue() != null) {
          for (DocumentoDTO doc : docEntry.getEntry().getValue()) {
            if (!doc.getSelected()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public void fecharSelecionarPSDlg() {
    showSelecionarDocsPSDlg = false;
    Protocolo item = documentosKeyMapPS.getCtrl().getItem();
    item.setComPendencias(false);
    documentosKeyMapPS.getList().forEach((i) -> {
      i.getList().forEach((dto) -> {
        if (!dto.getSelected()) {
          item.setComPendencias(true);
        }
      });
    });
  }

  public void preSelecionarDocumentosPS(DocumentoDTO dto) {
    showSelecionarDocsPSDlg = true;
    documentosKeyMapPS = new DtoKeyMap();
    if (dto.getItemProtocolo() == null) {
      Atendimento atd = new Atendimento();
      atd.setId(dto.getCodigo());

      Protocolo filho = new Protocolo();
      filho.setAtendimento(atd);
      filho.setOrigem(getItem().getOrigem());

      ItemProtocolo itemProtocolo = new ItemProtocolo();
      itemProtocolo.setProtocolo(getItem());
      itemProtocolo.setFilho(filho);

      dto.setItemProtocolo(itemProtocolo);
    }
    protocoloPS = dto.getItemProtocolo().getFilho();
    if (protocoloPS == null) {
      protocoloPS = new Protocolo();
      Atendimento atd = new Atendimento();
      atd.setId(dto.getCodigo());
      protocoloPS.setAtendimento(atd);
      protocoloPS.setEstado(EstadosProtocoloEnum.PRONTO_SOCORRO);
      protocoloPS.setOrigem(getItem().getOrigem());
      dto.getItemProtocolo().setFilho(protocoloPS);
    }
    if (protocoloPS.getId() == null) {
      Calendar cld = Calendar.getInstance();
      protocoloPS.setFim(cld.getTime());
      cld.add(Calendar.DAY_OF_MONTH, -2);
      protocoloPS.setInicio(cld.getTime());

    } else if (protocolosPSMap.get(protocoloPS.getId()) == null) {
      protocoloPS = service.initializeLists(protocoloPS);
      protocolosPSMap.put(protocoloPS.getId(), protocoloPS);
    } else {
      protocoloPS = protocolosPSMap.get(protocoloPS.getId());
    }
    dto.getItemProtocolo().setFilho(protocoloPS);
    protocoloPS.setEstado(EstadosProtocoloEnum.PRONTO_SOCORRO);
    try {
      List<DocumentoDTO> docs = service.buscarDocumentos(protocoloPS, null, null, null);
      documentosKeyMapPS.addAll(docs, false);
    } catch (Exception e) {
      addMsg(FacesMessage.SEVERITY_WARN, "Erro na busca de documentos.");
    }
    documentosKeyMapPS.addAll(service.gerarListaDTO(protocoloPS, false, false), true);
    ProtocoloCtrl ctrl = new ProtocoloCtrl(logger, service, session, atendimentoSrv);
    ctrl.setItem(protocoloPS);
    ctrl.setDocumentosKeyMap(documentosKeyMapPS);
    documentosKeyMapPS.compile(ctrl);
  }

  public String getListaAtendimentos() {
    return listaAtendimentos;
  }

  public void setListaAtendimentos(String listaAtendimentos) {
    this.listaAtendimentos = listaAtendimentos;
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

  public Integer getTotalEvolucaoAnotacao() {
    return totalEvolucaoAnotacao;
  }

  public Integer getTotalDocumentosProntuarios() {
    return totalDocumentosProntuarios;
  }

  public String getAtendimentos() {
    return atendimentos;
  }

  public void setAtendimentos(String atendimentos) {
    this.atendimentos = atendimentos;
  }

  public DtoKeyMap getDocumentosKeyMapPS() {
    return documentosKeyMapPS;
  }

  public Boolean getShowSelecionarDocsPSDlg() {
    return showSelecionarDocsPSDlg;
  }

  public Protocolo getProtocoloPS() {
    return protocoloPS;
  }

  public Boolean getVisualizarRecebidos() {
    return visualizarRecebidos;
  }

  public void setVisualizarRecebidos(Boolean visualizarRecebidos) {
    this.visualizarRecebidos = visualizarRecebidos;
  }

  public Boolean getShowAssinalarPendenciasDlg() {
    return showAssinalarPendenciasDlg;
  }

  public void setShowAssinalarPendenciasDlg(Boolean showAssinalarPendenciasDlg) {
    this.showAssinalarPendenciasDlg = showAssinalarPendenciasDlg;
  }
}
