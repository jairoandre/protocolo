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
@Table(name = "TB_NPTC_CAIXA", schema = "USRDBVAH")
public class Caixa extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqCaixa", sequenceName = "SEQ_NPTC_CAIXA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCaixa")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String titulo;

  @ManyToOne
  @JoinColumn(name = "ID_SALA")
  private Sala sala;

  @OneToMany(mappedBy = "caixa", cascade = CascadeType.ALL)
  private List<Envelope> envelopes = new ArrayList<>();

  @Column(name = "DT_CRIACAO")
  private Date criacao = new Date();

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

  public Sala getSala() {
    return sala;
  }

  public void setSala(Sala sala) {
    this.sala = sala;
  }

  public List<Envelope> getEnvelopes() {
    return envelopes;
  }

  public void setEnvelopes(List<Envelope> envelopes) {
    this.envelopes = envelopes;
  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  @Override
  public String getLabelForSelectItem() {
    return titulo;
  }
}
