package br.com.vah.protocolo.dto;

import java.util.Date;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.dbamv.RegistroDocumento;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

public class DocumentoDTO {

  private Protocolo protocolo;
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
  private String conselho;
  private ItemProtocolo itemProtocolo;
  private Boolean selected = false;
  private Protocolo protocoloItem;
  private RegFaturamento conta;

  public DocumentoDTO(PrescricaoMedica prescricao, TipoDocumentoEnum tipo) {
    setFieldsForPrescricao(prescricao, tipo);
  }

  private void setFieldsForPrescricao(PrescricaoMedica prescricao, TipoDocumentoEnum tipo) {
    this.prescricao = prescricao;
    this.data = prescricao.getDatePreMed();
    this.codigo = String.valueOf(prescricao.getId());
    this.prestador = prescricao.getPrestador().getNome();
    this.dataHoraCriacao = prescricao.getDataHoraCriacao();
    this.dataHoraImpressao = prescricao.getDataHoraImpressao();
    this.tipo = tipo;
    // Atributos inferidos
    resolverConselho();
  }

  public DocumentoDTO(AvisoCirurgia aviso, TipoDocumentoEnum tipo) {
    setFieldsForAvisoCirurgia(aviso, tipo);
  }

  private void setFieldsForAvisoCirurgia(AvisoCirurgia aviso, TipoDocumentoEnum tipo) {
    this.aviso = aviso;
    this.data = aviso.getInicioCirurgia();
    this.codigo = String.valueOf(aviso.getId());
    this.tipo = tipo;
  }

  public DocumentoDTO(RegistroDocumento registro) {
    setFieldsForRegistroDocumento(registro);
  }

  private void setFieldsForRegistroDocumento(RegistroDocumento registro) {
    this.registro = registro;
    this.data = registro.getDataRegistro();
    this.codigo = String.valueOf(registro.getId());
    this.descricao = registro.getDocumento().getDescricao();
    this.tipo = TipoDocumentoEnum.REGISTRO_DOCUMENTO;
    this.prestador = registro.getNomeUsuario();
  }

  public DocumentoDTO(Protocolo protocolo) {
    setFieldsProtocoloFilho(protocolo);
  }

  private void setFieldsProtocoloFilho(Protocolo protocoloItem) {
    this.protocoloItem = protocoloItem;
    this.data = protocoloItem.getDataEnvio();
    this.dataHoraCriacao = protocoloItem.getDataEnvio();
    this.codigo = String.valueOf(protocoloItem.getId());
    this.tipo = TipoDocumentoEnum.PROTOCOLO;
  }

  public DocumentoDTO(RegFaturamento conta) {
    setFieldsConta(conta);
  }

  private void setFieldsConta(RegFaturamento conta) {
    this.conta = conta;
    this.data = conta.getInicio();
    this.codigo = String.valueOf(conta.getId());
    this.tipo = TipoDocumentoEnum.CONTA;
  }

  public DocumentoDTO(ItemProtocolo itemProtocolo) {
    this.itemProtocolo = itemProtocolo;
    switch (itemProtocolo.getTipo()) {
      case PRESCRICAO:
      case EVOLUCAO:
        setFieldsForPrescricao(itemProtocolo.getPrescricaoMedica(), itemProtocolo.getTipo());
        break;
      case DESCRICAO_CIRURGICA:
      case FOLHA_ANESTESICA:
        setFieldsForAvisoCirurgia(itemProtocolo.getAvisoCirurgia(), itemProtocolo.getTipo());
        break;
      case REGISTRO_DOCUMENTO:
        setFieldsForRegistroDocumento(itemProtocolo.getRegistroDocumento());
        break;
      case PROTOCOLO:
        setFieldsProtocoloFilho(itemProtocolo.getProtocoloItem());
        break;
      case CONTA:
        setFieldsConta(itemProtocolo.getConta());
        break;
      default:
        break;
    }
  }

  public ItemProtocolo getItemProtocolo() {
    return this.itemProtocolo;
  }

  public ItemProtocolo criarItemProtocolo() {
    ItemProtocolo itemProtocolo = new ItemProtocolo();
    itemProtocolo.setProtocolo(protocolo);
    itemProtocolo.setTipo(tipo);
    if (prescricao != null) {
      itemProtocolo.setPrescricaoMedica(prescricao);
    } else if (aviso != null) {
      itemProtocolo.setAvisoCirurgia(aviso);
    } else if (registro != null) {
      itemProtocolo.setRegistroDocumento(registro);
    } else if (protocoloItem != null) {
      itemProtocolo.setProtocoloItem(protocoloItem);
    } else if (conta != null) {
      itemProtocolo.setConta(conta);
    }
    return itemProtocolo;
  }

  private void resolverConselho () {
    if (prescricao.getTipoPrescricao().equals("E")) {
      conselho = "COREN";
    } else {
      conselho = "CRM";
    }
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
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

  public Protocolo getProtocoloItem() {
    return protocoloItem;
  }

  public void setProtocoloItem(Protocolo protocoloItem) {
    this.protocoloItem = protocoloItem;
  }

  public RegFaturamento getConta() {
    return conta;
  }

  public void setConta(RegFaturamento conta) {
    this.conta = conta;
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
