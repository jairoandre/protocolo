package br.com.vah.protocolo.controllers;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.PacienteSrv;

@Named
@ViewScoped
public class PacienteCtrl extends AbstractCtrl<Paciente> {

	private @Inject transient Logger logger;

	private @Inject PacienteSrv service;
	
	private Paciente paciente;
	
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
		String local = null;
		this.paciente = service.initializeListsAtendimentos(new Long(1));
		
		for (Atendimento atendimento : this.paciente.getAtendimentos()){
			
			local = service.localizacaoProntuario(atendimento.getId());
			System.out.println(local);
		}
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
}
