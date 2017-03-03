package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.util.DateUtility;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
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
    Disjunction disjunction = Restrictions.disjunction();

    Date begin = DateUtility.zeroHour(range[0]);
    Date end = DateUtility.zeroHour(range[1]);

    // Contas que se iniciam e terminam dentro do período
    Conjunction dentroDoPeriodo = Restrictions.conjunction();
    dentroDoPeriodo.add(Restrictions.ge("inicio", begin));
    dentroDoPeriodo.add(Restrictions.le("fim", end));

    // Contas que iniciam antes do período e terminam dentro do período
    Conjunction iniciaAntesTerminaDentro = Restrictions.conjunction();
    iniciaAntesTerminaDentro.add(Restrictions.lt("inicio", begin));
    iniciaAntesTerminaDentro.add(Restrictions.between("fim", begin, end));

    // Contas que iniciam antes do período e terminam dentro do período
    Conjunction iniciaDentroTerminaFora = Restrictions.conjunction();
    iniciaDentroTerminaFora.add(Restrictions.between("inicio", begin, end));
    iniciaDentroTerminaFora.add(Restrictions.gt("fim", end));

    // Contas que iniciam antes do período e terminam dentro do período
    Conjunction iniciaETerminaFora = Restrictions.conjunction();
    iniciaETerminaFora.add(Restrictions.lt("inicio", begin));
    iniciaETerminaFora.add(Restrictions.gt("fim", end));

    disjunction.add(dentroDoPeriodo).add(iniciaAntesTerminaDentro).add(iniciaDentroTerminaFora).add(iniciaETerminaFora);

    criteria.add(disjunction);

    List<RegFaturamento> result = criteria.list();
    result.forEach((item) -> item.setProtocolo(protocolo));
    return result;
  }
}
