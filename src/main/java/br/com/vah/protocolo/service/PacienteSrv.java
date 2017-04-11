package br.com.vah.protocolo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.vah.protocolo.dto.HistoricoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Paciente;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.util.PaginatedSearchParam;

@Stateless
public class PacienteSrv extends AbstractSrv<Paciente> {

    private
    @Inject
    ProtocoloSrv protocoloService;

    public PacienteSrv() {
        super(Paciente.class);
    }

    public Paciente initializeListsAtendimentos(Long id) {
        List<Atendimento> tempAtendimento = new ArrayList<>();
        Paciente paciente = find(id);
        List<Protocolo> listProtocolos = new ArrayList<>();
        if (paciente != null) {
            new HashSet<>(paciente.getAtendimentos());

            for (Atendimento atendimento : paciente.getAtendimentos()) {
                if (atendimento.getTipo().equalsIgnoreCase("I") || atendimento.getTipo().equalsIgnoreCase("U")) {
                    for (Protocolo nptc : atendimento.getProtocolos()) {
                        if (nptc != null) {

                            if (!tempAtendimento.contains(atendimento)) {
                                tempAtendimento.add(atendimento);
                            }
                        }
                    }
                }
            }

            paciente.setAtendimentos(tempAtendimento);

            return paciente;
        } else {
            return null;
        }
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
            criteria.add(Restrictions.ilike("name", pacienteParam, MatchMode.ANYWHERE));
        }
        //criteria.createAlias("atendimentos", "atd").add(Restrictions.eq("atd.tipo", "I"));

        return criteria;
    }
}
