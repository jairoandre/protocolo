package br.com.vah.protocolo.service;


import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.PrescricaoMedica;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.exceptions.ProtocoloPersistException;
import br.com.vah.protocolo.reports.ReportLoader;
import br.com.vah.protocolo.reports.ReportTotalPorSetor;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class ProtocoloService extends DataAccessService<Protocolo> {

  private
  @Inject
  ReportLoader reportLoader;
  
  @Inject
  private PrescricaoMedicaService prescricaoMedicaService;
  
  public ProtocoloService() {
    super(Protocolo.class);
  }

  public StreamedContent relatorioTotalPorSetor(Date inicio, Date termino, List<Setor> setores, String[] relations) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Protocolo.class);

    Calendar beginCl = Calendar.getInstance();
    beginCl.setTime(inicio);
    beginCl.set(Calendar.HOUR_OF_DAY, 0);
    beginCl.set(Calendar.MINUTE, 0);
    beginCl.set(Calendar.SECOND, 0);

    Calendar endCl = Calendar.getInstance();
    endCl.setTime(termino);
    endCl.set(Calendar.HOUR_OF_DAY, 23);
    endCl.set(Calendar.MINUTE, 59);
    endCl.set(Calendar.SECOND, 59);


    criteria.add(Restrictions.between("data", beginCl.getTime(), endCl.getTime()));

    if (setores != null && !setores.isEmpty()) {
      criteria.add(Restrictions.in("setor", setores));
    }

    for (String relation : relations) {
      criteria.setFetchMode(relation, FetchMode.SELECT);
    }

    List<Protocolo> Protocolos = criteria.list();

    Map<String, ReportTotalPorSetor> map = new HashMap<>();

    for (Protocolo Protocolo : Protocolos) {
      String setor = Protocolo.getOrigem().getTitle();
      Atendimento atendimento = Protocolo.getAtendimento();
      String strConv = "SEM CONVÊNIO";
      if (atendimento != null) {
        Convenio convenio = atendimento.getConvenio();
        if (convenio != null) {
          strConv = convenio.getTitle();
        }
      }
      String key = setor + strConv;
      ReportTotalPorSetor report = map.get(key);
      if (report == null) {
        report = new ReportTotalPorSetor(setor, strConv);
        map.put(key, report);
      }

      report.setTotalCriadas(report.getTotalCriadas() + 1);

      switch (Protocolo.getEstado()) {
        case ENVIADO:
          report.setTotalPendentes(report.getTotalPendentes() + 1);
          break;
        case RECEBIDO:
          report.setTotalFechadas(report.getTotalFechadas() + 1);
          break;
      }

      Date data = Protocolo.getDataEnvio();
      Date dataResp = Protocolo.getDataResposta();

      if (dataResp != null) {
        long diasResp = (dataResp.getTime() - data.getTime()) / 1000 / 60 / 60 / 24;
        report.setTempoMedioRecebimento(report.getTempoMedioRecebimento() + Float.valueOf(diasResp / report.getTotalCriadas()));

      }
    }


    List<ReportTotalPorSetor> list = new ArrayList<>(map.values());
    Collections.sort(list, new Comparator<ReportTotalPorSetor>() {
      @Override
      public int compare(ReportTotalPorSetor o1, ReportTotalPorSetor o2) {
        int compareSetor = o1.getSetor().compareTo(o2.getSetor());
        if (compareSetor == 0) {
          return o2.getTotalCriadas().compareTo(o1.getTotalCriadas());
        }
        return compareSetor;
      }
    });

    return reportLoader.imprimeRelatorio("medias", list);

  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Protocolo.class);
    Long atendimentoId = (Long) params.getParams().get("atendimento");
    String pacienteParam = (String) params.getParams().get("paciente");
    Setor setor = (Setor) params.getParams().get("setor");
    EstadosProtocoloEnum[] estados = (EstadosProtocoloEnum[]) params.getParams().get("estados");
    Convenio[] convenios = (Convenio[]) params.getParams().get("convenios");
    Date[] dateRange = (Date[]) params.getParams().get("dateRange");
    Criteria atendimentoAlias = criteria.createAlias("atendimento", "a");
    if (atendimentoId != null) {
      atendimentoAlias.add(Restrictions.eq("a.id", atendimentoId));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      atendimentoAlias.createAlias("a.paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    if (convenios != null) {
      atendimentoAlias.add(Restrictions.in("a.convenio", convenios));
    }
    if (setor != null) {
      Disjunction disjunction = Restrictions.disjunction();
      disjunction.add(Restrictions.eq("origem", setor));
      disjunction.add(Restrictions.eq("destino", setor));
      criteria.add(disjunction);
    }
    if (estados != null) {
      criteria.add(Restrictions.in("estado", estados));
    }
    if (dateRange != null) {
      if (dateRange[0] != null) {
        if (dateRange[1] != null) {
          criteria.add(Restrictions.between("dataEnvio", dateRange[0], dateRange[1]));
        } else {
          criteria.add(Restrictions.ge("dataEnvio", dateRange[0]));
        }
      } else {
        criteria.add(Restrictions.le("dataEnvio", dateRange[1]));
      }
    }
    return criteria;
  }

  public void saveAll(List<Protocolo> Protocolos) throws ProtocoloPersistException {
    for (Protocolo Protocolo : Protocolos) {
      try {
        create(Protocolo);
      } catch (Exception e) {
        throw new ProtocoloPersistException(String.format("Erro ao criar Protocolo para o atendimento %s", Protocolo.getAtendimento().getId().toString()));
      }

    }

  }
  
  public List<DocumentoDTO> buscarDocumentos(Atendimento atendimento, Date inicioDate, Date terminoDate){
	  
	  List<DocumentoDTO> documentoDTO = new ArrayList<>();
	  
	  List<PrescricaoMedica> prescricoes = prescricaoMedicaService.consultarPrescricoes(atendimento, inicioDate, terminoDate);
	  
	  
	  for (PrescricaoMedica prescricao : prescricoes){
		  
		  documentoDTO.add(new DocumentoDTO(prescricao));
	  }
	  
	  return documentoDTO;
  }
  
  public List<Map.Entry<String, List<DocumentoDTO>>> mapearDataDocumento(Atendimento atendimento, Date inicioDate, Date terminoDate){
	  
	  List<DocumentoDTO> documentos = buscarDocumentos(atendimento, inicioDate, terminoDate);
	  
	  Map<String, List<DocumentoDTO>> mapDocumentos = new HashMap<>();
	  
	  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	  
	  for ( DocumentoDTO documento : documentos){
		  
		  String data = sdf.format(documento.getData());
		  
		  List<DocumentoDTO> listaDoc = mapDocumentos.get(data);
		  
		  if (listaDoc == null){
			  listaDoc = new ArrayList<>();
			  
			  mapDocumentos.put(data, listaDoc);
		  }
		  
		  listaDoc.add(documento);
	  }
	  
	  List<Map.Entry<String, List<DocumentoDTO>>> mapearData = new ArrayList<>(mapDocumentos.entrySet());
	  
	  Collections.sort(mapearData, new Comparator<Map.Entry<String, List<DocumentoDTO>>>() {

		@Override
		public int compare(Map.Entry<String, List<DocumentoDTO>> o1, Map.Entry<String, List<DocumentoDTO>> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
		
	});
	  
	  return mapearData;
	
  }

}
