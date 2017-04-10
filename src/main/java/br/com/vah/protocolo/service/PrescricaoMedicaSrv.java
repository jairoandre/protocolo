package br.com.vah.protocolo.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

/**
 * Created by jairoportela on 16/06/2016.
 */

@Stateless
public class PrescricaoMedicaSrv extends AbstractSrv<PrescricaoMedica> {

  public PrescricaoMedicaSrv() {
    super(PrescricaoMedica.class);
  }

  @SuppressWarnings("unchecked")
  public List<PrescricaoMedica> consultarPrescricoes(Protocolo protocolo, Date begin, Date end, Date dataReferencia) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(PrescricaoMedica.class, "pre");

    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
    criteria.add(Restrictions.isNotNull("dataHoraImpressao"));
    criteria.setFetchMode("items", FetchMode.SELECT);

    Criteria subCriteria = criteria.createCriteria("unidade", "u", JoinType.LEFT_OUTER_JOIN);
    subCriteria.add(Restrictions.eq("u.setor", protocolo.getOrigem().getSetorMV()));

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
    dt.setProjection(Projections.rowCount());
    dt.add(Restrictions.eqProperty("pre.id", "dp.codigo"));
    dt.add(Restrictions.in("dp.tipo", new TipoDocumentoEnum[] {TipoDocumentoEnum.PRESCRICAO, TipoDocumentoEnum.EVOLUCAO}));
    criteria.add(Subqueries.gt(2l, dt));

    Disjunction disj = Restrictions.disjunction();
    disj.add(Restrictions.eq("dataReferencia", dataReferencia));

    Conjunction conj01 = Restrictions.conjunction();
    conj01.add(Restrictions.lt("dataReferencia", dataReferencia));
    conj01.add(Restrictions.between("dataHoraImpressao", begin, end));

    disj.add(conj01);

    criteria.add(disj);

    return criteria.list();
  }

  public Boolean hasEvolucao(PrescricaoMedica prescricaoMedica) {

    Query query = getEm().createNamedQuery(PrescricaoMedica.EVOLUCAO);
    query.setParameter("id", prescricaoMedica.getId());
    List list = query.getResultList();

    return !list.isEmpty();

  }

}
