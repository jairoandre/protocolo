package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 16/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_DOCUMENTO", schema = "USRDBVAH")
public class DocumentoProtocolo extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqProtocolo", sequenceName = "SEQ_NPTC_PROTOCOLO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProtocolo")
  @Column(name = "ID")
  private Long id;

  @Column(name = "CD_ITEM_PRONTUARIO")
  private Long codigo;

  @Column(name = "CD_TIPO")
  @Enumerated(EnumType.ORDINAL)
  private TipoDocumentoEnum tipo;

  @Column(name = "NM_DESCRICAO")
  private String descricao;

  @Column(name = "NM_CONSELHO")
  private String conselho;

  @Column(name = "NM_PRESTADOR")
  private String prestador;

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
