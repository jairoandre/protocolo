package br.com.vah.protocolo.entities.dbamv;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

import br.com.vah.protocolo.entities.BaseEntity;

/**
 * The persistent class for the ITPRE_MED database table.
 * 
 */
@Entity
@NamedQuery(name="ItpreMed.findAll", query="SELECT i FROM ItemPrescricaoMedica i")
@Table(name = "ITPRE_MED", schema = "DBAMV")
public class ItemPrescricaoMedica extends BaseEntity {

	@Id
	@Column(name = "CD_ITPRE_MED")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DH_FINAL")
	private Date dhFinal;

	@Temporal(TemporalType.DATE)
	@Column(name = "DH_INICIAL")
	private Date dhInicial;

	@Temporal(TemporalType.DATE)
	@Column(name = "DH_REGISTRO")
	private Date dhRegistro;

	@Column(name = "DS_ITPRE_MED")
	private String dsItpreMed;

	public ItemPrescricaoMedica() {
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getDhFinal() {
		return dhFinal;
	}

	public void setDhFinal(Date dhFinal) {
		this.dhFinal = dhFinal;
	}

	public Date getDhInicial() {
		return dhInicial;
	}

	public void setDhInicial(Date dhInicial) {
		this.dhInicial = dhInicial;
	}

	public Date getDhRegistro() {
		return dhRegistro;
	}

	public void setDhRegistro(Date dhRegistro) {
		this.dhRegistro = dhRegistro;
	}

	public String getDsItpreMed() {
		return dsItpreMed;
	}

	public void setDsItpreMed(String dsItpreMed) {
		this.dsItpreMed = dsItpreMed;
	}

	@Override
	public String getLabelForSelectItem() {
		return null;
	}

}
