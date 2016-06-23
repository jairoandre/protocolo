package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jairoportela on 23/06/2016.
 */
@Entity
@Table(name = "REGISTRO_DOCUMENTO", schema = "DBAMV")
public class RegistroDocumento implements Serializable {

  @Id
  @Column(name = "CD_REGISTRO_DOCUMENTO")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @ManyToOne
  @JoinColumn(name = "CD_DOCUMENTO")
  private Documento documento;

  @Column(name = "DT_REGISTRO")
  private Date dataRegistro;

  @Column(name = "SN_IMPRESSO")
  private String impresso;

  @Column(name = "CD_MULTI_EMPRESA")
  private Long multiEmpresa;

  @Column(name = "NM_USUARIO")
  private String nomeUsuario;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Atendimento getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Atendimento atendimento) {
    this.atendimento = atendimento;
  }

  public Date getDataRegistro() {
    return dataRegistro;
  }

  public void setDataRegistro(Date dataRegistro) {
    this.dataRegistro = dataRegistro;
  }

  public Documento getDocumento() {
    return documento;
  }

  public void setDocumento(Documento documento) {
    this.documento = documento;
  }

  public String getImpresso() {
    return impresso;
  }

  public void setImpresso(String impresso) {
    this.impresso = impresso;
  }

  public Long getMultiEmpresa() {
    return multiEmpresa;
  }

  public void setMultiEmpresa(Long multiEmpresa) {
    this.multiEmpresa = multiEmpresa;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }
}
