package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 16/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_DOCUMENTO_PROTOCOLO", schema = "USRDBVAH")
public class DocumentoProtocolo extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqDocProtocolo", sequenceName = "SEQ_NPTC_DOCUMENTO_PROTOCOLO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDocProtocolo")
  @Column(name = "ID")
  private Long id;

  @Column(name = "CD_ITEM_PRONTUARIO", nullable = false)
  private Long codigo = 0l;

  @Column(name = "CD_ATENDIMENTO")
  private Long atendimento;

  @Column(name = "CD_TIPO")
  @Enumerated(EnumType.ORDINAL)
  private TipoDocumentoEnum tipo;

  @Column(name = "NM_DESCRICAO")
  private String descricao;

  @Column(name = "NM_CONSELHO")
  private String conselho;

  @Column(name = "NM_PRESTADOR")
  private String prestador;

  @Column(name = "DT_REFERENCIA")
  private Date dataReferencia;

  @Column(name = "DT_HORA_CRIACAO")
  private Date dataHoraCriacao;

  @Column(name  = "DT_HORA_IMPRESSAO")
  private Date dataHoraImpressao;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Long getCodigo() {
    return codigo;
  }

  public void setCodigo(Long codigo) {
    this.codigo = codigo;
  }

  public Long getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Long atendimento) {
    this.atendimento = atendimento;
  }

  public TipoDocumentoEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoDocumentoEnum tipo) {
    this.tipo = tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getConselho() {
    return conselho;
  }

  public void setConselho(String conselho) {
    this.conselho = conselho;
  }

  public String getPrestador() {
    return prestador;
  }

  public Date getDataReferencia() {
    return dataReferencia;
  }

  public void setDataReferencia(Date dataReferencia) {
    this.dataReferencia = dataReferencia;
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

  @Override
  public String getLabelForSelectItem() {
    return descricao;
  }
}
