package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by jairoportela on 23/06/2016.
 */
@Entity
@Table(name = "REGISTRO_DOCUMENTO", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = RegistroDocumento.ALL, query = "SELECT r FROM RegistroDocumento r"),
    @NamedQuery(name = RegistroDocumento.COUNT, query = "SELECT COUNT(r) FROM RegistroDocumento r") })
public class RegistroDocumento extends BaseEntity {

  public final static String ALL = "RegistroDocumento.all";
  public final static String COUNT = "RegistroDocumento.count";

  @Id
  @Column(name = "CD_DOCUMENTO")
  private Long id;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
