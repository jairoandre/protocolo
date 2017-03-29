package br.com.vah.protocolo.entities.usrdbvah;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jairoportela on 05/07/2016.
 */
@Entity
@Table(name = "TB_NPTC_IT_PROTOCOLO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = ItemProtocolo.ALL, query = "SELECT p FROM Protocolo p"),
    @NamedQuery(name = ItemProtocolo.COUNT, query = "SELECT COUNT(p) FROM Protocolo p")})
public class ItemProtocolo implements Serializable {

  public static final String ALL = "ItemProtocolo.all";
  public static final String COUNT = "ItemProtocolo.count";

  @Id
  @SequenceGenerator(name = "seqItemProtocolo", sequenceName = "SEQ_NPTC_IT_PROTOCOLO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItemProtocolo")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO")
  private Protocolo protocolo;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "ID_DOCUMENTO")
  private DocumentoProtocolo documento;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO_FILHO")
  private Protocolo filho;

  @ManyToOne
  @JoinColumn(name = "ID_CAIXA_ENTRADA")
  private CaixaEntrada caixa;

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

  public DocumentoProtocolo getDocumento() {
    return documento;
  }

  public void setDocumento(DocumentoProtocolo documento) {
    this.documento = documento;
  }

  public Protocolo getFilho() {
    return filho;
  }

  public void setFilho(Protocolo filho) {
    this.filho = filho;
  }

  public CaixaEntrada getCaixa() {
    return caixa;
  }

  public void setCaixa(CaixaEntrada caixa) {
    this.caixa = caixa;
  }
}
