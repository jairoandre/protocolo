package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 14/06/2016.
 */
@Entity
@Table(name = "PRE_MED", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = PrescricaoMedica.ALL, query = "SELECT p FROM PrescricaoMedica p"),
    @NamedQuery(name = PrescricaoMedica.COUNT, query = "SELECT COUNT(p) FROM PrescricaoMedica p")})
public class PrescricaoMedica extends BaseEntity {

  public final static String ALL = "PrescricaoMedica.all";
  public final static String COUNT = "PrescricaoMedica.count";

  @Id
  @Column(name = "CD_PRE_MED")
  private Long id;

  @Column(name = "DT_PRE_MED")
  private Date datePreMed;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DH_CRIACAO")
  private Date dataHoraCriacao;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DH_IMPRESSAO")
  private Date dataHoraImpressao;

  @ManyToOne
  @JoinColumn(name = "CD_PRESTADOR")
  private Prestador prestador;

  @Column(name = "DS_EVOLUCAO")
  private String evolucao;

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
