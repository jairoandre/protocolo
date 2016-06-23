package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jairoportela on 23/06/2016.
 */
@Entity
@Table(name = "SAL_CIR", schema = "DBAMV")
public class SalaCirurgia implements Serializable {

  @Id
  @Column(name = "CD_SAL_CIR")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR")
  private Setor setor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }
}
