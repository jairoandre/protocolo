package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 16/06/2016.
 */
@Entity
@Table(name = "TB_NPTC_HISTORICO", schema = "USRDBVAH")
public class Historico extends BaseEntity{
  @Id
  @SequenceGenerator(name = "seqHistoricoProtocolo", sequenceName = "SEQ_NPTC_HISTORICO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqHistoricoProtocolo")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_USER")
  private User autor;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO")
  private Protocolo protocolo;

  @Column(name = "DT_ALTERACAO")
  private Date data;

  @Column(name = "CD_STATUS", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadosProtocoloEnum estado;


  public Historico() {
    this.data = new Date();
  }

  public Historico(User autor, Protocolo protocolo, EstadosProtocoloEnum estado) {
    this();
    this.autor = autor;
    this.protocolo = protocolo;
    this.estado = estado;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User autor) {
    this.autor = autor;
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

  public EstadosProtocoloEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadosProtocoloEnum estado) {
    this.estado = estado;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
