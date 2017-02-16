package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.ProFat;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.ProFatSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class ProFatController extends AbstractController<ProFat> {

  private @Inject
  transient Logger logger;

  private @Inject
  ProFatSrv service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public AbstractSrv<ProFat> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public ProFat createNewItem() {
    return new ProFat();
  }

  @Override
  public String path() {
    return "profat";
  }

  @Override
  public String getEntityName() {
    return "ProFat";
  }

  @Override
  public void prepareSearch() {
    setSearchParam("title", getSearchTerm());
  }

  public List<ProFat> completeProFat(String query) {
    setSearchTerm(query);
    prepareSearch();
    return getLazyModel().load(10);
  }
}
