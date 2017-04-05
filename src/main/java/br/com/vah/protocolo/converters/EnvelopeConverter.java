package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.usrdbvah.Envelope;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.EnvelopeSrv;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EnvelopeConverter extends GenericConverter<Envelope> {

    private
    @Inject
    EnvelopeSrv service;

    @Override
    public AbstractSrv<Envelope> getService() {
        return service;
    }
}