package br.com.vah.protocolo.entities.usrdbvah;

import br.com.vah.protocolo.constants.RolesEnum;
import br.com.vah.protocolo.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TB_NPTC_USUARIO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = User.ALL, query = "SELECT u FROM User u"),
    @NamedQuery(name = User.COUNT, query = "SELECT COUNT(u) FROM User u"),
    @NamedQuery(name = User.FIND_BY_LOGIN, query = "SELECT u FROM User u where u.login = :login")})
public class User extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "User.populatedItems";
  public static final String COUNT = "User.countTotal";
  public static final String FIND_BY_LOGIN = "User.findByLogin";

  @Id
  @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_NPTC_USUARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
  @Column(name = "ID")
  private Long id;

  @Column(name = "DS_LOGIN", nullable = false, unique = true)
  private String login;

  @Column(name = "NM_TITULO", nullable = true)
  private String title;

  @Column(name = "CD_ROLE", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private RolesEnum role;

  public User() {
    role = RolesEnum.SECRETARIA;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;

  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public RolesEnum getRole() {
    return role;
  }

  public void setRole(RolesEnum role) {
    this.role = role;
  }

  @Override
  public String getLabelForSelectItem() {
    return login;
  }



}