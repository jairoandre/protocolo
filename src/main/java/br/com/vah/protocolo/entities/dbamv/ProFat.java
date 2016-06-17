package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "PRO_FAT", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = ProFat.ALL, query = "SELECT e FROM ProFat e"),
    @NamedQuery(name = ProFat.COUNT, query = "SELECT COUNT(e) FROM ProFat e")})
public class ProFat extends BaseEntity {

  public final static String ALL = "ProFat.all";
  public final static String COUNT = "ProFat.count";

  @Id
  @Column(name = "CD_PRO_FAT")
  private String idStr;

  @Column(name = "DS_PRO_FAT")
  private String title;

  @Column(name = "CD_GRU_PRO")
  private Integer grupo;

  public String getIdStr() {
    return idStr;
  }

  public void setIdStr(String idStr) {
    this.idStr = idStr;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getGrupo() {
    return grupo;
  }

  public void setGrupo(Integer grupo) {
    this.grupo = grupo;
  }

  @Override
  public Long getId() {
    return null;
  }

  @Override
  public void setId(Long id) {
    this.idStr = id.toString();
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
