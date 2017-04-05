package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.usrdbvah.Caixa;
import br.com.vah.protocolo.entities.usrdbvah.Envelope;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.CaixaSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 04/04/2017.
 */
@Named
@ViewScoped
public class CaixaCtrl extends AbstractCtrl<Caixa> {

    private
    @Inject
    transient Logger logger;

    private
    @Inject
    CaixaSrv service;

    @PostConstruct
    public void init() {
        logger.info(this.getClass().getSimpleName() + " created");
        setItem(createNewItem());
        initLazyModel(service);
    }

    @Override
    public void onLoad() {
        getLogger().info("Load params");
        if (getId() == null) {
            setItem(createNewItem());
        } else {
            setItem(service.loadLists(getId()));
        }
    }

    public void novoEnvelope() {
        Envelope envelope = new Envelope();
        envelope.setEditing(true);
        envelope.setCaixa(getItem());
        getItem().getEnvelopes().add(envelope);
    }

    public void confirmarEnvelope(Envelope envelope) {
        envelope.setEditing(false);
    }

    public void removerEnvelope(Envelope envelope) {
        getItem().getEnvelopes().remove(envelope);
    }

    public void editarEnvelope(Envelope envelope) {
        envelope.setEditing(true);
    }


    @Override
    public AbstractSrv<Caixa> getService() {
        return service;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Caixa createNewItem() {
        return new Caixa();
    }

    @Override
    public String path() {
        return "caixa";
    }

    @Override
    public String getEntityName() {
        return "Caixa";
    }

    @Override
    public void prepareSearch() {
        super.prepareSearch();
        setSearchParam("titulo", getSearchTerm());
    }
}
