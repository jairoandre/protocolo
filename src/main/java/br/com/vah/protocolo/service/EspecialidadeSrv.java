package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Especialidade;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class EspecialidadeSrv extends AbstractSrv<Especialidade> {

  public EspecialidadeSrv() {
    super(Especialidade.class);
  }

}
