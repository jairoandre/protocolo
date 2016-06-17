package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.dbamv.Setor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Entidade que representa um protocolo.
 *
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "TB_NPTC_PROTOCOLO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Protocolo.ALL, query = "SELECT p FROM Protocolo p"),
    @NamedQuery(name = Protocolo.COUNT, query = "SELECT COUNT(p) FROM Protocolo p")})
public class Protocolo extends BaseEntity {

  public static final String ALL = "Protocolo.all";
  public static final String COUNT = "Protocolo.count";

  @Id
  @SequenceGenerator(name = "seqProtocolo", sequenceName = "SEQ_NPTC_PROTOCOLO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProtocolo")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @ManyToOne
  @JoinColumn(name = "CD_REG_FAT")
  private RegFaturamento conta;

  @Column(name = "CD_STATUS")
  @Enumerated(EnumType.ORDINAL)
  private EstadosProtocoloEnum estado;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR_ORIGEM")
  private Setor origem;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR_DESTINO")
  private Setor destino;

  @OneToMany(mappedBy = "protocolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Historico> historico;

  @OneToMany(mappedBy = "protocolo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Comentario> comentarios;

  @Column(name = "DT_ENVIO")
  private Date dataEnvio;

  @Column(name = "DT_RESPOSTA")
  private Date dataResposta;

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_NPTC_PROT_PRESC", joinColumns = {
      @JoinColumn(name = "ID_PROTOCOLO")}, inverseJoinColumns = {@JoinColumn(name = "CD_PRE_MED")}, schema = "USRDBVAH")
  private List<PrescricaoMedica> prescricoes;

  @OneToMany(mappedBy = "protocolo")
  private Set<DocumentoManual> documentosManuais;

  @Column(name = "SN_REENVIADO")
  private Boolean reenviado = false;

  @Column(name = "SN_ARQUIVADO")
  private Boolean arquivado = false;

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

  public RegFaturamento getConta() {
    return conta;
  }

  public void setConta(RegFaturamento conta) {
    this.conta = conta;
  }

  public EstadosProtocoloEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadosProtocoloEnum estado) {
    this.estado = estado;
  }

  public Setor getOrigem() {
    return origem;
  }

  public void setOrigem(Setor origem) {
    this.origem = origem;
  }

  public Setor getDestino() {
    return destino;
  }

  public void setDestino(Setor destino) {
    this.destino = destino;
  }

  public Date getDataEnvio() {
    return dataEnvio;
  }

  public void setDataEnvio(Date dataEnvio) {
    this.dataEnvio = dataEnvio;
  }

  public Date getDataResposta() {
    return dataResposta;
  }

  public void setDataResposta(Date dataResposta) {
    this.dataResposta = dataResposta;
  }

  public List<PrescricaoMedica> getPrescricoes() {
    return prescricoes;
  }

  public void setPrescricoes(List<PrescricaoMedica> prescricoes) {
    this.prescricoes = prescricoes;
  }

  public Set<DocumentoManual> getDocumentosManuais() {
    return documentosManuais;
  }

  public void setDocumentosManuais(Set<DocumentoManual> documentosManuais) {
    this.documentosManuais = documentosManuais;
  }

  public Boolean getReenviado() {
    return reenviado;
  }

  public void setReenviado(Boolean reenviado) {
    this.reenviado = reenviado;
  }

  public Boolean getArquivado() {
    return arquivado;
  }

  public void setArquivado(Boolean arquivado) {
    this.arquivado = arquivado;
  }

  public List<Historico> getHistorico() {
    return historico;
  }

  public void setHistorico(List<Historico> historico) {
    this.historico = historico;
  }

  public List<Comentario> getComentarios() {
    return comentarios;
  }

  public void setComentarios(List<Comentario> comentarios) {
    this.comentarios = comentarios;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
