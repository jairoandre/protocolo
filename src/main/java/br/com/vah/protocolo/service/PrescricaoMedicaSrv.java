package br.com.vah.protocolo.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.vah.protocolo.entities.dbamv.ConfiguracaoSetor;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

/**
 * Created by jairoportela on 16/06/2016.
 */

@Stateless
public class PrescricaoMedicaSrv extends AbstractSrv<PrescricaoMedica> {

  public PrescricaoMedicaSrv() {
    super(PrescricaoMedica.class);
  }

  public Date[] normalizeDate(Protocolo protocolo, Date begin, Date end) {
    if (protocolo.getOrigem().getSetorMV() != null) {

      Criteria criteria = getSession().createCriteria(ConfiguracaoSetor.class);
      criteria.add(Restrictions.eq("id", protocolo.getOrigem().getSetorMV().getId()));

      List<ConfiguracaoSetor> configs = criteria.list();
      if (configs.size() > 0) {
        Date hour = configs.get(0).getHoraInicioPreMed();
        if (hour == null) {
          Calendar hourCld = Calendar.getInstance();
          hourCld.setTime(hour);

          Calendar beginCld = Calendar.getInstance();
          beginCld.setTime(begin);

          Calendar endCld = Calendar.getInstance();
          endCld.setTime(end);


          // Verifica a existência de prescrições anteriores à data de busca
          Integer count = countPreMed(protocolo, begin);

          if (count > 0) {
            beginCld.set(Calendar.HOUR_OF_DAY, hourCld.get(Calendar.HOUR_OF_DAY));
            beginCld.set(Calendar.MINUTE, hourCld.get(Calendar.MINUTE));
            beginCld.set(Calendar.SECOND, hourCld.get(Calendar.SECOND));
          }



          endCld.set(Calendar.HOUR_OF_DAY, hourCld.get(Calendar.HOUR_OF_DAY));
          endCld.set(Calendar.MINUTE, hourCld.get(Calendar.MINUTE));
          endCld.set(Calendar.SECOND, hourCld.get(Calendar.SECOND));
          endCld.add(Calendar.SECOND, -1);

          return new Date[]{beginCld.getTime(), endCld.getTime()};

        }
      }


    }
    return new Date[]{begin, end};

  }

  public Integer countPreMed(Protocolo protocolo, Date begin) {
    Session session = getEm().unwrap(Session.class);

    String sql  =
        "SELECT COUNT(*) FROM DBAMV.PRE_MED PRE " +
            "LEFT OUTER JOIN DBAMV.UNIDADE U ON U.CD_UNID_INT = PRE.CD_UNID_INT " +
            "LEFT OUTER JOIN DBAMV.SETOR S ON S.CD_SETOR= U.CD_SETOR " +
            "WHERE PRE.CD_ATENDIMENTO = :CD_ATENDIMENTO " +
            "AND S.CD_SETOR = :CD_SETOR " +
            "AND PRE.DT_PRE_MED < :DT_PRE_MED ";

    SQLQuery query = session.createSQLQuery(sql);
    query.setParameter("CD_ATENDIMENTO", protocolo.getAtendimento().getId());
    query.setParameter("CD_SETOR", protocolo.getOrigem().getSetorMV().getId());
    query.setParameter("DT_PRE_MED", begin);

    Integer count = query.getFirstResult();

    return count;

  }

  @SuppressWarnings("unchecked")
  public List<PrescricaoMedica> consultarPrescricoes(Protocolo protocolo, Date begin, Date end) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(PrescricaoMedica.class, "pre");

    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
    criteria.setFetchMode("items", FetchMode.SELECT);

    Criteria subCriteria = criteria.createCriteria("unidade", "u", JoinType.LEFT_OUTER_JOIN);
    subCriteria.add(Restrictions.eq("u.setor", protocolo.getOrigem().getSetorMV()));

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("pre.id", "i.prescricaoMedica.id"))));

    criteria.add(Restrictions.between("datePreMed", begin, end));

    return criteria.list();
  }

  public Boolean hasEvolucao(PrescricaoMedica prescricaoMedica) {

    Query query = getEm().createNamedQuery(PrescricaoMedica.EVOLUCAO);
    query.setParameter("id", prescricaoMedica.getId());
    List list = query.getResultList();

    return !list.isEmpty();

  }
}
