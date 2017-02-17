package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.constants.RolesEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.Comentario;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.exceptions.ProtocoloPersistException;
import br.com.vah.protocolo.service.AtendimentoSrv;
import br.com.vah.protocolo.service.AbstractSrv;
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
public class ProtocoloCtrl extends AbstractController<Protocolo> {

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

  private EstadosProtocoloEnum acaoComentario;

  private Boolean renderComentariosDlg = false;

  private Boolean renderHistoricoDlg = false;

  private Boolean renderReceberDlg = false;

  private Boolean renderDocumentosDlg = false;

  private Protocolo protocoloToVisualize;

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

  public void changeOrigem() {
    getItem().getItens().clear();
    contarDocumentos();
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
    documentosSelecionados = service.gerarDocumentosSelecionados(att);
    setItem(att);
    renderReceberDlg = true;
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

  public void preOpenDocumentosItemDlg(Protocolo protocolo) {
    Protocolo att = service.initializeLists(protocolo);
    documentosSelecionados = service.gerarDocumentosSelecionados(att);
    renderDocumentosDlg = true;
    protocoloToVisualize = att;
  }

  public void preOpenDocumentosDlg(Protocolo protocolo) {
    Protocolo att = service.initializeLists(protocolo);
    documentosSelecionados = service.gerarDocumentosSelecionados(att);
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
    documentosSelecionados = service.gerarDocumentosSelecionados(getItem());
  }

  public void salvarNovoComentario() {
    Comentario newComment = new Comentario();
    newComment.setProtocolo(getItem());
    newComment.setAutor(session.getUser());
    newComment.setComentario(comentario);
    newComment.setData(new Date());
    getItem().getComentarios().add(newComment);
    if (EstadosProtocoloEnum.RECUSADO.equals(acaoComentario)) {
      saveAddingHistory(EstadosProtocoloEnum.RECUSADO);
    } else {
      saveAddingHistory(EstadosProtocoloEnum.COMENTARIO);
    }
    setItem(createNewItem());
  }

  public void searchDocumentos() {
    if (getItem().getOrigem() != null) {
      documentosNaoSelecionados =
          service.buscarDocumentosNaoSelecionados(getItem(), inicioDate, terminoDate);
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
      documentosNaoSelecionados.getSelecionados().forEach((dto) -> {
        dto.setProtocolo(getItem());
        getItem().getItens().add(dto.criarItemProtocolo());
      });
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

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() != null) {
      setItem(service.initializeLists(getItem()));
      prepareDocumentos();
    }
  }

  @Override
  public String path() {
    return "protocolo";
  }

  @Override
  public String getEntityName() {
    return "Protocolo";
  }

  public Boolean showRefuseButton(Protocolo protocolo) {
    return true;
  }

  public Boolean showEditButton(Protocolo protocolo) {
    if (RolesEnum.ADMINISTRATOR.equals(session.getUser().getRole())) {
      return true;
    }
    if (session.getSetor() == null) {
      return EstadosProtocoloEnum.RASCUNHO.equals(protocolo.getEstado()) && session.getSetor().equals(protocolo.getOrigem());
    }
    return false;
  }

  public Boolean disableResponderItem(Protocolo item) {
    return !EstadosProtocoloEnum.ENVIADO.equals(item.getEstado());
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

  public void selecionarTodosRecebimento() {
    for (Map.Entry<String, List<DocumentoDTO>> docEntry : documentosSelecionados.getList()) {
      if (docEntry.getValue() != null) {
        for (DocumentoDTO doc : docEntry.getValue()) {
          doc.setSelected(true);
        }
      }
    }
  }

  public void selecionarTodosDocumentos() {
    documentosNaoSelecionados.getList().forEach((item) -> item.getValue().forEach((value) -> value.setSelected(true)));
  }
}
