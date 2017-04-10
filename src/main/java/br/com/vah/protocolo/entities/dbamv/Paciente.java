package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import java.util.List;

import javax.persistence.*;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "PACIENTE", schema = "DBAMV")
@NamedQueries({ @NamedQuery(name = Paciente.ALL, query = "SELECT p FROM Paciente p"),
		@NamedQuery(name = Paciente.COUNT, query = "SELECT COUNT(p) FROM Paciente p") })
public class Paciente extends BaseEntity {

	public final static String ALL = "Paciente.all";
	public final static String COUNT = "Paciente.count";

	@Id
	@Column(name = "CD_PACIENTE")
	private Long id;

	@Column(name = "NM_PACIENTE")
	private String name;
	
	@Column(name = "CD_MULTI_EMPRESA")
	private Long empresa;

	@OneToMany(mappedBy = "paciente")
	private List<Atendimento> atendimentos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getLabelForSelectItem() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

}
