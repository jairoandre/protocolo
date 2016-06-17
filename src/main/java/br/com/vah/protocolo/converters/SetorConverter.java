package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.SetorService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SetorConverter extends GenericConverter<Setor> {

  private
  @Inject
  SetorService service;

  @Override
  public DataAccessService<Setor> getService() {
    return service;
  }
}