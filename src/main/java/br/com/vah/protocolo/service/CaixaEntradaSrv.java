package br.com.vah.protocolo.service;

import br.com.vah.protocolo.constants.CaixaEntradaFieldEnum;
import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.dbamv.Setor;
import br.com.vah.protocolo.entities.usrdbvah.CaixaEntrada;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jairoportela on 20/03/2017.
 */
@Stateless
public class CaixaEntradaSrv extends AbstractSrv<CaixaEntrada> {

  public CaixaEntradaSrv() {
    super(CaixaEntrada.class);
  }

  public CaixaEntrada criarCaixa(Protocolo protocolo) {
    CaixaEntrada caixa = new CaixaEntrada();
    caixa.setProtocolo(protocolo);
    if (protocolo.getAtendimento() != null) {
      caixa.setAtendimento(protocolo.getAtendimento());
    }
    if (protocolo.getContaFaturamento() != null) {
      caixa.setContaFaturamento(protocolo.getContaFaturamento());
    }
    caixa.setOrigem(protocolo.getOrigem());
    caixa.setDestino(protocolo.getDestino());
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    RegFaturamento conta = protocolo.getContaFaturamento();
    if (conta != null) {
      caixa.setDescricao(String.format("%d (de %s à %s)", conta.getId(), sdf.format(conta.getInicio()), sdf.format(conta.getFim())));
    } else {
      caixa.setDescricao("N/A");
    }
    caixa.setCriacao(new Date());
    return caixa;
  }

  public CaixaEntrada criarCaixa(Protocolo protocolo, DocumentoProtocolo documento) {
    CaixaEntrada caixa = new CaixaEntrada();
    Atendimento atendimento = protocolo.getAtendimento();
    if (atendimento != null) {
      caixa.setAtendimento(atendimento);
      caixa.setConvenio(atendimento.getConvenio());
    }
    if (protocolo.getContaFaturamento() != null) {
      caixa.setContaFaturamento(protocolo.getContaFaturamento());
    }
    caixa.setDocumento(documento);
    caixa.setOrigem(protocolo.getOrigem());
    caixa.setDestino(protocolo.getDestino());
    caixa.setDescricao(documento.getDescricao());
    caixa.setCriacao(documento.getDataHoraCriacao());
    return caixa;
  }

  public List<CaixaEntrada> busqueDocumentosNaoVinculados(Atendimento atendimento, SetorProtocolo setor) {
    Criteria crit = getSession().createCriteria(CaixaEntrada.class);
    crit.add(Restrictions.eq("atendimento", atendimento.getId()));
    crit.add(Restrictions.eq("destino", setor));
    crit.add(Restrictions.eq("vinculado", false));
    return crit.list();
  }

  public List<CaixaEntrada> buscarDocumentosPendentes(Map<String, Object> params) {
    Criteria crit = getSession().createCriteria(CaixaEntrada.class);
    SetorProtocolo same = new SetorProtocolo();
    same.setId(11l); // Hardcoded ID do SAME
    crit.add(Restrictions.eq("destino", same));
    crit.add(Restrictions.eq("vinculado", false));
    return crit.list();
  }


  public List<CaixaEntrada> getItensCaixaEntrada(Protocolo protocolo, Convenio convenio, String listaContas) {
    Criteria criteria = getSession().createCriteria(CaixaEntrada.class);
    Atendimento atendimento = protocolo.getAtendimento();
    criteria.add(Restrictions.eq("destino", protocolo.getOrigem()));
    criteria.add(Restrictions.eq("vinculado", false));

    if (atendimento != null) {
      criteria.add(Restrictions.eq("atendimento", atendimento));
    }


    if (convenio != null) {
      criteria.add(Restrictions.eq("convenio", convenio));
    }

    List<RegFaturamento> ids = new ArrayList<>();

    if (listaContas != null && !listaContas.isEmpty()) {
      String[] contas = listaContas.split(";");

      for (int i = 0, len = contas.length; i < len; i++) {
        try {
          Long l = Long.parseLong(contas[i]);
          ids.add(new RegFaturamento(l));
        } catch (NumberFormatException nfe) {
          System.out.println("Erro de conversão silenciado.");
        }
      }
      criteria.add(Restrictions.in("contaFaturamento", ids));
    }

    return criteria.list();

  }

  @Override
  public Criteria createCriteria(PaginatedSearchParam params) {
    Criteria criteria = getSession().createCriteria(CaixaEntrada.class);
    Map<String, Object> mapParams = params.getParams();
    List<Long> contas = (List<Long>) mapParams.get(CaixaEntradaFieldEnum.CONTAS.name());
    List<Long> convenios = (List<Long>) mapParams.get(CaixaEntradaFieldEnum.CONVENIOS.name());
    SetorProtocolo setor = (SetorProtocolo) mapParams.get(CaixaEntradaFieldEnum.SETOR.name());
    Boolean vinculados = (Boolean) mapParams.get(CaixaEntradaFieldEnum.VINCULADOS.name());

    if (contas != null && !contas.isEmpty()) {
      criteria.add(Restrictions.in("contaFaturamento", contas));
    }

    if (convenios != null && !convenios.isEmpty()) {
      criteria.add(Restrictions.in("convenio", convenios));
    }

    if (setor != null) {
      criteria.add(Restrictions.eq("destino", setor));
    }

    if (vinculados == null) {
      criteria.add(Restrictions.eq("vinculado", false));
    } else {
      criteria.add(Restrictions.eq("vinculado", vinculados));
    }

    return criteria;
  }
}
