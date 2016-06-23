package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.RegistroDocumento;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class RegistroDocumentoService extends DataAccessService<RegistroDocumento> {

  public RegistroDocumentoService() {
    super(RegistroDocumento.class);
  }

  @SuppressWarnings("unchecked")
  public List<RegistroDocumento> consultarRegistros(Atendimento atendimento, Date inicio, Date fim) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(RegistroDocumento.class);

    criteria.add(Restrictions.eq("atendimento", atendimento));

    criteria.add(Restrictions.eq("impresso", "S"));

    Long[] codigos = {53l, 84l, 122l, 130l, 148l, 69l};

    criteria.createCriteria("documento").add(Restrictions.not(Restrictions.in("id", codigos)));

    criteria.add(Restrictions.between("dataRegistro", inicio, fim));

    criteria.add(Restrictions.eq("multiEmpresa", 1l));

    return criteria.list();
  }


}
