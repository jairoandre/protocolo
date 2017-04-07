package br.com.vah.protocolo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.vah.protocolo.dto.HistoricoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.util.PaginatedSearchParam;

@Stateless
public class PacienteSrv extends AbstractSrv<Paciente> {
	public PacienteSrv() {
		super(Paciente.class);
	}

	public Paciente initializeListsAtendimentos(Long id) {
		List<Atendimento> tempAtendimento = new ArrayList<>();
		Paciente paciente = find(id);

		if (paciente != null) {
			new HashSet<>(paciente.getAtendimentos());

			for (Atendimento atendimento : paciente.getAtendimentos()) {
				if (atendimento.getTipo().equalsIgnoreCase("I")) {
					tempAtendimento.add(atendimento);
				}
			}

			paciente.setAtendimentos(tempAtendimento);

			return paciente;
		}
		else{
			return null;
		}
	}

	public List<Object[]> localizacaoProntuario(Long id) {
		List<Object[]> localizacao = new ArrayList<>();
		Session session = getEm().unwrap(Session.class);
		SQLQuery consulta = session.createSQLQuery("SELECT NPTC.* FROM USRDBVAH.TB_NPTC_PROTOCOLO NPTC"
				+ " WHERE NPTC.CD_ATENDIMENTO = "+id.toString());
		
		localizacao = consulta.list();
		
		return localizacao;
	}
	
	public List<HistoricoDTO> historicoAtendimento(Long atendimento) {
		List<Object[]> historicoObject = new ArrayList<>();
		List<HistoricoDTO> historicoDTO = new ArrayList<>();
		Session session = getEm().unwrap(Session.class);
		SQLQuery consulta = session.createSQLQuery(
				"SELECT HISTPRO.ID,"
						+ " HISTPRO.ID_PROTOCOLO,"
						+ " HISTPRO.DT_ALTERACAO,"
						+ " HISTPRO.CD_ACAO,"
						+ " SETORPROORIGEM.NM_SETOR  AS ORIGEM,"
						+ " SETORPRODESTINO.NM_SETOR AS DESTINO"

						+ " FROM USRDBVAH.TB_NPTC_HISTORICO HISTPRO"

						+ " LEFT JOIN USRDBVAH.TB_NPTC_SETOR SETORPROORIGEM"
						+ "  ON (HISTPRO.ID_SETOR_ORIGEM = SETORPROORIGEM.CD_SETOR)"

						+ " LEFT JOIN USRDBVAH.TB_NPTC_SETOR SETORPRODESTINO"
						+ "  ON (HISTPRO.ID_SETOR_DESTINO = SETORPRODESTINO.CD_SETOR)"

						+ " LEFT JOIN USRDBVAH.TB_NPTC_PROTOCOLO PRO"
						+ "  ON (HISTPRO.ID_PROTOCOLO = PRO.ID)"

						+ " WHERE HISTPRO.ID IN (SELECT T.ID"
												+ " FROM USRDBVAH.TB_NPTC_HISTORICO T, USRDBVAH.TB_NPTC_PROTOCOLO P"
												+ " WHERE T.ID_PROTOCOLO = P.ID"
												+ " AND P.Cd_Atendimento ="+atendimento.toString()+")");
		
		historicoObject = consulta.list();
		
		for (Object[] obj : historicoObject ){
			historicoDTO.add(new HistoricoDTO(obj));
		}
		return historicoDTO;
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
