package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "ESPECIALID", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Especialidade.ALL, query = "SELECT e FROM Especialidade e"),
    @NamedQuery(name = Especialidade.COUNT, query = "SELECT COUNT(e) FROM Especialidade e"),
    @NamedQuery(name = Especialidade.ACTIVES, query = "SELECT e FROM Especialidade e WHERE e.ativo = 'S'")})
public class Especialidade extends BaseEntity {

  public final static String ALL = "Especialidade.all";
  public final static String COUNT = "Especialidade.count";
  public final static String ACTIVES = "Especialidade.actives";

  @Id
  @Column(name = "CD_ESPECIALID")
  private Long id;

  @Column(name = "DS_ESPECIALID")
  private String title;

  @Column(name = "SN_ATIVO")
  private String ativo;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAtivo() {
    return ativo;
  }

  public void setAtivo(String ativo) {
    this.ativo = ativo;
  }

  @Override
  public String getLabelForSelectItem() {
    return String.format("%l - %s", id, getTitle());
  }
}
