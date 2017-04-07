package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.service.CaixaEntradaSrv;
import br.com.vah.protocolo.service.CaixaSrv;
import br.com.vah.protocolo.service.ProtocoloSrv;
import br.com.vah.protocolo.service.SameSrv;
import br.com.vah.protocolo.util.DtoKeyMap;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private
    @Inject
    CaixaSrv caixaSrv;

    private
    @Inject
    SameSrv sameSrv;

    private
    @Inject
    SessionController sessionCtrl;

    private List<DocumentoDTO> pendentes;

    private List<DocumentoDTO> selecteds;

    private List<DocumentoDTO> filtereds;

    private Protocolo protocoloToVisualize;

    private DtoKeyMap documentosToVisualize;

    private String listaContas;

    private Convenio convenio;

    private Integer tipoArquivamento;

    private Caixa caixa;

    private Armario armario;

    private Envelope envelope;

    private List<String> linhas;

    private List<String> colunas;

    private String linha;

    private String coluna;

    private List<Envelope> envelopes;

    public void navigate(Integer route) {
        this.route = route;
    }

    public void buscarPendentes() {
        List<CaixaEntrada> naoVinculados = caixaEntradaSrv.buscarDocumentosPendentes(null);
        final List<DocumentoDTO> dtos = new ArrayList<>();
        naoVinculados.forEach((caixa) -> dtos.add(new DocumentoDTO(caixa)));
        pendentes = dtos;
        selecteds = null;
        filtereds = null;
        envelopes = null;
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

    private void resetDadosLocalidade() {
        linhas = null;
        colunas = null;
        linha = null;
        coluna = null;
        caixa = null;
        envelopes = null;
        envelope = null;
        armario = null;
    }

    public void changeTipo() {
        resetDadosLocalidade();
    }

    public void changeArmario() {
        linhas = null;
        colunas = null;
        linha = null;
        coluna = null;
        if (armario != null) {
            linhas = new ArrayList<>();
            colunas = new ArrayList<>();
            for (int i = 1; i <= armario.getLinhas(); i++) {
                linhas.add(String.format("%d", i));
            }
            int charConst = 65;
            for (int j = 0; j <= armario.getColunas(); j++) {
                int currChar = charConst + j;
                if (currChar > 90) {
                    currChar = charConst;
                }
                colunas.add(Character.toString((char) currChar));
            }
        }
    }

    // PENDENTES

    public void pendentesMarcarTodos() {
        if (pendentes != null) {
            pendentes.forEach((pendente) -> pendente.setSelected(true));
            selecteds = pendentes;
        }
    }

    public void pendentesDesmarcarTodos() {
        if (pendentes != null) {
            pendentes.forEach((pendente) -> pendente.setSelected(false));
            selecteds = null;
        }
    }

    // GETTERS AND SETTERS

    public Integer getPendentesSelectCount() {
        if (pendentes == null) {
            return 0;
        } else {
            return pendentes.stream().map(DocumentoDTO::getSelectedInteger).reduce(0, (a, b) -> a + b);
        }
    }

    public void changeCaixa() {
        envelope = null;
        envelopes = null;
        if (caixa != null) {
            Caixa att = caixaSrv.loadLists(caixa.getId());
            envelopes = att.getEnvelopes();
        }
    }

    public void arquivarMarcados() {
        if (selecteds != null) {
            if (tipoArquivamento == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione o tipo de arquivamento");
                return;
            } else {
                if (tipoArquivamento == 0) {
                    Integer msgsCount = 0;
                    if (armario == null) {
                        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione o tipo de arquivamento");
                        msgsCount++;
                    }
                    if (linha == null) {
                        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a linha");
                        msgsCount++;
                    }
                    if (coluna == null) {
                        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a coluna");
                        msgsCount++;
                    }
                    if (msgsCount > 0) {
                        return;
                    }
                } else {
                    Integer msgsCount = 0;
                    if (caixa == null) {
                        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe a caixa");
                        msgsCount++;
                    }
                    if (envelope == null) {
                        addMsg(FacesMessage.SEVERITY_WARN, "Atenção", "Informe o envelope");
                        msgsCount++;
                    }
                    if (msgsCount > 0) {
                        return;
                    }
                }
            }
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("tipo", tipoArquivamento);
                if (armario != null) {
                    params.put("id", armario.getId());
                }
                if (envelope != null) {
                    params.put("id", envelope.getId());
                }
                params.put("linha", linha);
                params.put("coluna", coluna);
                params.put("user", sessionCtrl.getUser());
                sameSrv.arquivar(selecteds, params);
                selecteds = null;
                pendentes = null;
                filtereds = null;
                addMsg(FacesMessage.SEVERITY_INFO, "Sucesso", "Itens arquivados.");
            } catch (Exception e) {
                addMsg(FacesMessage.SEVERITY_ERROR, "Ops!", "Erro na execução do arquivamento.");
            }
        }
    }

    public void addMsg(FacesMessage.Severity severity, String summary, String detail) {
        addMsg(new FacesMessage(severity, summary, detail), false);
    }

    public void addMsg(FacesMessage msg, boolean flash) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, msg);
        if (flash) {
            ctx.getExternalContext().getFlash().setKeepMessages(true);
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

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Armario getArmario() {
        return armario;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getColuna() {
        return coluna;
    }

    public void setColuna(String coluna) {
        this.coluna = coluna;
    }

    public Integer getTipoArquivamento() {
        return tipoArquivamento;
    }

    public void setTipoArquivamento(Integer tipoArquivamento) {
        this.tipoArquivamento = tipoArquivamento;
    }

    public List<String> getLinhas() {
        return linhas;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public List<Envelope> getEnvelopes() {
        return envelopes;
    }

    public void setEnvelopes(List<Envelope> envelopes) {
        this.envelopes = envelopes;
    }
}
