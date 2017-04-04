package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.Caixa;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.CaixaSrv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Named
public class CaixaConverter extends GenericConverter<Caixa> {

    private
    @Inject
    CaixaSrv service;


    @Override
    public AbstractSrv<Caixa> getService() {
        return service;
    }
}
