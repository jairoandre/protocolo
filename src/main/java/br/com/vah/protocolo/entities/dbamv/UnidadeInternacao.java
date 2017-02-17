package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 17/02/2017.
 */
@Entity
@Table(name = "UNID_INT", schema = "DBAMV")
public class UnidadeInternacao extends BaseEntity {

  @Id
  @Column(name = "CD_UNID_INT")
  private Long id;

  @Column(name = "DS_UNID_INT")
  private String title;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR")
  private Setor setor;


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

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  @Override
  public String getLabelForSelectItem() {
    return id == null ? "-" : id.toString();
  }
}
