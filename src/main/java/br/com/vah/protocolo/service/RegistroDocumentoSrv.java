package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.RegistroDocumento;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
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
public class RegistroDocumentoSrv extends AbstractSrv<RegistroDocumento> {

  public RegistroDocumentoSrv() {
    super(RegistroDocumento.class);
  }

  @SuppressWarnings("unchecked")
  public List<RegistroDocumento> consultarRegistros(Protocolo protocolo, Date inicio, Date fim) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(RegistroDocumento.class, "reg");

    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));

    criteria.add(Restrictions.eq("impresso", "S"));

    Long[] codigos = {53l, 84l, 122l, 130l, 148l, 69l};

    criteria.createCriteria("documento").add(Restrictions.not(Restrictions.in("id", codigos)));

    criteria.add(Restrictions.between("dataRegistro", inicio, fim));

    criteria.add(Restrictions.eq("multiEmpresa", 1l));

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("reg.id", "i.registroDocumento.id"))));

    return criteria.list();
  }


}
