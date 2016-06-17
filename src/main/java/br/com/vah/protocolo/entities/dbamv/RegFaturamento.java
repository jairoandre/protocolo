package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.*;

/**
 * Created by jairoportela on 28/04/2016.
 */
@Entity
@Table(name = "REG_FAT", schema = "DBAMV")
public class RegFaturamento {

  @Id
  @Column(name = "CD_REG_FAT")
  private Long id;

  @OneToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @Column(name = "SN_FECHADA")
  private String fechada;

  @Column(name = "CD_REMESSA")
  private Long remessa;

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

  public String getFechada() {
    return fechada;
  }

  public void setFechada(String fechada) {
    this.fechada = fechada;
  }

  public Long getRemessa() {
    return remessa;
  }

  public void setRemessa(Long remessa) {
    this.remessa = remessa;
  }
}
