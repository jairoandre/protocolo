package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Especialidade;
import br.com.vah.protocolo.service.DataAccessService;
import br.com.vah.protocolo.service.EspecialidadeService;

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
public class EspecialidadeController extends AbstractController<Especialidade> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  EspecialidadeService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public DataAccessService<Especialidade> getService() {
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
