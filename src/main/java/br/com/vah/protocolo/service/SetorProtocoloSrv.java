package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 26/01/2017.
 */
@Stateless
public class SetorProtocoloSrv extends DataAccessService<SetorProtocolo> {


  public SetorProtocoloSrv() {
    super(SetorProtocolo.class);
  }

}
