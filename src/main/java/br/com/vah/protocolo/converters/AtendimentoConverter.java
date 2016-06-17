package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.service.AtendimentoService;
import br.com.vah.protocolo.service.DataAccessService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AtendimentoConverter extends GenericConverter<Atendimento> {

  private @Inject
  AtendimentoService service;


  @Override
  public DataAccessService<Atendimento> getService() {
    return service;
  }
}