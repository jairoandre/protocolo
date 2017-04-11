package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.dto.HistoricoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.entities.usrdbvah.Historico;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.PacienteSrv;
import br.com.vah.protocolo.service.ProtocoloSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named
@ViewScoped
public class PacienteCtrl extends AbstractCtrl<Paciente> {

    private
    @Inject
    transient Logger logger;

    private
    @Inject
    PacienteSrv service;

    private
    @Inject
    ProtocoloSrv protocoloService;

    private Paciente paciente;

    private String localizacaoProntuario;

    private List<HistoricoDTO> historicoDTO;

    private List<Historico> protocolo;

    @PostConstruct
    public void init() {
        logger.info(this.getClass().getSimpleName() + " created");
        setItem(createNewItem());
        initLazyModel(service);
    }

    @Override
    public AbstractSrv<Paciente> getService() {
        return service;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Paciente createNewItem() {
        return new Paciente();
    }

    @Override
    public String path() {
        return "paciente";
    }

    @Override
    public String getEntityName() {
        return "Paciente";
    }

    @Override
    public void prepareSearch() {
        super.prepareSearch();
        setSearchParam("name", getSearchTerm());
    }

    public void onPacienteSelect() {
        Protocolo local = null;
        List<Protocolo> tempNptc = new ArrayList<>();
        this.protocolo = new ArrayList<>();
        if (getItem() != null && getItem().getId() != null) {

            this.localizacaoProntuario = null;
            this.historicoDTO = new ArrayList<>();
            this.paciente = service.initializeListsAtendimentos(getItem().getId());

            for (Atendimento atendimento : this.paciente.getAtendimentos()) {
                Protocolo temp = atendimento.getProtocolos().get(0);
                for (Protocolo nptc : atendimento.getProtocolos()) {
                    if (nptc.getId() >= temp.getId()) {
                        if (nptc.getLocalizacao() == null) {
                            nptc.setLocalizacao(nptc.getOrigem().getLabelForSelectItem());
                        }
                        temp = nptc;
                    }
                }
                tempNptc.add(temp);
                atendimento.setProtocolos(tempNptc);
            }

        }
    }

    public List<Historico> recuperarHistorico(Protocolo protocolo) {
        return protocoloService.initializeHistorico(protocolo);
    }


    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getLocalizacaoProntuario() {
        return localizacaoProntuario;
    }

    public void setLocalizacaoProntuario(String localizacaoProntuario) {
        this.localizacaoProntuario = localizacaoProntuario;
    }

    public List<HistoricoDTO> getProtocolos() {
        return historicoDTO;
    }

    public void setProtocolos(List<HistoricoDTO> protocolos) {
        this.historicoDTO = protocolos;
    }

    public List<Historico> getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(List<Historico> protocolo) {
        this.protocolo = protocolo;
    }
}
