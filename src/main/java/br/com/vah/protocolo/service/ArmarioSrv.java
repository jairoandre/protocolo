package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.Armario;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Stateless
public class ArmarioSrv extends AbstractSrv<Armario> {

    public ArmarioSrv() {
        super(Armario.class);
    }
}
