package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.Envelope;

import javax.ejb.Stateless;
import java.util.HashSet;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Stateless
public class EnvelopeSrv extends AbstractSrv<Envelope> {

    public EnvelopeSrv() {
        super(Envelope.class);
    }

    public Envelope loadLists(Long id) {
        Envelope envelope = find(id);
        new HashSet<>(envelope.getItens());
        return envelope;
    }
}
