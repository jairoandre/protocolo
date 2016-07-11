package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;

import javax.ejb.Stateless;

/**
 * Created by jairoportela on 11/07/2016.
 */
@Stateless
public class ItemProtocoloService extends DataAccessService<ItemProtocolo> {

  public ItemProtocoloService() {
    super(ItemProtocolo.class);
  }

}
