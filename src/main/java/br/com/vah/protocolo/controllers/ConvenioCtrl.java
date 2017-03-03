package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.ConvenioSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 20/02/2017.
 */
@Named
@ViewScoped
public class ConvenioCtrl extends AbstractCtrl<Convenio> {

  private
  @Inject
  transient Logger logger;

  private @Inject
  ConvenioSrv service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public AbstractSrv<Convenio> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Convenio createNewItem() {
    return new Convenio();
  }

  @Override
  public String path() {
    return "convenio";
  }

  @Override
  public String getEntityName() {
    return "Convênio";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
  }
}
