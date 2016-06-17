package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.service.ConvenioService;
import br.com.vah.protocolo.service.DataAccessService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ConvenioConverter extends GenericConverter<Convenio> {

  private
  @Inject
  ConvenioService service;

  @Override
  public DataAccessService<Convenio> getService() {
    return service;
  }
}