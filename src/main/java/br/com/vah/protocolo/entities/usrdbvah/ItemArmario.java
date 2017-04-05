package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 31/03/2017.
 */
@Entity
@Table(name = "TB_NPTC_ITEM_ARMARIO", schema = "USRDBVAH")
public class ItemArmario extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqItemArmario", sequenceName = "SEQ_NPTC_CAIXA_ENTRADA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItemArmario")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO")
  private Protocolo protocolo;

  @ManyToOne
  @JoinColumn(name = "ID_ARMARIO")
  private Armario armario;

  @Column(name = "CD_LINHA")
  private String linha;

  @Column(name = "CD_COLUNA")
  private String coluna;

  @Column(name = "DT_CRIACAO")
  private Date criacao = new Date();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
  }

  public Armario getArmario() {
    return armario;
  }

  public void setArmario(Armario armario) {
    this.armario = armario;
  }

  public String getLinha() {
    return linha;
  }

  public void setLinha(String linha) {
    this.linha = linha;
  }

  public String getColuna() {
    return coluna;
  }

  public void setColuna(String coluna) {
    this.coluna = coluna;
  }

  public Date getCriacao() {
    return criacao;
  }

  public void setCriacao(Date criacao) {
    this.criacao = criacao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
