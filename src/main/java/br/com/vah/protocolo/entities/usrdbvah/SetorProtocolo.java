package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.dbamv.Setor;

import javax.persistence.*;

/**
 * Created by Jairoportela on 25/01/2017.
 */
@Entity
@Table(name = "TB_NPTC_SETOR", schema = "USRDBVAH")
public class SetorProtocolo extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqSetorProtocolo", sequenceName = "SEQ_NPTC_SETOR", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSetorProtocolo")
  @Column(name = "CD_SETOR")
  private Long id;

  @Column(name = "NM_SETOR")
  private String title;

  @Column(name = "CD_NIVEL")
  private Integer nivel;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR_MV")
  private Setor setorMV;


  @Override
  public Long getId() {
    return this.id;
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

  public Integer getNivel() {
    return nivel;
  }

  public void setNivel(Integer nivel) {
    this.nivel = nivel;
  }

  public Setor getSetorMV() {
    return setorMV;
  }

  public void setSetorMV(Setor setorMV) {
    this.setorMV = setorMV;
  }

  @Override
  public String getLabelForSelectItem() {
    return title;
  }
}
