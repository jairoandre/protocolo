package br.com.vah.protocolo.entities.usrdbvah;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.vah.protocolo.constants.AcaoHistoricoEnum;
import br.com.vah.protocolo.entities.BaseEntity;

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

  @ManyToOne
  @JoinColumn(name = "ID_SETOR_ORIGEM")
  private SetorProtocolo origem;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR_DESTINO")
  private SetorProtocolo destino;

  @Column(name = "DT_ALTERACAO")
  private Date data;

  @Column(name = "CD_ACAO", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private AcaoHistoricoEnum acao;


  public Historico() {
    this.data = new Date();
  }

  public Historico(User autor, Protocolo protocolo, AcaoHistoricoEnum acao) {
    this();
    this.autor = autor;
    this.protocolo = protocolo;
    this.acao = acao;
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

  public SetorProtocolo getOrigem() {
    return origem;
  }

  public void setOrigem(SetorProtocolo origem) {
    this.origem = origem;
  }

  public SetorProtocolo getDestino() {
    return destino;
  }

  public void setDestino(SetorProtocolo destino) {
    this.destino = destino;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public AcaoHistoricoEnum getAcao() {
    return acao;
  }

  public void setAcao(AcaoHistoricoEnum acao) {
    this.acao = acao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
