package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 22/03/2017.
 */
@Stateless
public class DocumentoProtocoloSrv extends AbstractSrv<DocumentoProtocolo> {

  public List<DocumentoProtocolo> buscarDocumentosProtocolos(Protocolo protocolo, Date begin, Date end) {
    Session session = getSession();
    Criteria criteria = getSession().createCriteria(DocumentoProtocolo.class);
    Atendimento atendimento = protocolo.getAtendimento();
    if (atendimento != null) {
      criteria.add(Restrictions.eq("atendimento", atendimento.getId()));
    }

    return null;

  }

}
