package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.DocumentoProtocoloSrv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 04/05/2017.
 */
@Named
public class DocumentoProtocoloConverter extends GenericConverter<DocumentoProtocolo> {

  private
  @Inject
  DocumentoProtocoloSrv service;

  @Override
  public AbstractSrv<DocumentoProtocolo> getService() {
    return service;
  }
}
