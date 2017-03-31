package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 28/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_SALA", schema = "USRDBVAH")
public class Sala extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqSala", sequenceName = "SEQ_NPTC_SALA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSala")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String titulo;

  @OneToMany(mappedBy = "sala")
  private List<Armario> armarios;

  @Column(name = "DT_CRIACAO")
  private Date criacao;

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

  public List<Armario> getArmarios() {
    return armarios;
  }

  public void setArmarios(List<Armario> armarios) {
    this.armarios = armarios;
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
