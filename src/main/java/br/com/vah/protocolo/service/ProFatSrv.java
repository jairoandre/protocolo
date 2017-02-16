package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.ProFat;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class ProFatSrv extends AbstractSrv<ProFat> {

  public ProFatSrv() {
    super(ProFat.class);
  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(ProFat.class);
    String searchTerm = (String) params.getParams().get("title");
    Disjunction disjunction = Restrictions.disjunction();
    disjunction.add(Restrictions.eq("idStr", searchTerm));
    disjunction.add(Restrictions.ilike("title", searchTerm, MatchMode.ANYWHERE));
    criteria.add(disjunction);
    criteria.add(Restrictions.not(Restrictions.in("grupo", new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 10, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86})));
    return criteria;
  }

}
