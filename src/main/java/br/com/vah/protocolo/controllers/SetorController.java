package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.SetorService;

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
public class SetorController extends AbstractController<Setor> {

  private @Inject
  transient Logger logger;

  private @Inject
  SetorService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public DataAccessService<Setor> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Setor createNewItem() {
    return new Setor();
  }

  @Override
  public String path() {
    return "setor";
  }

  @Override
  public String getEntityName() {
    return "Setor";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
  }

}
