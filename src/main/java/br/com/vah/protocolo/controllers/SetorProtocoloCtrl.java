package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.SetorProtocoloSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class SetorProtocoloCtrl extends AbstractController<SetorProtocolo> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  SetorProtocoloSrv service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public AbstractSrv<SetorProtocolo> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public SetorProtocolo createNewItem() {
    return new SetorProtocolo();
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
