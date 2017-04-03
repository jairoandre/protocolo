package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.Sala;

import javax.ejb.Stateless;
import java.util.HashSet;

/**
 * Created by Jairoportela on 03/04/2017.
 */
@Stateless
public class SalaSrv extends AbstractSrv<Sala> {

  public SalaSrv() {
    super(Sala.class);
  }

  public Sala loadLists(Long id) {
    Sala sala = find(id);
    new HashSet<>(sala.getArmarios());
    return sala;
  }
}
