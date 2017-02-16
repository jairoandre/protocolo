package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.SetorSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SetorConverter extends GenericConverter<Setor> {

  private
  @Inject
  SetorSrv service;

  @Override
  public AbstractSrv<Setor> getService() {
    return service;
  }
}