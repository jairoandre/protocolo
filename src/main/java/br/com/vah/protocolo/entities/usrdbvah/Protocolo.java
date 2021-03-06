package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.dbamv.*;

import javax.persistence.*;
import java.util.*;

/**
 * Entidade que representa um protocolo.
 * <p>
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

  @Column(name = "CD_STATUS")
  @Enumerated(EnumType.ORDINAL)
  private EstadosProtocoloEnum estado = EstadosProtocoloEnum.RASCUNHO;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR_ORIGEM")
  private SetorProtocolo origem;

  @ManyToOne
  @JoinColumn(name = "CD_REG_FAT")
  private RegFaturamento contaFaturamento;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR_DESTINO")
  private SetorProtocolo destino;

  @OneToMany(mappedBy = "protocolo", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ItemProtocolo> itens = new LinkedHashSet<>();

  @OneToMany(mappedBy = "protocolo", cascade = CascadeType.ALL)
  private List<Historico> historico;

  @OneToMany(mappedBy = "protocolo", cascade = CascadeType.ALL)
  private List<Comentario> comentarios;

  @Column(name = "DT_ENVIO")
  private Date dataEnvio;

  @Column(name = "DT_RESPOSTA")
  private Date dataResposta;

  @Column(name = "SN_REENVIADO")
  private Boolean reenviado = false;

  @Column(name = "SN_ARQUIVADO")
  private Boolean arquivado = false;

  @Column(name = "NM_LOCALIZACAO")
  private String localizacao;

  @Column(name = "DT_INICIO")
  private Date inicio;

  @Column(name = "DT_FIM")
  private Date fim;

  @Column(name = "SN_PENDENCIAS")
  private Boolean comPendencias = false;

  @Column(name = "NM_DOCS_PENDENTES")
  private String docsPendentes;

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

  public EstadosProtocoloEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadosProtocoloEnum estado) {
    this.estado = estado;
  }

  public SetorProtocolo getOrigem() {
    return origem;
  }

  public void setOrigem(SetorProtocolo origem) {
    this.origem = origem;
  }

  public RegFaturamento getContaFaturamento() {
    return contaFaturamento;
  }

  public void setContaFaturamento(RegFaturamento contaFaturamento) {
    this.contaFaturamento = contaFaturamento;
  }

  public SetorProtocolo getDestino() {
    return destino;
  }

  public void setDestino(SetorProtocolo destino) {
    this.destino = destino;
  }

  public Set<ItemProtocolo> getItens() {
    return itens;
  }

  public void setItens(Set<ItemProtocolo> itens) {
    this.itens = itens;
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

  public String getLocalizacao() {
    return localizacao;
  }

  public void setLocalizacao(String localizacao) {
    this.localizacao = localizacao;
  }

  public Date getInicio() {
    return inicio;
  }

  public void setInicio(Date inicio) {
    this.inicio = inicio;
  }

  public Date getFim() {
    return fim;
  }

  public void setFim(Date fim) {
    this.fim = fim;
  }

  public Boolean getComPendencias() {
    return comPendencias;
  }

  public void setComPendencias(Boolean comPendencias) {
    this.comPendencias = comPendencias;
  }

  public String getDocsPendentes() {
    return docsPendentes;
  }

  public void setDocsPendentes(String docsPendentes) {
    this.docsPendentes = docsPendentes;
  }

  @Override
  public String getLabelForSelectItem() {
    return String.format("Protocolo nº %d", id);
  }
}
