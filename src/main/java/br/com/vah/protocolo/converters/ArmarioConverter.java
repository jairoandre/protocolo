package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.Armario;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.ArmarioSrv;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Named
public class ArmarioConverter extends GenericConverter<Armario> {

    private
    @Inject
    ArmarioSrv service;


    @Override
    public AbstractSrv<Armario> getService() {
        return service;
    }
}
