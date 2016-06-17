package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.service.ProtocoloService;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class RelatorioMediasController implements Serializable {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ProtocoloService service;

  private Date inicioDate;

  private Date terminoDate;

  private List<Setor> setores = new ArrayList<>();

  private Setor setorToAdd;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
  }

  public Date getInicioDate() {
    return inicioDate;
  }

  public void setInicioDate(Date inicioDate) {
    this.inicioDate = inicioDate;
  }

  public Date getTerminoDate() {
    return terminoDate;
  }

  public void setTerminoDate(Date terminoDate) {
    this.terminoDate = terminoDate;
  }

  public List<Setor> getSetores() {
    return setores;
  }

  public void setSetores(List<Setor> setores) {
    this.setores = setores;
  }

  public Setor getSetorToAdd() {
    return setorToAdd;
  }

  public void setSetorToAdd(Setor setorToAdd) {
    this.setorToAdd = setorToAdd;
  }

  public void addSetor() {
    if (setorToAdd != null) {
      if (!setores.contains(setorToAdd)) {
        setores.add(setorToAdd);
      }
      setorToAdd = null;
    }
  }

  public void removeSetor(Setor setorToRemove) {
    setores.remove(setorToRemove);
  }

  public void addMsg(FacesMessage msg, boolean flash) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.addMessage(null, msg);
    if (flash) {
      ctx.getExternalContext().getFlash().setKeepMessages(true);
    }
  }

  public StreamedContent getRelatorio() {
    try {
      return service.relatorioTotalPorSetor(inicioDate, terminoDate, setores, null);
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "relatório"), false);
    }
    return null;
  }

}
