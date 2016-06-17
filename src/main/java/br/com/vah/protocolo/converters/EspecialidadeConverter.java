package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Especialidade;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.EspecialidadeService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EspecialidadeConverter extends GenericConverter<Especialidade> {

  private
  @Inject
  EspecialidadeService service;

  @Override
  public DataAccessService<Especialidade> getService() {
    return service;
  }
}