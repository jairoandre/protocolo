package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.Caixa;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Stateless
public class CaixaSrv extends AbstractSrv<Caixa> {

    public CaixaSrv() {
        super(Caixa.class);
    }
}
