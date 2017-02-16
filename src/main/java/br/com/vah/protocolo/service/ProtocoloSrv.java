package br.com.vah.protocolo.service;


import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.*;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.reports.ReportLoader;
import br.com.vah.protocolo.reports.ReportTotalPorSetor;
import br.com.vah.protocolo.util.DtoKeyMap;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class ProtocoloSrv extends AbstractSrv<Protocolo> {

  private
  @Inject
  ReportLoader reportLoader;

  private
  @Inject
  PrescricaoMedicaSrv prescricaoMedicaSrv;

  private
  @Inject
  AvisoCirurgiaSrv avisoCirurgiaSrv;

  private
  @Inject
  RegistroDocumentoSrv registroDocumentoSrv;

  private
  @Inject
  ItemProtocoloSrv itemProtocoloSrv;

  private
  @Inject
  RegFaturamentoSrv regFaturamentoSrv;

  public ProtocoloSrv() {
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

  public Protocolo addHistorico(Protocolo protocolo, User user, EstadosProtocoloEnum acao) {
    if (user != null) {
      Historico historico = new Historico();
      historico.setAutor(user);
      historico.setEstado(acao);
      historico.setProtocolo(protocolo);
      if (protocolo.getHistorico() == null) {
        protocolo.setHistorico(new ArrayList<Historico>());
      }
      protocolo.getHistorico().add(historico);
    }
    return protocolo;
  }

  public Protocolo salvarParcial(Protocolo protocolo, User user) {

    addHistorico(protocolo, user, protocolo.getEstado());

    return this.update(protocolo);
  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Protocolo.class);
    Long atendimentoId = (Long) params.getParams().get("atendimento");
    String pacienteParam = (String) params.getParams().get("paciente");
    SetorProtocolo setor = (SetorProtocolo) params.getParams().get("setor");
    EstadosProtocoloEnum[] estados = (EstadosProtocoloEnum[]) params.getParams().get("estados");
    Convenio[] convenios = (Convenio[]) params.getParams().get("convenios");
    Date[] dateRange = (Date[]) params.getParams().get("dateRange");
    Criteria atendimentoAlias = criteria.createAlias("atendimento", "a", JoinType.LEFT_OUTER_JOIN);
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

  private List<DocumentoDTO> gerarListaDTO(List<PrescricaoMedica> prescricoes,
                                           List<AvisoCirurgia> avisos,
                                           List<RegistroDocumento> registros,
                                           List<Protocolo> protocolos,
                                           List<RegFaturamento> contas) {
    List<DocumentoDTO> documentos = new ArrayList<>();

    // Prescrições e evoluções médicas
    if (prescricoes != null) {
      for (PrescricaoMedica prescricao : prescricoes) {
        if (!prescricao.getItems().isEmpty()) {
          documentos.add(new DocumentoDTO(prescricao, TipoDocumentoEnum.PRESCRICAO));
        }
        if (prescricaoMedicaSrv.hasEvolucao(prescricao)) {
          documentos.add(new DocumentoDTO(prescricao, TipoDocumentoEnum.EVOLUCAO));
        }
      }
    }

    // Avisos de cirurgia
    if (avisos != null) {
      for (AvisoCirurgia aviso : avisos) {
        documentos.add(new DocumentoDTO(aviso, TipoDocumentoEnum.DESCRICAO_CIRURGICA));
        documentos.add(new DocumentoDTO(aviso, TipoDocumentoEnum.FOLHA_ANESTESICA));
      }
    }

    // Registro de Documentos
    if (registros != null) {
      registros.forEach((registro) -> documentos.add(new DocumentoDTO(registro)));
    }

    // Protocolos
    if (protocolos != null) {
      protocolos.forEach((protocoloItem) -> documentos.add(new DocumentoDTO(protocoloItem)));
    }

    // Contas
    if (contas != null) {
      contas.forEach((conta) -> documentos.add(new DocumentoDTO(conta)));
    }

    return documentos;
  }

  private List<DocumentoDTO> gerarListaDTO(Protocolo protocolo) {
    List<DocumentoDTO> dtos = new ArrayList<>();
    if (protocolo.getItens() != null) {
      protocolo.getItens().forEach((item) -> dtos.add(new DocumentoDTO(item)));
    }
    return dtos;
  }

  private DtoKeyMap gerarDtoKeyMap(List<DocumentoDTO> documentos) {
    DtoKeyMap dtoKeyMap = new DtoKeyMap();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    if (documentos.isEmpty()) {
      dtoKeyMap.put("Sem documentos", null);
    }

    for (DocumentoDTO documento : documentos) {

      String key = sdf.format(documento.getData());


      List<DocumentoDTO> listaDoc = dtoKeyMap.get(key);

      if (listaDoc == null) {
        listaDoc = new ArrayList<>();
        dtoKeyMap.put(key, listaDoc);
      }

      listaDoc.add(documento);
    }

    return dtoKeyMap;
  }

  public List<DocumentoDTO> removeIfExists(List<DocumentoDTO> dtos, ItemProtocolo itemProtocolo) {
    DocumentoDTO toRemove = null;
    for (DocumentoDTO dto : dtos) {
      if (itemProtocolo.getTipo().equals(dto.getTipo())) {
        if (dto.getPrescricao() != null) {
          if (dto.getPrescricao().equals(itemProtocolo.getPrescricaoMedica())) {
            toRemove = dto;
            break;
          }
        } else if (dto.getAviso() != null) {
          if (dto.getAviso().equals(itemProtocolo.getAvisoCirurgia())) {
            toRemove = dto;
            break;
          }
        } else if (dto.getRegistro() != null) {
          if (dto.getRegistro().equals(itemProtocolo.getRegistroDocumento())) {
            toRemove = dto;
            break;
          }
        }
      }
    }

    if (toRemove != null) {
      dtos.remove(toRemove);
    }

    return dtos;

  }

  private List<Protocolo> protocolosAptosParaEnvio(Atendimento atendimento, Date inicio, Date fim, SetorProtocolo setor, Protocolo protocolo) {
    Criteria criteria = getSession().createCriteria(Protocolo.class, "protocolo");
    criteria.add(Restrictions.eq("atendimento", atendimento));
    criteria.add(Restrictions.eq("estado", EstadosProtocoloEnum.RECEBIDO));
    criteria.add(Restrictions.eq("destino", setor));

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("protocolo.id", "i.protocoloItem.id"))));

    return criteria.list();
  }

  private List<DocumentoDTO> buscarDocumentos(Protocolo protocolo, Date inicio, Date fim) {

    List<PrescricaoMedica> prescricoes = null;
    List<AvisoCirurgia> avisos = null;
    List<RegistroDocumento> registros = null;
    List<RegFaturamento> contas = null;

    Atendimento atendimento = protocolo.getAtendimento();
    SetorProtocolo origem = protocolo.getOrigem();


    if (protocolo.getOrigem().getNivel() == 0) {
      prescricoes = prescricaoMedicaSrv.consultarPrescricoes(atendimento, inicio, fim);
      avisos = avisoCirurgiaSrv.consultarAvisos(atendimento, inicio, fim, origem);
      registros = registroDocumentoSrv.consultarRegistros(atendimento, inicio, fim);
    } else {
      contas = inferirContas(protocolo);
    }

    List<Protocolo> protocolos = protocolosAptosParaEnvio(atendimento, inicio, fim, origem, protocolo);

    List<DocumentoDTO> dtos = gerarListaDTO(prescricoes, avisos, registros, protocolos, contas);

    for (ItemProtocolo itemProtocolo : protocolo.getItens()) {
      dtos = removeIfExists(dtos, itemProtocolo);
    }

    return dtos;

  }

  public DtoKeyMap buscarDocumentosNaoSelecionados(Protocolo protocolo, Date inicio, Date fim) {
    List<DocumentoDTO> documentos = buscarDocumentos(protocolo, inicio, fim);
    return gerarDtoKeyMap(documentos);
  }

  public DtoKeyMap gerarDocumentosSelecionados(Protocolo protocolo) {
    return gerarDtoKeyMap(gerarListaDTO(protocolo));
  }

  public Protocolo enviarProtocolo(Protocolo protocolo) throws ProtocoloBusinessException {

    if (protocolo.getDestino() == null) {
      throw new ProtocoloBusinessException("Protocolo sem origem definido.");
    }

    if (protocolo.getDestino() == null) {
      throw new ProtocoloBusinessException("Protocolo sem destino definido.");
    }

    if (EstadosProtocoloEnum.RASCUNHO.equals(protocolo.getEstado())) {
      protocolo.setEstado(EstadosProtocoloEnum.ENVIADO);
      protocolo.setDataEnvio(new Date());
      protocolo = update(protocolo);
    } else {
      throw new ProtocoloBusinessException("Apenas rascunhos ou novos protocolos podem ser enviados.");
    }

    return protocolo;
  }

  public Integer[] contarDocumentos(Protocolo protocolo) {
    Integer totalDocumentos = 0;
    Integer totalPrescricoes = 0;
    Integer totalEvolucoes = 0;
    Integer totalDescricoes = 0;
    Integer totalFolha = 0;
    Integer totalRegistros = 0;
    Integer totalDocumentosManuais = 0;

    for (ItemProtocolo item : protocolo.getItens()) {
      switch (item.getTipo()) {
        case EVOLUCAO:
          totalDocumentos++;
          totalEvolucoes++;
          break;
        case DESCRICAO_CIRURGICA:
          totalDocumentos++;
          totalDescricoes++;
          break;
        case FOLHA_ANESTESICA:
          totalDocumentos++;
          totalFolha++;
          break;
        case PRESCRICAO:
          totalDocumentos++;
          totalPrescricoes++;
          break;
        case REGISTRO_DOCUMENTO:
          totalDocumentos++;
          totalRegistros++;
          break;
        case DOCUMENTO_MANUAL:
          totalDocumentos++;
          totalDocumentosManuais++;
          break;
        case PROTOCOLO:
          Integer[] resultFilho = contarDocumentos(initializeLists(item.getProtocoloItem()));
          totalDocumentos += resultFilho[0];
          totalPrescricoes += resultFilho[1];
          totalEvolucoes += resultFilho[2];
          totalDescricoes += resultFilho[3];
          totalFolha += resultFilho[4];
          totalRegistros += resultFilho[5];
          totalDocumentosManuais += resultFilho[6];
          break;
        default:
          break;
      }
    }
    return new Integer[]{totalDocumentos, totalPrescricoes, totalEvolucoes, totalFolha, totalDescricoes, totalRegistros, totalDocumentosManuais};
  }

  public Protocolo buscarDadosRascunho(Protocolo protocolo) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Protocolo.class);
    criteria.add(Restrictions.eq("origem", protocolo.getOrigem()));
    criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
    criteria.add(Restrictions.eq("estado", EstadosProtocoloEnum.RASCUNHO));
    criteria.setFetchMode("atendimento", FetchMode.SELECT);
    criteria.setFetchMode("itens", FetchMode.SELECT);
    criteria.setFetchMode("historico", FetchMode.SELECT);
    criteria.setFetchMode("comentarios", FetchMode.SELECT);
    List<Protocolo> protocolos = criteria.list();
    return protocolos.isEmpty() ? null : protocolos.get(0);
  }

  public Protocolo initializeLists(Protocolo protocolo) {
    Protocolo att = find(protocolo.getId());
    new LinkedHashSet<>(att.getItens());
    new LinkedHashSet<>(att.getHistorico());
    new LinkedHashSet<>(att.getComentarios());
    return att;
  }

  private void checkDates(Date date, Date[] range) {
    if (range[0] == null || range[0].compareTo(date) > 0) {
      range[0] = date;
    }
    if (range[1] == null || range[1].compareTo(date) < 0) {
      range[1] = date;
    }
  }

  public List<RegFaturamento> inferirContas(Protocolo protocolo) {
    Protocolo att = initializeLists(protocolo);
    Date[] range = new Date[2];
    att.getItens().forEach((itemProtocolo) -> {
      // Check Aviso
      if (itemProtocolo.getAvisoCirurgia() != null) {
        Date dtAviso = itemProtocolo.getAvisoCirurgia().getInicioCirurgia();
        checkDates(dtAviso, range);
      }
      // Check Prescrição
      if (itemProtocolo.getPrescricaoMedica() != null) {
        Date dtPrescricao = itemProtocolo.getPrescricaoMedica().getDataHoraImpressao();
        checkDates(dtPrescricao, range);
      }
      // Check Registro
      if (itemProtocolo.getRegistroDocumento() != null) {
        Date dtRegistro = itemProtocolo.getRegistroDocumento().getDataRegistro();
        checkDates(dtRegistro, range);
      }
      // Check Doc. Manual
      if (itemProtocolo.getDocumentoManual() != null) {
        Date dtManual = itemProtocolo.getDocumentoManual().getDataImpressao();
        checkDates(dtManual, range);
      }
    });
    return regFaturamentoSrv.obterContas(att, range);
  }

}
