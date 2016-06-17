package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.service.AtendimentoService;
import br.com.vah.protocolo.service.DataAccessService;

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
public class AtendimentoController extends AbstractController<Atendimento> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  AtendimentoService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public DataAccessService<Atendimento> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Atendimento createNewItem() {
    return new Atendimento();
  }

  @Override
  public String path() {
    return "atendimento";
  }

  @Override
  public String getEntityName() {
    return "Atendimento";
  }

  @Override
  public void prepareSearch() {
    super.prepareSearch();
    setSearchParam("paciente", getSearchTerm());
  }

  public List<Atendimento> completeAtendimento(String query) {
    setSearchTerm(query);
    super.prepareSearch();
    setSearchParam("paciente", query);
    setSearchParam("semAltaRecente", true);
    return getLazyModel().load(10);
  }
}
