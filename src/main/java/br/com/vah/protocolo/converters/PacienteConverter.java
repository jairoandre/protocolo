package br.com.vah.protocolo.converters;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.PacienteSrv;

@Named
public class PacienteConverter extends GenericConverter<Paciente> {

	private @Inject PacienteSrv service;

	@Override
	public AbstractSrv<Paciente> getService() {
		return service;
	}

}
