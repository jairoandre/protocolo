package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 28/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_ARMARIO", schema = "USRDBVAH")
public class Armario extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqArmario", sequenceName = "SEQ_NPTC_ARMARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqArmario")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_TITULO")
  private String titulo;

  @ManyToOne
  @JoinColumn(name = "ID_SALA")
  private Sala sala;

  @OneToMany(mappedBy = "armario")
  private List<ItemArmario> itens;

  @Column(name = "DT_CRIACAO")
  private Date criacao;

  @Column(name = "VL_COLUNAS")
  private Integer colunas;

  @Column(name = "VL_LINHAS")
  private Integer linhas;

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

  public List<ItemArmario> getItens() {
    return itens;
  }

  public void setItens(List<ItemArmario> itens) {
    this.itens = itens;
  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  public Integer getColunas() {
    return colunas;
  }

  public void setColunas(Integer colunas) {
    this.colunas = colunas;
  }

  public Integer getLinhas() {
    return linhas;
  }

  public void setLinhas(Integer linhas) {
    this.linhas = linhas;
  }

  @Override
  public String getLabelForSelectItem() {
    return titulo;
  }
}
