package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 22/06/2016.
 */
@Entity
@Table(name = "EVO_ENF", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = EvolucaoEnfermagem.ALL, query = "SELECT e FROM EvolucaoEnfermagem e"),
    @NamedQuery(name = EvolucaoEnfermagem.COUNT, query = "SELECT COUNT(e) FROM EvolucaoEnfermagem e") })
public class EvolucaoEnfermagem extends BaseEntity {

  public final static String ALL = "EvolucaoEnfermagem.all";
  public final static String COUNT = "EvolucaoEnfermagem.count";

  @Id
  @Column(name = "CD_EVO_ENF")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @ManyToOne
  @JoinColumn(name = "CD_PRESTADOR")
  private Prestador prestador;

  @Transient
  private String descricao;

  @Column(name = "NM_USUARIO")
  private String usuario;

  @Column(name = "DT_EVO_ENF")
  private Date data;

  @Column(name = "HR_EVO_ENF")
  private Date hora;

  @Column(name = "DT_IMPRESSAO")
  private Date dataImpressao;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Atendimento getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Atendimento atendimento) {
    this.atendimento = atendimento;
  }

  public Prestador getPrestador() {
    return prestador;
  }

  public void setPrestador(Prestador prestador) {
    this.prestador = prestador;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Date getHora() {
    return hora;
  }

  public void setHora(Date hora) {
    this.hora = hora;
  }

  public Date getDataImpressao() {
    return dataImpressao;
  }

  public void setDataImpressao(Date dataImpressao) {
    this.dataImpressao = dataImpressao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
