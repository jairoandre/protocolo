package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by jairoportela on 14/06/2016.
 */
@Entity
@Table(name = "PRESTADOR", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Prestador.ALL, query = "SELECT p FROM Prestador p"),
    @NamedQuery(name = Prestador.COUNT, query = "SELECT COUNT(p) FROM Prestador p")})
public class Prestador extends BaseEntity {

  public static final String ALL = "Prestador.all";
  public static final String COUNT = "Prestador.count";


  @Id
  @Column(name = "CD_PRESTADOR")
  private Long id;

  @Column(name = "NM_PRESTADOR")
  private String nome;

  @ManyToOne
  @JoinColumn(name = "CD_CONSELHO")
  private Conselho conselho;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Conselho getConselho() {
    return conselho;
  }

  public void setConselho(Conselho conselho) {
    this.conselho = conselho;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
