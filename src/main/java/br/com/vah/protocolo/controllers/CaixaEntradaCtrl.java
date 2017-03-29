package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.constants.CaixaEntradaFieldEnum;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.usrdbvah.CaixaEntrada;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.service.AbstractSrv;
import br.com.vah.protocolo.service.CaixaEntradaSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 24/03/2017.
 */
@Named
@ViewScoped
public class CaixaEntradaCtrl extends AbstractCtrl<CaixaEntrada> {

  private Map<CaixaEntradaFieldEnum, Object> mapFiltros = new HashMap<>();

  private
  @Inject
  CaixaEntradaSrv service;

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  SessionController session;

  private String contas;

  private List<Convenio> convenios = new ArrayList<>();

  private Convenio convenio;

  private SetorProtocolo setor;

  private Boolean vinculados;

  public String getContas() {
    return contas;
  }

  public void setContas(String contas) {
    this.contas = contas;
  }

  public List<Convenio> getConvenios() {
    return convenios;
  }

  public void setConvenios(List<Convenio> convenios) {
    this.convenios = convenios;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }

  public SetorProtocolo getSetor() {
    return setor;
  }

  public void setSetor(SetorProtocolo setor) {
    this.setor = setor;
  }

  public Boolean getVinculados() {
    return vinculados;
  }

  public void setVinculados(Boolean vinculados) {
    this.vinculados = vinculados;
  }

  public void adicionarConvenio() {
    convenios.add(convenio);
    convenio = null;
  }

  public void removerConvenio(Convenio convenio) {
    convenios.remove(convenio);
  }

  @Override
  public AbstractSrv<CaixaEntrada> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public CaixaEntrada createNewItem() {
    return new CaixaEntrada();
  }

  @Override
  public String path() {
    return "caixaEntrada";
  }

  @Override
  public String getEntityName() {
    return "Caixa Entrada";
  }

  public List<CaixaEntradaFieldEnum> getFieldsMap() {
    return new ArrayList<>(mapFiltros.keySet());
  }

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    if (session.getSetor() != null) {
      setor = session.getSetor();
    }
    setItem(createNewItem());
    initLazyModel(service);
    prepareSearch();
  }

  private List<Long> interpolarContas() {
    List<Long> listaContas = new ArrayList<>();
    if (contas != null && !contas.isEmpty()) {
      String[] contasSeparadas = contas.split(";");
      for (String conta : contasSeparadas) {
        try {
          Long contaValue = Long.valueOf(conta);
          listaContas.add(contaValue);
        } catch (Exception e) {

        }
      }
    }
    return listaContas;
  }

  @Override
  public void prepareSearch() {
    resetSearchParams();
    List<Long> contas = interpolarContas();
    if (!contas.isEmpty()) {
      mapFiltros.put(CaixaEntradaFieldEnum.CONTAS, contas);
      setSearchParam(CaixaEntradaFieldEnum.CONTAS.name(), contas);
    }
    if (!convenios.isEmpty()) {
      mapFiltros.put(CaixaEntradaFieldEnum.CONVENIOS, convenios);
      setSearchParam(CaixaEntradaFieldEnum.CONVENIOS.name(), convenios);
    }
    if (setor != null) {
      mapFiltros.put(CaixaEntradaFieldEnum.SETOR, setor);
      setSearchParam(CaixaEntradaFieldEnum.SETOR.name(), setor);
    }
    if (vinculados != null) {
      mapFiltros.put(CaixaEntradaFieldEnum.VINCULADOS, vinculados);
      setSearchParam(CaixaEntradaFieldEnum.VINCULADOS.name(), vinculados);
    }
  }

  public String interpolarTextoMapFiltros(CaixaEntradaFieldEnum filtro) {
    return filtro.getText(mapFiltros.get(filtro));
  }

  public void removeFilterItem(CaixaEntradaFieldEnum filtro) {
    mapFiltros.remove(filtro);
    removeFromSearchParams(filtro.name());
    switch (filtro) {
      case CONTAS:
        contas = null;
        break;
      case CONVENIOS:
        convenios.clear();
        break;
      case SETOR:
        setor = null;
        break;
      case VINCULADOS:
        vinculados = null;
        break;
    }
    prepareSearch();
  }
}
