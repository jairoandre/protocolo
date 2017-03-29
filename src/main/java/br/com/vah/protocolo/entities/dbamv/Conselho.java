package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jairoportela on 20/03/2017.
 */
@Entity
@Table(name = "CONSELHO", schema = "DBAMV")
public class Conselho {

  @Id
  @Column(name = "CD_CONSELHO")
  private Long id;

  @Column(name = "DS_CONSELHO")
  private String nome;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
