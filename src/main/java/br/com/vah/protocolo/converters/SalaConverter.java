package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.Sala;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.SalaSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SalaConverter extends GenericConverter<Sala> {

    private
    @Inject
    SalaSrv service;

    @Override
    public AbstractSrv<Sala> getService() {
        return service;
    }
}