package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.dbamv.Setor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class AvisoCirurgiaService extends DataAccessService<AvisoCirurgia> {

  public AvisoCirurgiaService() {
    super(AvisoCirurgia.class);
  }

  @SuppressWarnings("unchecked")
  public List<AvisoCirurgia> consultarAvisos(Atendimento atendimento, Date inicio, Date fim, Setor setor) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(AvisoCirurgia.class);

    criteria.add(Restrictions.eq("atendimento", atendimento));

    criteria.add(Restrictions.between("inicioCirurgia", inicio, fim));

    criteria.add(Restrictions.eq("tipo", "R"));

    if (setor != null) {
      Criteria salaCirurgiaCriteria = criteria.createCriteria("salaCirurgia");
      salaCirurgiaCriteria.add(Restrictions.eq("setor", setor));
    }

    return criteria.list();
  }


}
