package br.com.vah.protocolo.entities.dbamv;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vah.protocolo.entities.BaseEntity;

@Entity
@Table(name = "CASOS_ATD", schema = "DBAMV")
public class HDA extends BaseEntity{
	
	@Id
	@Column(name = "CD_CASOS_ATD")
	private Long id;
	
	@Column(name = "DT_INICIO")
	private Date dataInicio;
	
	@ManyToOne
	@JoinColumn(name = "CD_ATENDIMENTO")
	private Atendimento atendimento;
	
	@ManyToOne
	@JoinColumn(name = "CD_PRESTADOR")
	private Prestador prestador;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	@Override
	public String getLabelForSelectItem() {
		return String.format("%d - %s", id, prestador.getNome());
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
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
}
