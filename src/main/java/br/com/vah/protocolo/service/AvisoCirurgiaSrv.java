package br.com.vah.protocolo.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.AvisoCirurgia;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;

/**
 * Created by jairoportela on 23/06/2016.
 */
public class AvisoCirurgiaSrv extends AbstractSrv<AvisoCirurgia> {

  public AvisoCirurgiaSrv() {
    super(AvisoCirurgia.class);
  }

  @SuppressWarnings("unchecked")
  public List<AvisoCirurgia> consultarAvisos(Protocolo protocolo, Date inicio, Date fim, Date dataReferencia) {

    Session session = getEm().unwrap(Session.class);

    Criteria criteria = session.createCriteria(AvisoCirurgia.class, "aviso");

    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));

    criteria.add(Restrictions.between("inicioCirurgia", inicio, fim));

    criteria.add(Restrictions.eq("situacao", "R"));

    SetorProtocolo setor = protocolo.getOrigem();

    if (setor != null) {
      Criteria salaCirurgiaCriteria = criteria.createCriteria("salaCirurgia");
      salaCirurgiaCriteria.add(Restrictions.eq("setor", setor.getSetorMV()));
    }

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
    dt.setProjection(Projections.id());
    dt.add(Restrictions.eqProperty("aviso.id", "dp.codigo"));
    dt.add(Restrictions.eq("dp.tipo", TipoDocumentoEnum.DESCRICAO_CIRURGICA));
    criteria.add(Subqueries.notExists(dt));

    List<AvisoCirurgia> avisos = criteria.list();

    avisos.forEach((aviso) -> aviso.setDataReferencia(dataReferencia));

    return avisos;
  }


}
