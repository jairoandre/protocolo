package br.com.vah.protocolo.entities.dbamv;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 14/06/2016.
 */
@Entity
@Table(name = "PRE_MED", schema = "DBAMV")
@NamedNativeQueries({
		@NamedNativeQuery(name = PrescricaoMedica.EVOLUCAO, query = "select 1 from DBAMV.PRE_MED WHERE CD_PRE_MED = :id AND DS_EVOLUCAO IS NOT NULL") })
@NamedQueries({ @NamedQuery(name = PrescricaoMedica.ALL, query = "SELECT p FROM PrescricaoMedica p"),
		@NamedQuery(name = PrescricaoMedica.COUNT, query = "SELECT COUNT(p) FROM PrescricaoMedica p") })
public class PrescricaoMedica extends BaseEntity {

	public final static String ALL = "PrescricaoMedica.all";
	public final static String COUNT = "PrescricaoMedica.count";
	public final static String EVOLUCAO = "PrescricaoMedica.evolucao";

	@Id
	@Column(name = "CD_PRE_MED")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CD_ATENDIMENTO")
	private Atendimento atendimento;

	@Column(name = "HR_PRE_MED")
	private Date datePreMed;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DH_CRIACAO")
	private Date dataHoraCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DH_IMPRESSAO")
	private Date dataHoraImpressao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_VALIDADE")
	private Date dataValidade;

	@Column(name = "DT_REFERENCIA")
	private Date dataReferencia;
	
	@Column(name = "TP_PRE_MED")
	private String tipoPrescricao;

	@ManyToOne
	@JoinColumn(name = "CD_PRESTADOR")
	private Prestador prestador;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "CD_PRE_MED")
	private List<ItemPrescricaoMedica> items;

	@ManyToOne
	@JoinColumn(name = "CD_UNID_INT")
	private UnidadeInternacao unidade;
	
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

	public Date getDatePreMed() {
		return datePreMed;
	}

	public void setDatePreMed(Date datePreMed) {
		this.datePreMed = datePreMed;
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

	public Date getDataValidade() {
		return dataValidade;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}

	@Override
	public String getLabelForSelectItem() {
		return null;
	}

	public String getTipoPrescricao() {
		return tipoPrescricao;
	}

	public void setTipoPrescricao(String tpPreMed) {
		this.tipoPrescricao = tpPreMed;
	}

	public List<ItemPrescricaoMedica> getItems() {
		return items;
	}

	public void setItems(List<ItemPrescricaoMedica> items) {
		this.items = items;
	}

	public UnidadeInternacao getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeInternacao unidade) {
		this.unidade = unidade;
	}
}
