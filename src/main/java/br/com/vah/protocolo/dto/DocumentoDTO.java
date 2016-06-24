package br.com.vah.protocolo.dto;

import java.util.Date;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.RegistroDocumento;

public class DocumentoDTO {

  private TipoDocumentoEnum tipo;
  private PrescricaoMedica prescricao;
  private AvisoCirurgia aviso;
  private RegistroDocumento registro;
  private String descricao;
  private Date data;
  private String codigo;
  private String prestador;
  private Date dataHoraCriacao;
  private Date dataHoraImpressao;
  private Boolean hasEvolucao;
  private Integer qtdDoc = 1;
  private String conselho;
  private Boolean selected = false;

  public DocumentoDTO(PrescricaoMedica prescricao, Boolean hasEvolucao) {

    this.prescricao = prescricao;
    this.data = prescricao.getDatePreMed();
    this.codigo = String.valueOf(prescricao.getId());
    this.prestador = prescricao.getPrestador().getNome();
    this.dataHoraCriacao = prescricao.getDataHoraCriacao();
    this.dataHoraImpressao = prescricao.getDataHoraImpressao();
    this.hasEvolucao = hasEvolucao;

    // Atributos inferidos
    resolverConselho();
    resolverTipoDoc();
  }

  public DocumentoDTO(AvisoCirurgia aviso) {

    this.aviso = aviso;
    this.data = aviso.getInicioCirurgia();
    this.codigo = String.valueOf(aviso.getId());
    this.qtdDoc = 2;
    this.tipo =TipoDocumentoEnum.AVISO_CIRURGIA;

  }

  public DocumentoDTO(RegistroDocumento registro) {

    this.registro = registro;
    this.data = registro.getDataRegistro();
    this.codigo = String.valueOf(registro.getId());
    this.descricao = registro.getDocumento().getDescricao();
    this.qtdDoc = 1;
    this.tipo = TipoDocumentoEnum.REGISTRO_DOCUMENTO;
    this.prestador = registro.getNomeUsuario();

  }

  public DocumentoDTO() {
    // Documentos de prontuário
  }

  private void resolverConselho () {
    if (prescricao.getTipoPrescricao().equals("E")) {
      conselho = "COREN";
    } else {
      conselho = "CRM";
    }
  }

  private void resolverTipoDoc () {
    // LISTA DE ITENS PRESCRITOS NAO E VAZIO
    if(prescricao != null) {
      if (!prescricao.getItems().isEmpty() && hasEvolucao) {
        qtdDoc = 2;
      }
      if (qtdDoc > 1) {
        tipo = TipoDocumentoEnum.PRESCRICAO_EVOLUCAO;
      } else {
        if (qtdDoc < 2 && !hasEvolucao) {
          tipo = TipoDocumentoEnum.PRESCRICAO;
        } else {
          tipo = TipoDocumentoEnum.EVOLUCAO;
        }
      }
    } if (aviso != null) {
      tipo = TipoDocumentoEnum.AVISO_CIRURGIA;
    }
  }

  public Integer getQtdDoc() {
    return qtdDoc;
  }

  public void setQtdDoc(Integer qtdDoc) {
    this.qtdDoc = qtdDoc;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getPrestador() {
    return prestador;
  }

  public void setPrestador(String prestador) {
    this.prestador = prestador;
  }

  public RegistroDocumento getRegistro() {
    return registro;
  }

  public void setRegistro(RegistroDocumento registro) {
    this.registro = registro;
  }

  public Date getDataHoraCriacao() {
    return dataHoraCriacao;
  }

  public void setDataHoraCriacao(Date dataHoraCriacao) {
    this.dataHoraCriacao = dataHoraCriacao;
  }

  public Date getDataHoraImpressao() {
    return dataHoraImpressao;
  }

  public void setDataHoraImpressao(Date dataHoraImpressao) {
    this.dataHoraImpressao = dataHoraImpressao;
  }

  public TipoDocumentoEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoDocumentoEnum tipo) {
    this.tipo = tipo;
  }

  public PrescricaoMedica getPrescricao() {
    return prescricao;
  }

  public void setPrescricao(PrescricaoMedica prescricao) {
    this.prescricao = prescricao;
  }

  public AvisoCirurgia getAviso() {
    return aviso;
  }

  public void setAviso(AvisoCirurgia aviso) {
    this.aviso = aviso;
  }

  public Boolean getHasEvolucao() {
    return hasEvolucao;
  }

  public void setHasEvolucao(Boolean hasEvolucao) {
    this.hasEvolucao = hasEvolucao;
  }

  public String getConselho() {
    return conselho;
  }

  public void setConselho(String conselho) {
    this.conselho = conselho;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
}
