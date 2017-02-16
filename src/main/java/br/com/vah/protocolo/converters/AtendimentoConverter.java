package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.service.AtendimentoSrv;
import br.com.vah.protocolo.service.AbstractSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AtendimentoConverter extends GenericConverter<Atendimento> {

  private @Inject
  AtendimentoSrv service;


  @Override
  public AbstractSrv<Atendimento> getService() {
    return service;
  }
}