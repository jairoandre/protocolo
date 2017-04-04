package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.usrdbvah.Armario;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.ArmarioSrv;

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
public class ArmarioCtrl extends AbstractCtrl<Armario> {

    private
    @Inject
    transient Logger logger;

    private
    @Inject
    ArmarioSrv service;

    @PostConstruct
    public void init() {
        logger.info(this.getClass().getSimpleName() + " created");
        setItem(createNewItem());
        initLazyModel(service);
    }


    @Override
    public AbstractSrv<Armario> getService() {
        return service;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Armario createNewItem() {
        return new Armario();
    }

    @Override
    public String path() {
        return "armario";
    }

    @Override
    public String getEntityName() {
        return "Armário";
    }

    @Override
    public void prepareSearch() {
        super.prepareSearch();
        setSearchParam("titulo", getSearchTerm());
    }
}
