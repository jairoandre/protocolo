package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.ConselhoEnum;
import br.com.vah.protocolo.constants.TipoDocumentoManualEnum;
import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.dbamv.Prestador;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 14/06/2016.
 */
@Entity
@Table(name = "TB_NPTC_DOC_MANUAL", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = DocumentoManual.ALL, query = "SELECT d FROM DocumentoManual d"),
    @NamedQuery(name = DocumentoManual.COUNT, query = "SELECT COUNT(d) FROM DocumentoManual d")})
public class DocumentoManual extends BaseEntity {

  public final static String ALL = "DocumentoManual.all";
  public final static String COUNT = "DocumentoManual.count";

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "CD_CODIGO")
  private Long codigo;

  @Column(name = "CD_TIPO", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private TipoDocumentoManualEnum tipo;

  @Column(name = "NM_OBSERVACAO")
  private String observacao;

  @ManyToOne
  @JoinColumn(name = "CD_PRESTADOR")
  private Prestador prestador;

  @Column(name = "DH_CRIACAO")
  private Date dataCriacao;

  @Column(name = "DH_IMPRESSAO")
  private Date dataImpressao;

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

  public TipoDocumentoManualEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoDocumentoManualEnum tipo) {
    this.tipo = tipo;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public Prestador getPrestador() {
    return prestador;
  }

  public void setPrestador(Prestador prestador) {
    this.prestador = prestador;
  }

  public Date getDataCriacao() {
    return dataCriacao;
  }

  public void setDataCriacao(Date dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public Date getDataImpressao() {
    return dataImpressao;
  }

  public void setDataImpressao(Date dataImpressao) {
    this.dataImpressao = dataImpressao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

}
