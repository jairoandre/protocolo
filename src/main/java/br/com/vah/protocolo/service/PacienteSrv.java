package br.com.vah.protocolo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.util.PaginatedSearchParam;

@Stateless
public class PacienteSrv extends AbstractSrv<Paciente> {
	public PacienteSrv() {
		super(Paciente.class);
	}

	public Paciente initializeListsAtendimentos(Long id) {
		List<Atendimento> tempAtendimento = new ArrayList<>();
		Paciente paciente = find(id);

		new HashSet<>(paciente.getAtendimentos());

		for (Atendimento atendimento : paciente.getAtendimentos()) {
			if (atendimento.getTipo().equalsIgnoreCase("I")) {
				tempAtendimento.add(atendimento);
			}
		}

		paciente.setAtendimentos(tempAtendimento);

		return paciente;
	}

	public String localizacaoProntuario(Long id) {
		String localizacao = null;
		Session session = getEm().unwrap(Session.class);
		Query consulta = (Query) session.createSQLQuery("SELECT NPTC.NM_LOCALIZACAO FROM TB_NPTC_PROTOCOLO NPTC"
				+ " WHERE NPTC.CD_ATENDIMENTO = "+id);
		
		localizacao = (String) consulta.getSingleResult();
		
		
		
		
		
		
		return localizacao;
	}

	public Criteria createCriteria(PaginatedSearchParam params) {
		Session session = getEm().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Paciente.class);
		Long idParam = (Long) params.getParams().get("id");
		String pacienteParam = (String) params.getParams().get("name");
		if (idParam != null) {
			criteria.add(Restrictions.eq("id", idParam));
			criteria.add(Restrictions.eq("empresa", new Long(1)));
		} else if (pacienteParam != null && !pacienteParam.isEmpty()) {
			criteria.createAlias("name", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
		}
		criteria.createAlias("atendimentos", "atd").add(Restrictions.eq("atd.tipo", "I"));

		return criteria;
	}
}
