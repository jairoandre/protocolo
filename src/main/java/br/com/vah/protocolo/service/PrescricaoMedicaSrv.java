package br.com.vah.protocolo.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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

  @SuppressWarnings("unchecked")
  public List<PrescricaoMedica> consultarPrescricoes(Atendimento atendimento, Date inicioDate, Date terminoDate) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(PrescricaoMedica.class, "pre");

    criteria.add(Restrictions.eq("atendimento", atendimento));

    criteria.add(Restrictions.between("datePreMed", inicioDate, terminoDate));

    criteria.setFetchMode("items", FetchMode.SELECT);

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("pre.id", "i.prescricaoMedica.id"))));


    return criteria.list();
  }

  public Boolean hasEvolucao(PrescricaoMedica prescricaoMedica) {

    Query query = getEm().createNamedQuery(PrescricaoMedica.EVOLUCAO);
    query.setParameter("id", prescricaoMedica.getId());
    List list = query.getResultList();

    return !list.isEmpty();

  }
}
