package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by jairoportela on 23/06/2016.
 */
@Entity
@Table(name = "DOCUMENTO", schema = "DBAMV")
public class Documento implements Serializable {

  @Id
  @Column(name = "CD_DOCUMENTO")
  private Long id;

  @Column(name = "DS_DOCUMENTO")
  private String descricao;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
