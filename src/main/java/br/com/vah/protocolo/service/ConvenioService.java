package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Convenio;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class ConvenioService extends DataAccessService<Convenio> {

  public ConvenioService() {
    super(Convenio.class);
  }

}
