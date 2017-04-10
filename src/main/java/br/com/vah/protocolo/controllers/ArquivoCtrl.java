package br.com.vah.protocolo.controllers;

import br.com.vah.protocolo.dto.SameDTO;
import br.com.vah.protocolo.entities.dbamv.Convenio;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Jairoportela on 07/04/2017.
 */
@Named
@ViewScoped
public class ArquivoCtrl implements Serializable {


    private List<SameDTO> selecteds;

    private List<SameDTO> filtereds;

    private List<SameDTO> arquivos;

    private Long atendimento;

    private String listaContas;

    private Convenio convenio;

    public Integer getSelectCount() {
        if (arquivos == null) {
            return 0;
        } else {
            return arquivos.stream().map(SameDTO::getSelectedInteger).reduce(0, (a, b) -> a + b);
        }
    }

    public List<SameDTO> getSelecteds() {
        return selecteds;
    }

    public void setSelecteds(List<SameDTO> selecteds) {
        this.selecteds = selecteds;
    }

    public List<SameDTO> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<SameDTO> filtereds) {
        this.filtereds = filtereds;
    }

    public List<SameDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<SameDTO> arquivos) {
        this.arquivos = arquivos;
    }

    public String getListaContas() {
        return listaContas;
    }

    public void setListaContas(String listaContas) {
        this.listaContas = listaContas;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public Long getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Long atendimento) {
        this.atendimento = atendimento;
    }
}
