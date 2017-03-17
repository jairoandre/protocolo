package br.com.vah.protocolo.dto;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentoDTO {

  private Protocolo protocolo;
  private TipoDocumentoEnum tipo;
  private String descricao;
  private String codigo;
  private String conselho;
  private String prestador;
  private String consPrestConv;
  private Date dataHoraCriacao;
  private Date dataHoraImpressao;
  private DocumentoProtocolo documento;
  private Protocolo filho;
  private ItemProtocolo itemProtocolo;

  private Boolean selected = false;

  public void setFields(Object[] row) {
    this.codigo = row[0] == null ? "" : row[0].toString();
    this.tipo = row[1] == null ? null : TipoDocumentoEnum.getByTitle(row[1].toString());
    this.descricao = row[2] == null ? "" : row[2].toString();
    this.conselho = row[3] == null ? "" : row[3].toString();
    this.prestador = row[4] == null ? "" : row[4].toString();
    this.dataHoraCriacao = (Date) row[5];
    this.dataHoraImpressao = (Date) row[6];
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    this.documento = new DocumentoProtocolo();
    this.documento.setCodigo(((BigDecimal) row[0]).longValue());
    this.documento.setTipo(tipo);
    this.documento.setDescricao(descricao);
    this.documento.setConselho(conselho);
    this.documento.setPrestador(prestador);
    this.documento.setDataHoraCriacao(dataHoraCriacao);
    this.documento.setDataHoraImpressao(dataHoraImpressao);
  }

  public DocumentoDTO(Object[] row) {
    setFields(row);
  }

  public void setFields(DocumentoProtocolo documento) {
    this.documento = documento;
    this.codigo = String.valueOf(documento.getCodigo());
    this.tipo = documento.getTipo();
    this.descricao = documento.getDescricao();
    if (this.descricao == null) {
      this.descricao = documento.getTipo().getLabel();
    }
    this.conselho = documento.getConselho();
    this.prestador = documento.getPrestador();
    this.dataHoraCriacao = documento.getDataHoraCriacao();
    this.dataHoraImpressao = documento.getDataHoraImpressao();
    if (this.conselho != null && this.prestador != null) {
      this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    } else {
      this.consPrestConv = "N/A";
    }

  }

  public DocumentoDTO(DocumentoProtocolo documento) {
    setFields(documento);
  }

  public void setFields(Protocolo filho) {
    this.filho = filho;
    this.codigo = String.valueOf(filho.getId());
    this.tipo = TipoDocumentoEnum.PROTOCOLO;
    RegFaturamento conta = filho.getContaFaturamento();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
    this.descricao = conta == null ? "Remessa" : String.format("%d - (%s à %s)", conta.getId(), sdf.format(conta.getInicio()), sdf.format(conta.getFim()));
    this.conselho = "-";
    this.prestador = "-";
    this.dataHoraCriacao = filho.getDataEnvio();
    this.dataHoraImpressao = filho.getDataEnvio();
    Atendimento atendimento = filho.getAtendimento();
    if (atendimento != null) {
      this.consPrestConv = String.format("%s - %d - %s", atendimento.getConvenio().getTitle(), atendimento.getId(), atendimento.getPaciente().getName());
    } else {
      this.consPrestConv = "Remessa";
    }
  }

  public DocumentoDTO(Protocolo protocolo) {
    setFields(protocolo);
  }

  public DocumentoDTO(ItemProtocolo item) {
    this.itemProtocolo = item;
    if (item.getDocumento() != null) {
      setFields(item.getDocumento());
    } else if (item.getFilho() != null) {
      setFields(item.getFilho());
    }
  }

  public ItemProtocolo getItemProtocolo() {
    return this.itemProtocolo;
  }

  public ItemProtocolo criarItemProtocolo() {
    ItemProtocolo itemProtocolo = new ItemProtocolo();
    itemProtocolo.setProtocolo(protocolo);
    itemProtocolo.setDocumento(documento);
    itemProtocolo.setFilho(filho);
    return itemProtocolo;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
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

  public String getConselho() {
    return conselho;
  }

  public void setConselho(String conselho) {
    this.conselho = conselho;
  }

  public String getConsPrestConv() {
    return consPrestConv;
  }

  public void setConsPrestConv(String consPrestConv) {
    this.consPrestConv = consPrestConv;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public DocumentoProtocolo getDocumento() {
    return documento;
  }

  public void setDocumento(DocumentoProtocolo documento) {
    this.documento = documento;
  }

  public Protocolo getFilho() {
    return filho;
  }

  public void setFilho(Protocolo filho) {
    this.filho = filho;
  }

  public void setItemProtocolo(ItemProtocolo itemProtocolo) {
    this.itemProtocolo = itemProtocolo;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
}
