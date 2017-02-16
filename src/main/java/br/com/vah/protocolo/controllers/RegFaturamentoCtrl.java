package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.RegFaturamentoSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 13/02/2017.
 */

@Named
@ViewScoped
public class RegFaturamentoCtrl extends AbstractController<RegFaturamento> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  RegFaturamentoSrv service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }

  @Override
  public AbstractSrv<RegFaturamento> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public RegFaturamento createNewItem() {
    return new RegFaturamento();
  }

  @Override
  public String path() {
    return "regfaturamento";
  }

  @Override
  public String getEntityName() {
    return "Registro Faturamento";
  }

  private @Inject ProtocoloCtrl protocoloCtrl;

  @Override
  public List<RegFaturamento> completeMethod(String query) {
    return service.obterContas(protocoloCtrl.getItem(), null);
  }
}
