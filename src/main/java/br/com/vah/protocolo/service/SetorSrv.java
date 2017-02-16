package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class SetorSrv extends AbstractSrv<Setor> {

  public SetorSrv() {
    super(Setor.class);
  }

  @Override
  public Criteria createCriteria(PaginatedSearchParam params) {
    Criteria criteria = super.createCriteria(params);
    criteria.add(Restrictions.eq("multiEmpresa", 1));
    criteria.add(Restrictions.eq("grupoCusto", 2));
    return criteria;
  }
}
