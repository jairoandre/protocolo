package br.com.vah.protocolo.dto;

import java.util.Date;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;

public class DocumentoDTO {

	private TipoDocumentoEnum tipo;
	private PrescricaoMedica prescricao;
	private Date data;
	private String codigo;
	private String prestador;
	private Date dataHoraCriacao;
	private Date dataHoraImpressao;
	private String dsEvolucao;

	public DocumentoDTO(PrescricaoMedica prescricao) {

		this.tipo = TipoDocumentoEnum.PRESCRICAO_MEDICA;
		this.prescricao = prescricao;
		this.data = prescricao.getDatePreMed();
		this.codigo = String.valueOf(prescricao.getId());
		this.prestador = prescricao.getPrestador().getNome();
		this.dataHoraCriacao = prescricao.getDataHoraCriacao();
		this.dataHoraImpressao = prescricao.getDataHoraImpressao();

	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getPrestador() {
		return prestador;
	}

	public void setPrestador(String prestador) {
		this.prestador = prestador;
	}

	public Date getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(Date dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public Date getDataHoraImpressao() {
		return dataHoraImpressao;
	}

	public void setDataHoraImpressao(Date dataHoraImpressao) {
		this.dataHoraImpressao = dataHoraImpressao;
	}

	public TipoDocumentoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumentoEnum tipo) {
		this.tipo = tipo;
	}

	public PrescricaoMedica getPrescricao() {
		return prescricao;
	}

	public void setPrescricao(PrescricaoMedica prescricao) {
		this.prescricao = prescricao;
	}

	public String getDsEvolucao() {
		return dsEvolucao;
	}

	public void setDsEvolucao(String dsEvolucao) {
		this.dsEvolucao = dsEvolucao;
	}

	public DocumentoDTO() {
		// Documentos de prontuário
	}
}
