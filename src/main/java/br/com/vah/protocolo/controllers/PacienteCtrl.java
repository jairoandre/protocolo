package br.com.vah.protocolo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.protocolo.dto.HistoricoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.entities.usrdbvah.Historico;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.PacienteSrv;

@Named
@ViewScoped
public class PacienteCtrl extends AbstractCtrl<Paciente> {

	private @Inject transient Logger logger;

	private @Inject PacienteSrv service;
	
	private Paciente paciente;
	
	private String localizacaoProntuario;
	
	private List<HistoricoDTO> protocolos;
	
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
		List<Object[]> local = new ArrayList<>();
		List<HistoricoDTO> hist = new ArrayList<>();
		this.localizacaoProntuario = null;
		this.protocolos = new ArrayList<>();
		this.paciente = service.initializeListsAtendimentos(new Long(1812646));
		
		for (Atendimento atendimento : this.paciente.getAtendimentos()){
			
			local = service.localizacaoProntuario(atendimento.getId());
			if (local.isEmpty() || local == null){
				break;
			}
			System.out.println(local.get(0)[10]);
			setLocalizacaoProntuario(local.get(0)[10].toString());
			
			//hist.addAll(service.historicoAtendimento(atendimento.getId()));
			this.protocolos = service.historicoAtendimento(atendimento.getId());
			
			//System.out.println(hist.get(0).getId()+" "+hist.get(0).getAcao());
			//System.out.println(hist.get(1).getId()+" "+hist.get(1).getAcao());
		}
		
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
		return protocolos;
	}

	public void setProtocolos(List<HistoricoDTO> protocolos) {
		this.protocolos = protocolos;
	}
}
