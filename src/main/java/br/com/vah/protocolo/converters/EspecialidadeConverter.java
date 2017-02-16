package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Especialidade;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.EspecialidadeSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EspecialidadeConverter extends GenericConverter<Especialidade> {

  private
  @Inject
  EspecialidadeSrv service;

  @Override
  public AbstractSrv<Especialidade> getService() {
    return service;
  }
}