package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.SetorProtocoloSrv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 26/01/2017.
 */
@Named
public class SetorProtocoloConverter  extends GenericConverter<SetorProtocolo> {
  private @Inject
  SetorProtocoloSrv service;

  @Override
  public DataAccessService<SetorProtocolo> getService() {
    return service;
  }
}
