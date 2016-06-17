package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "CONVENIO", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Convenio.ALL, query = "SELECT a FROM Convenio a"),
    @NamedQuery(name = Convenio.COUNT, query = "SELECT COUNT(a) FROM Convenio a")})
public class Convenio extends BaseEntity {

  public final static String ALL = "Convenio.all";
  public final static String COUNT = "Convenio.count";

  @Id
  @Column(name = "CD_CONVENIO")
  private Long id;

  @Column(name = "NM_CONVENIO")
  private String title;

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

  @Override
  public String getLabelForSelectItem() {
    return String.format("%l - %s", id, getTitle());
  }
}
