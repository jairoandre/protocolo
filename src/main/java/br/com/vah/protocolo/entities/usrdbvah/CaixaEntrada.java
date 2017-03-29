package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 20/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_CAIXA_ENTRADA", schema = "USRDBVAH")
public class CaixaEntrada extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqCaixaEntrada", sequenceName = "SEQ_NPTC_CAIXA_ENTRADA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCaixaEntrada")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO")
  private Protocolo protocolo;

  @ManyToOne
  @JoinColumn(name = "ID_DOCUMENTO")
  private DocumentoProtocolo documento;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @ManyToOne
  @JoinColumn(name = "CD_REG_FAT")
  private RegFaturamento contaFaturamento;

  @ManyToOne
  @JoinColumn(name = "CD_CONVENIO")
  private Convenio convenio;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR_ORIGEM")
  private SetorProtocolo origem;

  @ManyToOne
  @JoinColumn(name = "ID_SETOR_DESTINO")
  private SetorProtocolo destino;

  @Column(name = "DT_CRIACAO")
  private Date criacao;

  @Column(name = "DT_VINCULACAO")
  private Date dataVinculacao;

  @Column(name = "SN_VINCULADO")
  private Boolean vinculado = false;

  @Column(name = "NM_DESCRICAO")
  private String descricao;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public DocumentoProtocolo getDocumento() {
    return documento;
  }

  public Atendimento getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Atendimento atendimento) {
    this.atendimento = atendimento;
  }

  public RegFaturamento getContaFaturamento() {
    return contaFaturamento;
  }

  public void setContaFaturamento(RegFaturamento contaFaturamento) {
    this.contaFaturamento = contaFaturamento;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }

  public void setDocumento(DocumentoProtocolo documento) {
    this.documento = documento;
  }

  public SetorProtocolo getOrigem() {
    return origem;
  }

  public void setOrigem(SetorProtocolo origem) {
    this.origem = origem;
  }

  public SetorProtocolo getDestino() {
    return destino;
  }

  public void setDestino(SetorProtocolo destino) {
    this.destino = destino;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;

  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  public Date getDataVinculacao() {
    return dataVinculacao;
  }

  public void setDataVinculacao(Date dataVinculacao) {
    this.dataVinculacao = dataVinculacao;
  }

  public Boolean getVinculado() {
    return vinculado;
  }

  public void setVinculado(Boolean vinculado) {
    this.vinculado = vinculado;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @Override
  public String getLabelForSelectItem() {
    return descricao;
  }
}
