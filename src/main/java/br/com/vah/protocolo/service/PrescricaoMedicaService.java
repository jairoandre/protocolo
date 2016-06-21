package br.com.vah.protocolo.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;

/**
 * Created by jairoportela on 16/06/2016.
 */

@Stateless
public class PrescricaoMedicaService extends DataAccessService<PrescricaoMedica> {
	
	public PrescricaoMedicaService(){
		super (PrescricaoMedica.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PrescricaoMedica> consultarPrescricoes(Atendimento atendimento, Date inicioDate, Date terminoDate) {
	    
		Session session = getEm().unwrap(Session.class);
	    
		Criteria criteria = session.createCriteria(PrescricaoMedica.class);
		
	    criteria.add(Restrictions.eq("atendimento", atendimento));
	    
	    criteria.add(Restrictions.between("datePreMed", inicioDate, terminoDate));
	    
		return criteria.list();
	}
}
