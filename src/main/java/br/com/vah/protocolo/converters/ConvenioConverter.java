package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.service.ConvenioSrv;
import br.com.vah.protocolo.service.AbstractSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ConvenioConverter extends GenericConverter<Convenio> {

  private
  @Inject
  ConvenioSrv service;

  @Override
  public AbstractSrv<Convenio> getService() {
    return service;
  }
}