package br.com.vah.protocolo.reports;

/**
 * Created by jairoportela on 28/04/2016.
 */
public class ReportTotalPorSetor {

  private String setor;

  private String convenio;

  private Integer totalCriadas;

  private Integer totalPendentes;

  private Integer totalFechadas;

  private Float tempoMedioRecebimento;

  private Float tempoMedioAuditoria;

  private Float tempoMedioSolicitacao;

  private Float tempoMedioResposta;

  public ReportTotalPorSetor(String setor, String convenio) {
    this.setor = setor;
    this.convenio = convenio;
    this.totalCriadas = 0;
    this.totalPendentes = 0;
    this.totalFechadas = 0;
    this.tempoMedioRecebimento = 0f;
    this.tempoMedioAuditoria = 0f;
    this.tempoMedioSolicitacao = 0f;
    this.tempoMedioResposta = 0f;
  }

  public String getSetor() {
    return setor;
  }

  public void setSetor(String setor) {
    this.setor = setor;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public Integer getTotalCriadas() {
    return totalCriadas;
  }

  public void setTotalCriadas(Integer totalCriadas) {
    this.totalCriadas = totalCriadas;
  }

  public Integer getTotalPendentes() {
    return totalPendentes;
  }

  public void setTotalPendentes(Integer totalPendentes) {
    this.totalPendentes = totalPendentes;
  }

  public Integer getTotalFechadas() {
    return totalFechadas;
  }

  public void setTotalFechadas(Integer totalFechadas) {
    this.totalFechadas = totalFechadas;
  }

  public Float getTempoMedioRecebimento() {
    return tempoMedioRecebimento;
  }

  public void setTempoMedioRecebimento(Float tempoMedioRecebimento) {
    this.tempoMedioRecebimento = tempoMedioRecebimento;
  }

  public Float getTempoMedioAuditoria() {
    return tempoMedioAuditoria;
  }

  public void setTempoMedioAuditoria(Float tempoMedioAuditoria) {
    this.tempoMedioAuditoria = tempoMedioAuditoria;
  }

  public Float getTempoMedioSolicitacao() {
    return tempoMedioSolicitacao;
  }

  public void setTempoMedioSolicitacao(Float tempoMedioSolicitacao) {
    this.tempoMedioSolicitacao = tempoMedioSolicitacao;
  }

  public Float getTempoMedioResposta() {
    return tempoMedioResposta;
  }

  public void setTempoMedioResposta(Float tempoMedioResposta) {
    this.tempoMedioResposta = tempoMedioResposta;
  }
}
