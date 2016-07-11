package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.RegistroDocumento;

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

  @ManyToOne
  @JoinColumn(name = "CD_PRE_MED")
  private PrescricaoMedica prescricaoMedica;

  @ManyToOne
  @JoinColumn(name = "CD_AVISO_CIRURGIA")
  private AvisoCirurgia avisoCirurgia;

  @ManyToOne
  @JoinColumn(name = "CD_REGISTRO_DOCUMENTO")
  private RegistroDocumento registroDocumento;

  @ManyToOne
  @JoinColumn(name = "ID_DOC_MANUAL")
  private DocumentoManual documentoManual;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO_REENVIO")
  private Protocolo reenvio;

  @Column(name = "CD_TIPO")
  @Enumerated(EnumType.STRING)
  private TipoDocumentoEnum tipo;

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

  public PrescricaoMedica getPrescricaoMedica() {
    return prescricaoMedica;
  }

  public void setPrescricaoMedica(PrescricaoMedica prescricaoMedica) {
    this.prescricaoMedica = prescricaoMedica;
  }

  public AvisoCirurgia getAvisoCirurgia() {
    return avisoCirurgia;
  }

  public void setAvisoCirurgia(AvisoCirurgia avisoCirurgia) {
    this.avisoCirurgia = avisoCirurgia;
  }

  public RegistroDocumento getRegistroDocumento() {
    return registroDocumento;
  }

  public void setRegistroDocumento(RegistroDocumento registroDocumento) {
    this.registroDocumento = registroDocumento;
  }

  public DocumentoManual getDocumentoManual() {
    return documentoManual;
  }

  public void setDocumentoManual(DocumentoManual documentoManual) {
    this.documentoManual = documentoManual;
  }

  public Protocolo getReenvio() {
    return reenvio;
  }

  public void setReenvio(Protocolo reenvio) {
    this.reenvio = reenvio;
  }

  public TipoDocumentoEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoDocumentoEnum tipo) {
    this.tipo = tipo;
  }
}
