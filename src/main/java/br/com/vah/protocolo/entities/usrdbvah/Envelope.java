package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 28/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_ENVELOPE", schema = "USRDBVAH")
public class Envelope extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqEnvelope", sequenceName = "SEQ_NPTC_ENVELOPE", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEnvelope")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String titulo;

  @ManyToOne
  @JoinColumn(name = "ID_CAIXA")
  private Caixa caixa;

  @ManyToMany
  @JoinTable(name = "TB_NPTC_PROTOCOLO_ENVELOPE",
      joinColumns = {@JoinColumn(name = "ID_ENVELOPE")},
      inverseJoinColumns = {@JoinColumn(name = "ID_PROTOCOLO")},
      schema = "USRDBVAH")
  private List<Protocolo> itens = new ArrayList<>();

  @Column(name = "DT_CRIACAO")
  private Date criacao = new Date();

  @Transient
  private Boolean editing = false;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Caixa getCaixa() {
    return caixa;
  }

  public void setCaixa(Caixa caixa) {
    this.caixa = caixa;
  }

  public List<Protocolo> getItens() {
    return itens;
  }

  public void setItens(List<Protocolo> itens) {
    this.itens = itens;
  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  public Boolean getEditing() {
    return editing;
  }

  public void setEditing(Boolean editing) {
    this.editing = editing;
  }

  @Override
  public String getLabelForSelectItem() {
    return titulo;
  }
}
