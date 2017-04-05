package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.AcaoHistoricoEnum;
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

  @Column(name = "NM_DESCRICAO")
  private String descricao;


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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
