package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "TB_ATENDIME", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Atendimento.ALL, query = "SELECT a FROM Atendimento a"),
    @NamedQuery(name = Atendimento.COUNT, query = "SELECT COUNT(a) FROM Atendimento a")})
public class Atendimento extends BaseEntity {

  public final static String ALL = "Atendimento.all";
  public final static String COUNT = "Atendimento.count";

  @Id
  @Column(name = "CD_ATENDIMENTO")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_PACIENTE")
  private Paciente paciente;

  @ManyToOne
  @JoinColumn(name = "CD_CONVENIO")
  private Convenio convenio;

  @Column(name = "DT_ATENDIMENTO")
  private Date dataAtendimento;

  @Column(name = "DT_ALTA")
  private Date dataAlta;
  
  @Column(name = "TP_ATENDIMENTO")
  private String tipo;
  
  @OneToMany
  @JoinColumn(name = "CD_ATENDIMENTO")
  private List<Protocolo> protocolos;

  public Atendimento() {}

  public Atendimento(Long l) {
    this.id = l;
  }
  
  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return String.format("%d - %s", id, paciente.getName());
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }

  public Date getDataAtendimento() {
    return dataAtendimento;
  }

  public void setDataAtendimento(Date dataAtendimento) {
    this.dataAtendimento = dataAtendimento;
  }

  public Date getDataAlta() {
    return dataAlta;
  }

  public void setDataAlta(Date dataAlta) {
    this.dataAlta = dataAlta;
  }

  @Override
  public String getLabelForSelectItem() {
    return String.format("%d - %s", id, paciente.getName());
  }

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

public List<Protocolo> getProtocolos() {
	return protocolos;
}

public void setProtocolos(List<Protocolo> protocolos) {
	this.protocolos = protocolos;
}
}
