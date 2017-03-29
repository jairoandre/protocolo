package br.com.vah.protocolo.service;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.EvolucaoEnfermagem;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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
public class EvolucaoEnfermagemSrv extends AbstractSrv<EvolucaoEnfermagem> {

  public EvolucaoEnfermagemSrv() {
    super(EvolucaoEnfermagem.class);
  }

  @SuppressWarnings("unchecked")
  public List<EvolucaoEnfermagem> consultarEvolucoesEnfermagem(Protocolo protocolo, Date inicio, Date fim, Date referencia) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(EvolucaoEnfermagem.class, "evo");

    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));

    criteria.add(Restrictions.between("hora", inicio, fim));

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
    dt.setProjection(Projections.id());
    dt.add(Restrictions.eqProperty("evo.id", "dp.codigo"));
    dt.add(Restrictions.eq("dp.tipo", TipoDocumentoEnum.REGISTRO_DOCUMENTO));
    criteria.add(Subqueries.notExists(dt));

    List<EvolucaoEnfermagem> evolucoes = criteria.list();

    evolucoes.forEach((evolucao) -> evolucao.setDataReferencia(referencia));

    return evolucoes;
  }

  public String getDescricao(EvolucaoEnfermagem evolucao) {
    Session session = getEm().unwrap(Session.class);

    String sql  =
        "SELECT DS_EVO_ENF FROM DBAMV.EVO_ENF EVO " +
            "WHERE EVO.CD_EVO_ENF = :CD_EVO_ENF";

    SQLQuery query = session.createSQLQuery(sql);
    query.setParameter("CD_EVO_ENF", evolucao.getId());

    List<String> result = query.list();

    for (String val : result) {
      if (val != null) {
        return val;
      }
    }

    return "";

  }




}
