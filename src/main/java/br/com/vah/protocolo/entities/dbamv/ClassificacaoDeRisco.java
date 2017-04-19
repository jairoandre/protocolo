package br.com.vah.protocolo.entities.dbamv;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.vah.protocolo.entities.BaseEntity;

@Entity
@Table(name = "TRIAGEM_ATENDIMENTO", schema = "DBAMV")
public class ClassificacaoDeRisco extends BaseEntity {

	@Id
	@Column(name = "CD_TRIAGEM_ATENDIMENTO")
	private Long id;

	@OneToOne
	@JoinColumn(name = "CD_ATENDIMENTO")
	private Atendimento atendimento;

	@Column(name = "CD_MULTI_EMPRESA")
	private Long empresa;

	@Column(name = "DH_PRE_ATENDIMENTO")
	private Date dataPreAtendimento;

	@Column(name = "DH_PRE_ATENDIMENTO_FIM")
	private Date dataPreAtendimentoFim;

	@Column(name = "DH_REMOVIDO")
	private Date dataRemovido;
	
	@Column(name = "CD_USUARIO")
	private String usuario;
	
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
		return String.format("%s", usuario);
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public Date getDataPreAtendimento() {
		return dataPreAtendimento;
	}

	public void setDataPreAtendimento(Date dataPreAtendimento) {
		this.dataPreAtendimento = dataPreAtendimento;
	}

	public Date getDataPreAtendimentoFim() {
		return dataPreAtendimentoFim;
	}

	public void setDataPreAtendimentoFim(Date dataPreAtendimentoFim) {
		this.dataPreAtendimentoFim = dataPreAtendimentoFim;
	}

	public Date getDataRemovido() {
		return dataRemovido;
	}

	public void setDataRemovido(Date dataRemovido) {
		this.dataRemovido = dataRemovido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
