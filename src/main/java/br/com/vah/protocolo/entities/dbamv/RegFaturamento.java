package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jairoportela on 28/04/2016.
 */
@Entity
@Table(name = "REG_FAT", schema = "DBAMV")
public class RegFaturamento extends BaseEntity {

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

  @Column(name = "DT_INICIO")
  private Date inicio;

  @Column(name = "DT_FINAL")
  private Date fim;

  @Column(name = "DT_FECHAMENTO")
  private Date fechamento;

  @Transient
  private Protocolo protocolo;

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

  public Date getInicio() {
    return inicio;
  }

  public void setInicio(Date inicio) {
    this.inicio = inicio;
  }

  public Date getFim() {
    return fim;
  }

  public void setFim(Date fim) {
    this.fim = fim;
  }

  public Date getFechamento() {
    return fechamento;
  }

  public void setFechamento(Date fechamento) {
    this.fechamento = fechamento;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
  }

  @Override
  public String getLabelForSelectItem() {
    if (id != null && inicio != null && fechamento != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      return String.format("%d - %s à %s", id, sdf.format(inicio), sdf.format(fechamento));
    }
    return "";
  }
}
