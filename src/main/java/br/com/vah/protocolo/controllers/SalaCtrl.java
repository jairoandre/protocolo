package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.usrdbvah.Armario;
import br.com.vah.protocolo.entities.usrdbvah.Sala;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.SalaSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 03/04/2017.
 */
@Named
@ViewScoped
public class SalaCtrl extends AbstractCtrl<Sala> {

  private @Inject
  transient Logger logger;

  private @Inject
  SalaSrv service;


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

  public void novoArmario() {
    Armario armario = new Armario();
    armario.setEditing(true);
    armario.setSala(getItem());
    armario.setCriacao(new Date());
    getItem().getArmarios().add(armario);
  }

  public void confirmarArmario(Armario armario) {
    armario.setEditing(false);
  }

  public void removerArmario(Armario armario) {
    getItem().getArmarios().remove(armario);
  }

  public void editarArmario(Armario armario) {
    armario.setEditing(true);
  }


  @Override
  public AbstractSrv<Sala> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Sala createNewItem() {
    return new Sala();
  }

  @Override
  public String path() {
    return "sala";
  }

  @Override
  public String getEntityName() {
    return "Sala";
  }
}
