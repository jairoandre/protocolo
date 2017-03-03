package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Especialidade;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.EspecialidadeSrv;

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
public class EspecialidadeCtrl extends AbstractCtrl<Especialidade> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  EspecialidadeSrv service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public AbstractSrv<Especialidade> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Especialidade createNewItem() {
    return new Especialidade();
  }

  @Override
  public String path() {
    return "especialidade";
  }

  @Override
  public String getEntityName() {
    return "Especialidade";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("title", getSearchTerm());
  }

  public List<Especialidade> completeEspecialidade(String query) {
    setSearchTerm(query);
    super.prepareSearch();
    setSearchParam("title", query);
    return getLazyModel().load(10);
  }
}
