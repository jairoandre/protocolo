package br.com.vah.protocolo.service;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import javax.ejb.Stateless;
import java.util.*;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class AtendimentoSrv extends AbstractSrv<Atendimento> {

  public AtendimentoSrv() {
    super(Atendimento.class);
  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Atendimento.class);
    Long idParam = (Long) params.getParams().get("id");
    String pacienteParam = (String) params.getParams().get("paciente");
    Boolean semAltaRecente = (Boolean) params.getParams().get("semAltaRecente");
    if (idParam != null) {
      criteria.add(Restrictions.eq("id", idParam));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      criteria.createAlias("paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    if (semAltaRecente != null && semAltaRecente) {
      Disjunction disjunction = Restrictions.disjunction();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -4);
      disjunction.add(Restrictions.isNull("dataAlta"));
      disjunction.add(Restrictions.ge("dataAlta", calendar.getTime()));
      criteria.add(disjunction);

      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegFaturamento.class, "r");
      Disjunction regFatDisj = Restrictions.disjunction();
      regFatDisj.add(Restrictions.eq("r.fechada", "N"));
      regFatDisj.add(Restrictions.isNull("r.remessa"));
      detachedCriteria.add(regFatDisj);
      detachedCriteria.setProjection(Projections.property("r.atendimento"));

      criteria.add(Subqueries.propertyIn("id", detachedCriteria));

    }
    return criteria;
  }

  public Map<String, Object> buscarAtendimentosProntoSocorro(String atendimentos) {
    Map<String, Object> resultado = new HashMap<>();
    List<String> invalidos = new ArrayList<>();
    List<DocumentoDTO> dtos = new ArrayList<>();
    resultado.put("invalidos", invalidos);
    resultado.put("dtos", dtos);
    if (atendimentos != null) {

      String[] atendimentosValues = atendimentos.split(";");
      List<Long> atendimentosLong = new ArrayList<>();
      for (String atendimentoStr : atendimentosValues) {
        try {
          Long atendimento = Long.valueOf(atendimentoStr);
          atendimentosLong.add(atendimento);
        } catch (Exception e) {
          // Erro de conversão numérica, ignore e continue iterando os outros atendimentos
          invalidos.add(atendimentoStr);
        }
      }

      if (!atendimentosLong.isEmpty()) {
        Criteria criteria = getSession().createCriteria(Atendimento.class, "atd");
        criteria.add(Restrictions.in("id", atendimentosLong));
        criteria.add(Restrictions.isNotNull("dataAlta"));
        criteria.add(Restrictions.eq("tipo", "U"));

        DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
        dt.setProjection(Projections.id());
        dt.add(Restrictions.eqProperty("atd.id", "dp.codigo"));
        dt.add(Restrictions.eq("dp.tipo", TipoDocumentoEnum.ATENDIMENTO));
        criteria.add(Subqueries.notExists(dt));

        List<Atendimento> lista = criteria.list();
        for (Atendimento atendimento : lista) {
          dtos.add(new DocumentoDTO(atendimento));
        }
      }
    }
    return resultado;
  }
}
