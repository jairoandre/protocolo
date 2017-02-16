package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.RegFaturamentoSrv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 13/02/2017.
 */
@Named
public class RegFaturamentoConverter extends GenericConverter<RegFaturamento> {

  private
  @Inject
  RegFaturamentoSrv service;

  @Override
  public AbstractSrv<RegFaturamento> getService() {
    return service;
  }
}
