package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 16/06/2016.
 */
@Entity
@Table(name = "TB_NPTC_COMENTARIO", schema = "USRDBVAH")
public class Comentario extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqComentario", sequenceName = "SEQ_NPTC_COMENTARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqComentario")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_COMENTARIO", nullable = false)
  private String comentario;

  @Column(name = "DT_CRIACAO")
  private Date data;

  @ManyToOne
  @JoinColumn(name = "ID_USER")
  private User autor;

  @ManyToOne
  @JoinColumn(name = "ID_PROTOCOLO")
  private Protocolo protocolo;

  public Comentario(){}

  public Comentario(Protocolo protocolo, User autor, String comentario, Date data) {
    this();
    this.protocolo = protocolo;
    this.autor = autor;
    this.comentario = comentario;
    this.data = data;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User autor) {
    this.autor = autor;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
  }

}
