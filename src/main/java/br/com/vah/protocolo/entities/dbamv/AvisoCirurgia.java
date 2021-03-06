package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 22/06/2016.
 */
@Entity
@Table(name = "TB_AVISO_CIRURGIA", schema = "DBAMV")
public class AvisoCirurgia extends BaseEntity {

  @Id
  @Column(name = "CD_AVISO_CIRURGIA")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @Column(name = "TP_SITUACAO")
  private String situacao;

  @Column(name = "DT_INICIO_CIRURGIA")
  private Date inicioCirurgia;

  @ManyToOne
  @JoinColumn(name = "CD_SAL_CIR")
  private SalaCirurgia salaCirurgia;

  @Transient
  private Date dataReferencia;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Atendimento getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Atendimento atendimento) {
    this.atendimento = atendimento;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public Date getInicioCirurgia() {
    return inicioCirurgia;
  }

  public void setInicioCirurgia(Date inicioCirurgia) {
    this.inicioCirurgia = inicioCirurgia;
  }

  public SalaCirurgia getSalaCirurgia() {
    return salaCirurgia;
  }

  public void setSalaCirurgia(SalaCirurgia salaCirugia) {
    this.salaCirurgia = salaCirugia;
  }

  public Date getDataReferencia() {
    return dataReferencia;
  }

  public void setDataReferencia(Date dataReferencia) {
    this.dataReferencia = dataReferencia;
  }

  @Override
  public String getLabelForSelectItem() {return null;

  }
}
