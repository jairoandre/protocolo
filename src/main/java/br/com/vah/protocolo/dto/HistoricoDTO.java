package br.com.vah.protocolo.dto;

import java.math.BigDecimal;
import java.util.Date;

public class HistoricoDTO {

	private BigDecimal id;
	private BigDecimal idProtocolo;
	private Date dataAlteracao;
	private BigDecimal acao;
	private String setorOrigem;
	private String setorDestino;

	public HistoricoDTO(Object[] hist) {

		this.id =  (BigDecimal) hist[0];
		this.idProtocolo =  (BigDecimal) hist[1];
		this.dataAlteracao = (Date) hist[2];
		this.acao = (BigDecimal) hist[3];
		this.setorOrigem = (String) hist[4];
		this.setorDestino = (String) hist[5];

	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getIdProtocolo() {
		return idProtocolo;
	}

	public void setIdProtocolo(BigDecimal idProtocolo) {
		this.idProtocolo = idProtocolo;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public BigDecimal getAcao() {
		return acao;
	}

	public void setAcao(BigDecimal acao) {
		this.acao = acao;
	}

	public String getSetorOrigem() {
		return setorOrigem;
	}

	public void setSetorOrigem(String setorOrigem) {
		this.setorOrigem = setorOrigem;
	}

	public String getSetorDestino() {
		return setorDestino;
	}

	public void setSetorDestino(String setorDestino) {
		this.setorDestino = setorDestino;
	}

}
