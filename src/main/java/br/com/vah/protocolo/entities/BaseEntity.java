package br.com.vah.protocolo.entities;

import java.io.Serializable;

/**
 *
 * @author jairoportela
 *
 */
public abstract class BaseEntity implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public abstract Long getId();

  public abstract void setId(Long id);

  public abstract String getLabelForSelectItem();

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (getId() != null ? getId().hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null) {
      return false;
    } else if (!(obj instanceof BaseEntity)) {
      return false;
    } else if (((BaseEntity) obj).getId() != null && ((BaseEntity) obj).getId().equals(this.getId())) {
      return true;
    } else {
      return obj == this;
    }
  }

  @Override
  public String toString() {
    return "entity." + this.getClass() + "[ id=" + getId() + " ] ";
  }
}