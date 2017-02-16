package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class AvisoCirurgiaSrv extends AbstractSrv<AvisoCirurgia> {

  public AvisoCirurgiaSrv() {
    super(AvisoCirurgia.class);
  }

  @SuppressWarnings("unchecked")
  public List<AvisoCirurgia> consultarAvisos(Atendimento atendimento, Date inicio, Date fim, SetorProtocolo setor) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(AvisoCirurgia.class, "aviso");

    criteria.add(Restrictions.eq("atendimento", atendimento));

    criteria.add(Restrictions.between("inicioCirurgia", inicio, fim));

    criteria.add(Restrictions.eq("tipo", "R"));

    if (setor != null) {
      Criteria salaCirurgiaCriteria = criteria.createCriteria("salaCirurgia");
      salaCirurgiaCriteria.add(Restrictions.eq("setor", setor.getSetorMV()));
    }

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("aviso.id", "i.avisoCirurgia.id"))));

    return criteria.list();
  }


}
