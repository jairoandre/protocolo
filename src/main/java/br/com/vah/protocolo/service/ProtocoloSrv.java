package br.com.vah.protocolo.service;


import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.entities.dbamv.*;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.exceptions.ProtocoloBusinessException;
import br.com.vah.protocolo.reports.ReportLoader;
import br.com.vah.protocolo.reports.ReportTotalPorSetor;
import br.com.vah.protocolo.util.DateUtility;
import br.com.vah.protocolo.util.DtoKeyMap;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
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

  public Protocolo addHistorico(Protocolo protocolo, User user, EstadosProtocoloEnum acao) {
    if (user != null) {
      Historico historico = new Historico();
      historico.setAutor(user);
      historico.setEstado(acao);
      historico.setProtocolo(protocolo);
      if (protocolo.getHistorico() == null) {
        protocolo.setHistorico(new ArrayList<>());
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

  private List<DocumentoDTO> gerarListaDTO(Protocolo protocolo, Boolean onlyDocs) {
    List<DocumentoDTO> dtos = new ArrayList<>();
    if (protocolo.getItens() != null) {
      protocolo.getItens().forEach((item) -> {
        if (onlyDocs && item.getFilho() != null) {
          dtos.addAll(gerarListaDTO(initializeLists(item.getFilho()), true));
        } else {
          dtos.add(new DocumentoDTO(item));
        }
      });
    }
    return dtos;
  }

  private DtoKeyMap gerarDtoKeyMap(Protocolo protocolo, List<DocumentoDTO> documentos) {
    DtoKeyMap dtoKeyMap = new DtoKeyMap();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    if (documentos.isEmpty()) {
      dtoKeyMap.put("Sem documentos", null);
    }

    for (DocumentoDTO documento : documentos) {

      Date keyData = documento.getDataHoraCriacao();

      if (false && !TipoDocumentoEnum.PRESCRICAO.equals(documento.getTipo())) {
        ConfiguracaoSetor config = getConfiguracaoSetor(protocolo);

        if (config != null) {
          Calendar configHour = Calendar.getInstance();
          configHour.setTime(config.getHoraInicioPreMed());
          Integer hourConfig = configHour.get(Calendar.HOUR_OF_DAY);
          Calendar keyDataCld = Calendar.getInstance();
          keyDataCld.setTime(keyData);
          Integer hourKeyData = keyDataCld.get(Calendar.HOUR_OF_DAY);
          if (hourKeyData < hourConfig) {
            keyDataCld.add(Calendar.DAY_OF_MONTH, -1);
          }
          keyData = keyDataCld.getTime();
        }
      }

      String key = sdf.format(keyData);

      List<DocumentoDTO> listaDoc = dtoKeyMap.get(key);

      if (listaDoc == null) {
        listaDoc = new ArrayList<>();
        dtoKeyMap.put(key, listaDoc);
      }

      listaDoc.add(documento);
    }

    return dtoKeyMap;
  }

  public ConfiguracaoSetor getConfiguracaoSetor(Protocolo protocolo) {
    if (protocolo.getOrigem().getSetorMV() != null) {
      Criteria criteria = getSession().createCriteria(ConfiguracaoSetor.class);
      criteria.add(Restrictions.eq("id", protocolo.getOrigem().getSetorMV().getId()));
      List<ConfiguracaoSetor> configs = criteria.list();
      if (!configs.isEmpty()) {
        return configs.iterator().next();
      }
    }
    return null;
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

  private List<Protocolo> protocolosAptosParaEnvio(Protocolo protocolo, Convenio convenio, String listaContas) {
    Criteria criteria = getSession().createCriteria(Protocolo.class, "pro");
    if (protocolo.getAtendimento() != null) {
      criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
    }
    criteria.add(Restrictions.eq("estado", EstadosProtocoloEnum.RECEBIDO));
    criteria.add(Restrictions.eq("destino", protocolo.getOrigem()));
    criteria.add(Restrictions.eq("reenviado", false));


    if (convenio != null && protocolo.getOrigem().getNivel() < 2) {
      criteria.createCriteria("atendimento", "atd").add(Restrictions.eq("atd.convenio", convenio));
    }

    List<Long> ids = new ArrayList<>();

    if (listaContas != null && !listaContas.isEmpty()) {
      String[] contas = listaContas.split(";");

      for (int i = 0, len = contas.length; i < len; i++) {
        try {
          Long l = Long.parseLong(contas[i]);
          ids.add(l);
        } catch (NumberFormatException nfe) {
          System.out.println("Erro de conversão silenciado.");
        }
      }
      Disjunction disj = Restrictions.disjunction();
      disj.add(Restrictions.in("contaFaturamento.id", ids));
      disj.add(Restrictions.isNull("contaFaturamento"));
      criteria.add(disj);
    }

    // Remove itens já protocolados.
    DetachedCriteria dt = DetachedCriteria.forClass(ItemProtocolo.class, "i");
    criteria.add(Subqueries.notExists(dt.setProjection(Projections.id()).add(Restrictions.eqProperty("pro.id", "i.filho.id"))));

    List<Protocolo> rawResult = criteria.list();

    if (protocolo.getOrigem().getNivel() == 2) {
      final List<Protocolo> contas = new ArrayList<>();
      rawResult.forEach((prot) ->
        prot.getItens().forEach((it) -> {
          Protocolo filho = it.getFilho();
          Boolean checkIds = ids.isEmpty() || filho.getContaFaturamento() == null || (ids.contains(filho.getContaFaturamento().getId()));
          Boolean checkConvenio = convenio == null || filho.getAtendimento() == null || filho.getAtendimento().getConvenio().equals(convenio);
          if (checkIds && checkConvenio) {
            contas.add(it.getFilho());
          }
        })
      );
      return contas;
    }

    return rawResult;
  }

  public void addIfNoExists(Protocolo protocolo, DocumentoDTO dto) {
    ItemProtocolo newItem = dto.criarItemProtocolo();
    newItem.setProtocolo(protocolo);
    Boolean add = true;
    for (ItemProtocolo item : protocolo.getItens()) {
      if (documentoIguais(dto, item)) {
        add = false;
        break;
      }
    }
    if (add) {
      protocolo.getItens().add(newItem);
    }
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

  private List<DocumentoDTO> buscarDocumentosView(Protocolo protocolo, Date begin, Date end, Convenio convenio, String listaContas) {

    final List<DocumentoDTO> result = new ArrayList<>();

    if (protocolo.getOrigem().getNivel() == 0) {
      String sql = "SELECT " +
          "VW.COD_ITEM_PRONTUARIO, " +
          "VW.TIPO_ITEM_PRONTUARIO, " +
          "VW.NOME_DOCUMENTO, " +
          "VW.CD_CONSELHO_PROF, " +
          "VW.MN_PROFISSIONAL, " +
          "VW.DH_CRIACAO, " +
          "VW.DH_IMPRESSAO " +
          "FROM DBAMV.VDIC_ATEND_PRONT_PRESC_ATU VW " +
          "LEFT JOIN DBAMV.UNID_INT U ON U.CD_UNID_INT = VW.CD_UNID_INT " +
          "WHERE CD_ATENDIMENTO = :CD_ATENDIMENTO AND " +
          "U.CD_SETOR = :CD_SETOR " +
          ((begin != null && end != null) ? "AND VW.DT_REFERENCIA BETWEEN :DT_BEGIN AND :DT_END" : "");

      Session session = getSession();
      SQLQuery query = session.createSQLQuery(sql);
      query.setParameter("CD_ATENDIMENTO", protocolo.getAtendimento().getId());
      query.setParameter("CD_SETOR", protocolo.getOrigem().getSetorMV().getId());
      if (begin != null && end != null) {
        query.setParameter("DT_BEGIN", begin);
        query.setParameter("DT_END", end);
      }
      List<Object[]> rows = query.list();

      final List<DocumentoDTO> rawResult = new ArrayList<>();

      rows.forEach((row) -> rawResult.add(new DocumentoDTO(row)));

      result.addAll(rawResult.stream().filter((dto) -> !jaProtocolado(dto)).collect(Collectors.toList()));

    }

    List<Protocolo> protocolos = protocolosAptosParaEnvio(protocolo, convenio, listaContas);

    protocolos.forEach((p) -> result.add(new DocumentoDTO(p)));

    List<DocumentoDTO> dtos = result;

    for (ItemProtocolo itemProtocolo : protocolo.getItens()) {
      dtos = removeIfExists(dtos, itemProtocolo);
    }

    return dtos;
  }

  public DtoKeyMap buscarDocumentosNaoSelecionados(Protocolo protocolo, Date inicio, Date fim, Convenio convenio, String listaContas) {
    //fim = DateUtility.lastHour(fim);
    List<DocumentoDTO> documentos = buscarDocumentosView(protocolo, inicio, fim, convenio, listaContas);
    return gerarDtoKeyMap(protocolo, documentos);
  }

  public DtoKeyMap gerarDocumentosSelecionados(Protocolo protocolo, Boolean onlyDocs) {
    return gerarDtoKeyMap(protocolo, gerarListaDTO(protocolo, onlyDocs));
  }

  public Protocolo enviarProtocolo(final Protocolo protocolo) throws ProtocoloBusinessException {

    if (protocolo.getOrigem() == null) {
      throw new ProtocoloBusinessException("Protocolo sem origem definido.");
    }

    if (protocolo.getDestino() == null) {
      throw new ProtocoloBusinessException("Protocolo sem destino definido.");
    }

    if (protocolo.getOrigem().equals(protocolo.getDestino())) {
      throw new ProtocoloBusinessException("Destino e origem iguais.");
    }

    if (protocolo.getOrigem().getNivel() == 0) {
      if (protocolo.getContaFaturamento() == null) {
        throw new ProtocoloBusinessException("Por favor, indique a conta desta movimentação.");
      }
    }

    // ENVIO DO FATURAMENTO
    if (protocolo.getOrigem().getNivel() == 1) {

      if (protocolo.getDestino().getNivel() == 0) {
        final Set<RegFaturamento> contaSet = new HashSet<>();
        protocolo.getItens().forEach((it) -> {
          Protocolo filho = it.getFilho();
          if (filho != null) {
            if (filho.getContaFaturamento() != null) {
              contaSet.add(filho.getContaFaturamento());
            }
            // Seta o pai do envio
            filho.setPai(protocolo);
          }
        });
        if (contaSet.size() > 1) {
          throw new ProtocoloBusinessException("Existe mais de uma conta referenciada neste protocolo.");
        } else if (contaSet.size() == 1) {
          protocolo.setContaFaturamento(contaSet.iterator().next());
        }
      }

      if (protocolo.getDestino().getNivel() == 2) {
        // TODO: Alguma condição especial?
      }
    }

    // Envio a partir do "ENVIOS" - Verifica a necessidade de setar flag que indica que todos os documentos de um protocolo foram enviados.
    if (protocolo.getOrigem().getNivel() == 2) {
      Map<Protocolo, Integer> countMap = new HashMap<>();
      Set<ItemProtocolo> itens = protocolo.getItens();
      if (itens == null && itens.isEmpty() && protocolo.getId() != null) {
        Protocolo attProtocolo = find(protocolo.getId());
        itens = attProtocolo.getItens();
        Set<Protocolo> pais = new HashSet<>();
        itens.forEach((filho) -> {
          Protocolo protocoloFilho = filho.getFilho();
          if (protocoloFilho != null && protocoloFilho.getPai() != null) {
            Protocolo pai = protocoloFilho.getPai();
            if (!pais.contains(pai)) {
              pais.add(pai);
              pai.setReenviado(false);
              update(pai);
            }
          }
        });
      }
      // Itens incluídos
      for (ItemProtocolo item : protocolo.getItens()) {
        Protocolo filho = item.getFilho();
        if (filho != null && filho.getPai() != null) {
          Protocolo pai = filho.getPai();
          Integer count = countMap.get(pai);
          if (count == null) {
            count = 0;
          }
          countMap.put(pai, count + 1);
        }
      }
      // Se todos os protocolos filhos foram enviados, marque o protocolo pai como "reenviado" para não ser mostrado novamente
      // na busca de documentos
      countMap.forEach((pai, count) -> {
        pai.setReenviado(pai.getItens().size() == count);
        // Atualize  o pai
        update(pai);
      });
    }

    if (EstadosProtocoloEnum.RASCUNHO.equals(protocolo.getEstado()) || EstadosProtocoloEnum.RECUSADO.equals(protocolo.getEstado())) {
      protocolo.setEstado(EstadosProtocoloEnum.ENVIADO);
      protocolo.setDataEnvio(new Date());
      return update(protocolo);
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
          case DOC_MANUAL_EVOLUCAO:
          case DOC_MANUAL_PRESCRICAO:
          case DOC_MANUAL_EVOLUCAO_ANOTACAO:
          case DOC_MANUAL_DESCRICAO_CIRURGICA:
          case DOC_MANUAL_FOLHA_ANESTESICA:
          case DOC_MANUAL_HDA:
          case DOC_MANUAL_EVOLUCAO_ENFERMAGEM:
          case DOC_MANUAL_EXAME_FISICO_EVOLUCAO_ENFERMAGEM:
          case DOC_MANUAL_REGISTRO_SINAIS_VITAIS_PS:
          case DOC_MANUAL_CLASSIFICACAO_RISCO:
          case DOC_MANUAL_FOLHA_CLASSIFICACAO:
          case DOC_MANUAL_SADT:
          case DOC_MANUAL_AUTORIZACAO_PS:
            totalDocumentos++;
            totalDocumentosManuais++;
            break;
          default:
            break;
        }
      } else if (filho != null) {
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

  public Set<RegFaturamento> inferirContasInitialize(Protocolo protocolo) {
    Protocolo att = initializeLists(protocolo);
    return inferirContas(att);
  }

  public Set<RegFaturamento> inferirContas(Protocolo att) {
    Date[] range = new Date[2];
    Set<RegFaturamento> contas = new HashSet<>();
    att.getItens().forEach((itemProtocolo) -> {
      DocumentoProtocolo documento = itemProtocolo.getDocumento();
      Protocolo filho = itemProtocolo.getFilho();
      if (documento != null) {
        checkDates(documento.getDataHoraImpressao(), range);
      } else if (filho != null) {
        contas.addAll(inferirContasInitialize(filho));
      }
    });
    contas.addAll(regFaturamentoSrv.obterContas(att, range));
    return contas;
  }

}
