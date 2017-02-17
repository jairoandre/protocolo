package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 10/02/2017.
 */
public class RegFaturamentoSrv extends AbstractSrv<RegFaturamento> {

  public RegFaturamentoSrv() {
    super(RegFaturamento.class);
  }

  public List<RegFaturamento> obterContas(Protocolo protocolo, Date[] range) {
    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(RegFaturamento.class);
    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
    criteria.add(Restrictions.ge("inicio", range[0]));
    criteria.add(Restrictions.lt("fim", range[1]));
    List<RegFaturamento> result = criteria.list();
    result.forEach((item) -> item.setProtocolo(protocolo));
    return result;
  }
}
