package br.com.vah.protocolo.service;


import br.com.vah.protocolo.constants.AcaoHistoricoEnum;
import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.constants.SetorNivelEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.*;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.reports.ReportLoader;
import br.com.vah.protocolo.reports.ReportTotalPorSetor;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

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
  AtendimentoSrv atendimentoSrv;

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

  private
  @Inject
  EvolucaoEnfermagemSrv evolucaoEnfermagemSrv;

  private
  @Inject
  CaixaEntradaSrv caixaSrv;

  private
  @Inject
  ValidadeSrv validadeSrv;

  private
  @Inject
  HDASrv hdaSrv;

  private
  @Inject
  ClassificacaoDeRiscoSrv classificacaoDeRiscoSrv;

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
    Collections.sort(list, (o1, o2) -> {
      int compareSetor = o1.getSetor().compareTo(o2.getSetor());
      if (compareSetor == 0) {
        return o2.getTotalCriadas().compareTo(o1.getTotalCriadas());
      }
      return compareSetor;
    });

    return reportLoader.imprimeRelatorio("medias", list);

  }

  public Protocolo addHistorico(Protocolo protocolo, User user, SetorProtocolo origem, SetorProtocolo destino, String descricao, AcaoHistoricoEnum acao) {
    if (user != null) {
      Historico historico = new Historico();
      historico.setAutor(user);
      historico.setOrigem(origem);
      historico.setDestino(destino);
      historico.setDescricao(descricao);
      historico.setAcao(acao);
      historico.setProtocolo(protocolo);
      if (protocolo.getHistorico() == null) {
        protocolo.setHistorico(new ArrayList<>());
      }
      protocolo.getHistorico().add(historico);
    }
    return protocolo;
  }

  public Protocolo addHistorico(Protocolo protocolo, User user, SetorProtocolo origem, SetorProtocolo destino, AcaoHistoricoEnum acao) {
    return addHistorico(protocolo, user, origem, destino, null, acao);
  }

  /**
   * Criteria default
   *
   * @param params
   * @return
   */
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
    Boolean visualizarRecebidos = (Boolean) params.getParams().get("visualizarRecebidos");
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
    criteria.add(Restrictions.ne("estado", EstadosProtocoloEnum.PRONTO_SOCORRO));
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
    if (!visualizarRecebidos) {
      criteria.add(Restrictions.ne("estado", EstadosProtocoloEnum.RECEBIDO));
    }
    return criteria;
  }

  public List<DocumentoDTO> gerarListaDTO(Protocolo protocolo, Boolean onlyDocs, Boolean selected) {
    List<DocumentoDTO> dtos = new ArrayList<>();
    if (protocolo.getItens() != null) {
      protocolo.getItens().forEach((item) -> {
        if (onlyDocs && item.getFilho() != null) {
          dtos.addAll(gerarListaDTO(initializeLists(item.getFilho()), true, selected));
        } else {
          dtos.add(new DocumentoDTO(item, selected));
        }
      });
    }
    return dtos;
  }


  public Boolean documentoIguais(DocumentoDTO dto, ItemProtocolo itemProtocolo) {
    Protocolo filho = itemProtocolo.getFilho();
    DocumentoProtocolo documento = itemProtocolo.getDocumento();
    DocumentoProtocolo dtoDoc = dto.getDocumento();
    Protocolo dtoFilho = dto.getFilho();
    Boolean igual = false;
    if (filho != null && dtoFilho != null) {
      if (filho.equals(dtoFilho)) {
        igual = true;
      }
    } else if (documento != null && dtoDoc != null) {
      if (documento.getCodigo().equals(dtoDoc.getCodigo()) && documento.getTipo().equals(dtoDoc.getTipo())) {
        igual = true;
      }
    }
    return igual;
  }

  public List<DocumentoDTO> removeIfExists(List<DocumentoDTO> dtos, ItemProtocolo itemProtocolo) {
    DocumentoDTO toRemove = null;
    for (DocumentoDTO dto : dtos) {
      if (documentoIguais(dto, itemProtocolo)) {
        toRemove = dto;
        break;
      }
    }

    if (toRemove != null) {
      dtos.remove(toRemove);
    }
    return dtos;
  }

  private Boolean jaProtocolado(DocumentoDTO dto) {
    Session session = getSession();
    Criteria criteria = session.createCriteria(DocumentoProtocolo.class);
    criteria.add(Restrictions.eq("codigo", dto.getDocumento().getCodigo()));
    criteria.add(Restrictions.eq("tipo", dto.getDocumento().getTipo()));
    if (criteria.list().size() > 0) {
      Criteria itemProtocoloCrit = getSession().createCriteria(ItemProtocolo.class);
      itemProtocoloCrit.add(Restrictions.eq("documento", criteria.list().iterator().next()));
      return itemProtocoloCrit.list().size() > 0;
    }
    return false;
  }

  private Boolean checkFolhaAnestesica(AvisoCirurgia aviso) {
    String sql = "SELECT DISTINCT 'X' " +
        "FROM DBAMV.PRESTADOR_AVISO PRESTADOR_AVISO, " +
        "      DBAMV.ATI_MED         ATI_MED, " +
        "      DBAMV.PRESTADOR       PRESTADOR, " +
        "      DBAMV.CIRURGIA_AVISO  CIRURGIA_AVISO " +
        "WHERE (PRESTADOR_AVISO.CD_PRESTADOR = PRESTADOR.CD_PRESTADOR AND " +
        "      PRESTADOR_AVISO.CD_ATI_MED = ATI_MED.CD_ATI_MED AND " +
        "      TP_FUNCAO = 'N' AND " +
        "      CIRURGIA_AVISO.CD_AVISO_CIRURGIA = PRESTADOR_AVISO.CD_AVISO_CIRURGIA AND " +
        "      CIRURGIA_AVISO.CD_CIRURGIA = PRESTADOR_AVISO.CD_CIRURGIA) AND " +
        "      CIRURGIA_AVISO.CD_AVISO_CIRURGIA = C.CD_AVISO_CIRURGIA AND " +
        "      CIRURGIA_AVISO.CD_AVISO_CIRURGIA = :CD_AVISO_CIRURGIA ";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter("CD_AVISO_CIRURGIA", aviso.getId());
    return query.list().size() > 0;
  }

  private Map<String, Object> buscarDocumentosMap(Protocolo protocolo, Date begin, Date end, Convenio convenio, String listaContas, String listaAtendimentos) {

    final List<DocumentoDTO> result = new ArrayList<>();
    Map<String, Object> resultMap = new HashMap<>();

    SetorNivelEnum nivelOrigem = protocolo.getOrigem().getNivel();

    if (SetorNivelEnum.SECRETARIA.equals(nivelOrigem) || SetorNivelEnum.PRONTO_SOCORRO.equals(nivelOrigem)) {

      final List<PrescricaoMedica> prescricoes = new ArrayList<>();
      final List<AvisoCirurgia> avisos = new ArrayList<>();
      final List<RegistroDocumento> registros = new ArrayList<>();
      final List<EvolucaoEnfermagem> evolucoes = new ArrayList<>();
      final List<HDA> hdas = new ArrayList<>();
      final List<ClassificacaoDeRisco> classificacoes = new ArrayList<>();
      final List<DocumentoProtocolo> documentosPs = new ArrayList<>();

      List<Object[]> datas = new ArrayList<>();

      Atendimento atendimento = atendimentoSrv.find(protocolo.getAtendimento().getId());

      if (SetorNivelEnum.SECRETARIA.equals(nivelOrigem)) {
        datas = validadeSrv.recuperarValidades(protocolo, begin, end);
      } else {
        Calendar beginCld = Calendar.getInstance();
        beginCld.setTime(atendimento.getDataAtendimento());
        Calendar endCld = Calendar.getInstance();
        endCld.setTime(atendimento.getDataAlta());
        if (beginCld.compareTo(endCld) == 0) {
          endCld.add(Calendar.DAY_OF_MONTH, 1);
        }
        Object[] range = new Object[4];
        range[0] = range[3] = beginCld.getTime();
        range[1] = endCld.getTime();
        datas.add(range);
      }

      if (datas.size() > 0) {
        resultMap.put("periodo", datas);
      }

      datas.forEach((range) -> {
        Date inicioValidade = (Date) range[0];
        Date fimValidade = (Date) range[1];
        Date referencia = (Date) range[3];
        prescricoes.addAll(prescricaoMedicaSrv.consultarPrescricoes(protocolo, inicioValidade, fimValidade, referencia));
        if (nivelOrigem.equals(SetorNivelEnum.SECRETARIA)) {
          avisos.addAll(avisoCirurgiaSrv.consultarAvisos(protocolo, inicioValidade, fimValidade, referencia));
        }
        registros.addAll(registroDocumentoSrv.consultarRegistros(protocolo, inicioValidade, fimValidade, referencia));
        evolucoes.addAll(evolucaoEnfermagemSrv.consultarEvolucoesEnfermagem(protocolo, inicioValidade, fimValidade, referencia));
        if (SetorNivelEnum.PRONTO_SOCORRO.equals(nivelOrigem) &&
            "U".equals(atendimento.getTipo()) &&
            atendimento.getDataAlta() != null) {
          hdas.addAll(hdaSrv.consultarHdas(protocolo, inicioValidade, fimValidade, referencia));
          classificacoes.addAll(classificacaoDeRiscoSrv.consultarClassificacao(protocolo, inicioValidade, fimValidade, referencia));
        }
      });

      if (!hdas.isEmpty()) {
        // documentosPs.addAll(hdaSrv.diagnosticoPs(hdas));
        documentosPs.addAll(classificacaoDeRiscoSrv.guiaSadt(hdas));
      }

      final List<DocumentoDTO> rawResult = new ArrayList<>();

      prescricoes.forEach((prescricao) -> {
        // Tem itens de prescrição?
        if (!prescricao.getItems().isEmpty()) {
          rawResult.add(new DocumentoDTO(prescricao, true));
        }

        if (prescricaoMedicaSrv.hasEvolucao(prescricao)) {
          rawResult.add(new DocumentoDTO(prescricao, false));
        }
      });

      avisos.forEach((a) -> {
        DocumentoDTO dtoAviso = new DocumentoDTO(a);
        rawResult.add(dtoAviso);
        if (checkFolhaAnestesica(a)) {
          rawResult.add(dtoAviso.criarFolhaAnestesica());
        }
      });
      registros.forEach((r) -> rawResult.add(new DocumentoDTO(r)));
      evolucoes.forEach((e) -> rawResult.add(new DocumentoDTO(e)));
      hdas.forEach((h) -> rawResult.add(new DocumentoDTO(h)));
      classificacoes.forEach((c) -> rawResult.add(new DocumentoDTO(c)));
      documentosPs.forEach((d) -> rawResult.add(new DocumentoDTO(d)));

      result.addAll(rawResult.stream().filter((dto) -> !jaProtocolado(dto)).collect(Collectors.toList()));

    }

    // Busque itens na caixa de entrada (documentos repassados, protocolos recebidos)

    List<CaixaEntrada> caixaEntrada = caixaSrv.getItensCaixaEntrada(protocolo, convenio, listaContas, listaAtendimentos);

    caixaEntrada.forEach((c) -> result.add(new DocumentoDTO(c)));

    List<DocumentoDTO> dtos = result;

    for (ItemProtocolo itemProtocolo : protocolo.getItens()) {
      dtos = removeIfExists(dtos, itemProtocolo);
    }

    resultMap.put("items", dtos);

    return resultMap;
  }

  public List<DocumentoDTO> buscarDocumentos(Protocolo protocolo, Convenio convenio, String listaContas, String listaAtendimentos) throws ProtocoloBusinessException {
    Date begin = protocolo.getInicio();
    Date end = protocolo.getFim();
    if (SetorNivelEnum.SECRETARIA.equals(protocolo.getOrigem().getNivel()) || SetorNivelEnum.PRONTO_SOCORRO.equals(protocolo.getOrigem().getNivel())) {
      if (begin == null) {
        return null;
      } else {
        Calendar beginCld = Calendar.getInstance();
        beginCld.setTime(begin);
        beginCld.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endCld = Calendar.getInstance();
        if (end == null) {
          endCld.setTime(begin);
          endCld.add(Calendar.DAY_OF_MONTH, 2);
        } else {
          if (begin.after(end)) {
            throw new ProtocoloBusinessException("Período de busca inválido.");
          }
          endCld.setTime(end);
        }
        Map<String, Object> resultado = buscarDocumentosMap(protocolo, beginCld.getTime(), endCld.getTime(), convenio, listaContas, listaAtendimentos);
        List<DocumentoDTO> documentos = (List<DocumentoDTO>) resultado.get("items");
        return documentos;
      }
    } else {
      Map<String, Object> resultado = buscarDocumentosMap(protocolo, null, null, convenio, listaContas, listaAtendimentos);
      List<DocumentoDTO> documentos = (List<DocumentoDTO>) resultado.get("items");
      return documentos;
    }
  }

  public Protocolo enviarProtocolo(final Protocolo protocolo, User user, List<CaixaEntrada> caixasVinculadas, List<CaixaEntrada> caixasRemovidas) throws ProtocoloBusinessException {

    SetorProtocolo origem = protocolo.getOrigem();
    SetorProtocolo destino = protocolo.getDestino();

    if (origem == null) {
      throw new ProtocoloBusinessException("Protocolo sem origem definido.");
    }

    if (destino == null) {
      throw new ProtocoloBusinessException("Protocolo sem destino definido.");
    }

    if (origem.equals(destino)) {
      throw new ProtocoloBusinessException("Destino e origem iguais.");
    }

    SetorNivelEnum nivelOrigem = origem.getNivel();
    SetorNivelEnum nivelDestino = destino.getNivel();

    // Quando a origem é Pronto Socorro

    if (SetorNivelEnum.PRONTO_SOCORRO.equals(nivelOrigem)) {
      if (!SetorNivelEnum.FATURAMENTO_CENTRAL.equals(nivelDestino) && !SetorNivelEnum.SAME.equals(nivelDestino)) {
        throw new ProtocoloBusinessException("Destino deve ser Faturamento Central ou SAME.");
      }
    }

    if (SetorNivelEnum.SECRETARIA.equals(nivelOrigem)) {
      if (protocolo.getContaFaturamento() == null) {
        throw new ProtocoloBusinessException("Por favor, indique a conta desta movimentação.");
      }
    }

    // Verifique se há documentos pendentes caso a origem do envio seja uma secretaria
    if (SetorNivelEnum.SECRETARIA.equals(nivelDestino)) {
      if (!SetorNivelEnum.SECRETARIA.equals(nivelOrigem) && !SetorNivelEnum.ENVIOS.equals(nivelDestino)) {
        throw new ProtocoloBusinessException("Movimentação para secretaria não permitida");
      }
      RegFaturamento conta = protocolo.getContaFaturamento();
      if (conta != null) {
        List<CaixaEntrada> candidatos = caixaSrv.busqueDocumentosNaoVinculados(protocolo, origem);
        final List<CaixaEntrada> seraoEnviados = new ArrayList<>();
        protocolo.getItens().forEach((item) -> {
          CaixaEntrada seraEnviado = item.getCaixa();
          if (seraEnviado != null && seraEnviado.getId() != null) {
            candidatos.forEach((candidato) -> {
              if (seraEnviado.getId().equals(candidato.getId())) {
                seraoEnviados.add(candidato);
              }
            });
          }
        });
        if (candidatos.size() > seraoEnviados.size()) {
          throw new ProtocoloBusinessException("O envio não foi realizado, pois existem documentos pendentes de inclusão.");
        }
      }
    }

    // ENVIO A PARTIR DO FATURAMENTO
    if (SetorNivelEnum.FATURAMENTO_CENTRAL.equals(nivelOrigem)) {
      // Se o destino for uma secretaria, verifica se há mais de uma conta referenciada no protocolo
      if (SetorNivelEnum.SECRETARIA.equals(nivelDestino)) {
        final Set<RegFaturamento> contaSet = new HashSet<>();
        protocolo.getItens().forEach((it) -> {
          Protocolo filho = it.getFilho();
          if (filho != null) {
            if (filho.getContaFaturamento() != null) {
              contaSet.add(filho.getContaFaturamento());
            }
          }
        });
        if (contaSet.size() > 1) {
          throw new ProtocoloBusinessException("Existe mais de uma conta referenciada neste protocolo.");
        } else if (contaSet.size() == 1) {
          protocolo.setContaFaturamento(contaSet.iterator().next());
        }
      }

      if (!SetorNivelEnum.SECRETARIA.equals(nivelDestino)) {
        // TODO: Alguma condição especial?
      }
    }

    protocolo.setDataEnvio(new Date());

    if (EstadosProtocoloEnum.RASCUNHO.equals(protocolo.getEstado()) || EstadosProtocoloEnum.RECUSADO.equals(protocolo.getEstado())) {
      return salvarProtocolo(protocolo, user, AcaoHistoricoEnum.MOVIMENTO, caixasVinculadas, caixasRemovidas);
    } else {
      throw new ProtocoloBusinessException("Apenas rascunhos, protocolos recusados ou novos protocolos podem ser enviados.");
    }
  }

  public Integer[] contarDocumentos(Protocolo protocolo) {
    Integer totalDocumentos = 0;
    Integer totalPrescricoes = 0;
    Integer totalEvolucoes = 0;
    Integer totalDescricoes = 0;
    Integer totalFolha = 0;
    Integer totalRegistros = 0;
    Integer totalDocumentosManuais = 0;
    Integer totalEvolucaoAnotacao = 0;
    Integer totalDocumentosProntuarios = 0;

    for (ItemProtocolo item : protocolo.getItens()) {
      DocumentoProtocolo documento = item.getDocumento();
      Protocolo filho = item.getFilho();
      if (documento != null) {
        switch (documento.getTipo()) {
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
          case EVOLUCAO_ENFERMAGEM:
            totalDocumentos++;
            totalEvolucaoAnotacao++;
            break;
          case DOCUMENTO_PRONTUARIO:
            totalDocumentos++;
            totalDocumentosProntuarios++;
            break;
          default:
            if (documento.getTipo().isDocManual()) {
              totalDocumentos++;
              totalDocumentosManuais++;
            }
            break;
        }
      } else if (filho != null && filho.getId() != null) {
        Integer[] resultFilho = contarDocumentos(initializeLists(filho));
        totalDocumentos += resultFilho[0];
        totalPrescricoes += resultFilho[1];
        totalEvolucoes += resultFilho[2];
        totalDescricoes += resultFilho[3];
        totalFolha += resultFilho[4];
        totalRegistros += resultFilho[5];
        totalDocumentosManuais += resultFilho[6];
        totalEvolucaoAnotacao += resultFilho[7];
        totalDocumentosProntuarios += resultFilho[8];
      }

    }
    return new Integer[]{
        totalDocumentos,
        totalPrescricoes,
        totalEvolucoes,
        totalFolha,
        totalDescricoes,
        totalRegistros,
        totalDocumentosManuais,
        totalEvolucaoAnotacao,
        totalDocumentosProntuarios
    };
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
    protocolo.setItens(att.getItens());
    protocolo.setHistorico(att.getHistorico());
    protocolo.setComentarios(att.getComentarios());
    return att;
  }

  public List<Historico> initializeHistorico(Protocolo protocolo) {
    Protocolo att = find(protocolo.getId());
    new LinkedHashSet<>(att.getHistorico());
    return att.getHistorico();
  }


  private void checkDates(Date date, Date[] range) {
    if (range[0] == null || range[0].compareTo(date) > 0) {
      range[0] = date;
    }
    if (range[1] == null || range[1].compareTo(date) < 0) {
      range[1] = date;
    }
  }

  public Set<RegFaturamento> inferirContasInitialize(Protocolo protocolo) {
    Protocolo att = initializeLists(protocolo);
    return inferirContas(att, null);
  }

  public Set<RegFaturamento> inferirContas(Protocolo att, List<DocumentoDTO> documentos) {
    Date[] range = new Date[2];
    Set<RegFaturamento> contas = new HashSet<>();
    att.getItens().forEach((itemProtocolo) -> {
      DocumentoProtocolo documento = itemProtocolo.getDocumento();
      Protocolo filho = itemProtocolo.getFilho();
      if (documento != null) {
        checkDates(documento.getDataHoraCriacao(), range);
      } else if (filho != null) {
        contas.addAll(inferirContasInitialize(filho));
      }
    });
    if (documentos != null) {
      documentos.forEach((dto) -> checkDates(dto.getDataHoraCriacao(), range));
    }
    if (range[0] != null && range[1] != null) {
      contas.addAll(regFaturamentoSrv.obterContas(att, range));
    }
    return contas;
  }

  public EstadosProtocoloEnum getEstadoPorAcao(AcaoHistoricoEnum acao) {
    switch (acao) {
      case CRIACAO:
        return EstadosProtocoloEnum.RASCUNHO;
      case MOVIMENTO:
        return EstadosProtocoloEnum.ENVIADO;
      case RECEBIMENTO:
        return EstadosProtocoloEnum.RECEBIDO;
      case CORRECAO:
        return EstadosProtocoloEnum.CORRIGIDO;
      case RECUSA:
        return EstadosProtocoloEnum.RECUSADO;
      case ARQUIVAMENTO:
        return EstadosProtocoloEnum.ARQUIVADO;
      default:
        return EstadosProtocoloEnum.RASCUNHO;
    }
  }

  public Protocolo salvarProtocolo(
      Protocolo protocolo,
      User user,
      AcaoHistoricoEnum acao,
      List<CaixaEntrada> caixasVinculadas,
      List<CaixaEntrada> caixasRemovidas) {

    SetorProtocolo origem = protocolo.getOrigem();
    SetorProtocolo destino = protocolo.getDestino();

    if (protocolo.getId() == null) {
      addHistorico(protocolo, user, origem, destino, AcaoHistoricoEnum.CRIACAO);
    }

    if (acao != null) {
      addHistorico(protocolo, user, origem, destino, acao);
      protocolo.setLocalizacao(origem.getTitle());
      if (!SetorNivelEnum.SECRETARIA.equals(origem.getNivel())) {
        protocolo.getItens().forEach((item) -> {
          Protocolo filho = item.getFilho();
          // Adiciona histório aos protocolos filhos
          if (filho != null) {
            if (filho.getId() == null) {
              addHistorico(filho, user, origem, destino, acao);
            } else {
              Protocolo att = initializeLists(filho);
              addHistorico(att, user, origem, destino, acao);
              update(att);
            }
          }
        });
      }
      protocolo.setEstado(getEstadoPorAcao(acao));
    }

    /**
     * MOVIMENTO DE PROTOCOLOS
     */

    if (AcaoHistoricoEnum.MOVIMENTO.equals(acao)) {
      protocolo.setEstado(EstadosProtocoloEnum.ENVIADO);
      protocolo.setDataResposta(new Date());
    }

    /**
     * RECEBIMENTO DE PROTOCOLOS
     */

    // No recebimento, cria itens na caixa de entrada.
    if (AcaoHistoricoEnum.RECEBIMENTO.equals(acao)) {
      protocolo.setLocalizacao(destino.getTitle());
      protocolo.setDataResposta(new Date());
      SetorNivelEnum nivelOrigem = protocolo.getOrigem().getNivel();
      SetorNivelEnum nivelDestino = protocolo.getDestino().getNivel();

      if (SetorNivelEnum.SECRETARIA.equals(nivelOrigem)) {
        if (!SetorNivelEnum.SECRETARIA.equals(nivelDestino)) {
          // se estiver enviando para um setor de um nível acima (exemplo: Faturamento, Envios), crie somente um item na caixa de entrada.
          caixaSrv.create(caixaSrv.criarCaixa(protocolo));
        } else {
          // Se estiver transferindo para outra secretaria, crie itens de caixa para cada documento.
          protocolo.getItens().forEach((filho) -> caixaSrv.create(caixaSrv.criarCaixa(protocolo, filho.getDocumento())));
        }

      } else {
        // Cria um item de caixa pra o protocolo recebido
        protocolo.getItens().forEach((item) -> {
          Protocolo filho = find(item.getFilho().getId());
          // Muda "indiretamente" a localização do protocolo filho.
          filho.setLocalizacao(destino.getTitle());
          update(filho);
          CaixaEntrada caixa = caixaSrv.criarCaixa(item.getFilho());
          // Altera a origem e o destino para coincidir com o protocolo que está sendo recebido.
          // Isto é necessário, pois o método "criarCaixa" utiliza origem/destino do protocolo passado como parâmetro.
          caixa.setOrigem(protocolo.getOrigem());
          caixa.setDestino(protocolo.getDestino());
          caixaSrv.create(caixa);
        });
      }
    }

    /**
     * Atualiza o estados dos itens da caixa de entrada
     */

    caixasVinculadas.forEach((caixa) -> {
      caixa.setVinculado(true);
      caixaSrv.update(caixa);
    });

    caixasRemovidas.forEach((caixa) -> {
      caixa.setVinculado(false);
      caixaSrv.update(caixa);
    });

    if (protocolo.getId() == null) {
      // Re attach documentos
      protocolo.getItens().forEach((it) -> {
        if (it.getDocumento() != null && it.getDocumento().getId() != null) {
          it.setDocumento(getEm().find(DocumentoProtocolo.class, it.getDocumento().getId()));
        }
      });
      return create(protocolo);
    } else {
      return update(protocolo);
    }
  }

}
