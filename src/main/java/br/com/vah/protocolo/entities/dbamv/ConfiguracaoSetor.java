package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Jairoportela on 17/02/2017.
 */
@Entity
@Table(name = "CONFIG_PAGU_SETOR", schema = "DBAMV")
public class ConfiguracaoSetor extends BaseEntity {

  @Id
  @Column(name = "CD_SETOR")
  private Long id;

  @Column(name = "HR_PRESC_MED")
  private Date horaInicioPreMed;

  public ConfiguracaoSetor() {
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Date getHoraInicioPreMed() {
    return horaInicioPreMed;
  }

  public void setHoraInicioPreMed(Date horaInicioPreMed) {
    this.horaInicioPreMed = horaInicioPreMed;
  }

  @Override
  public String getLabelForSelectItem() {
    return id == null ? "" : id.toString();
  }


}
