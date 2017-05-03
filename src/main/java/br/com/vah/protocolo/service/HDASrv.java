package br.com.vah.protocolo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.HDA;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

public class HDASrv extends AbstractSrv<HDA> {
	
	public HDASrv(){
		super(HDA.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<HDA> consultarHdas(Protocolo protocolo, Date inicio, Date fim, Date dataReferencia){
		
		Session session = getEm().unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(HDA.class, "hda");
		
		criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));
		
		criteria.add(Restrictions.isNotNull("dataInicio"));
		
		
		DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
	    dt.setProjection(Projections.id());
	    dt.add(Restrictions.eqProperty("hda.id", "dp.codigo"));
	    dt.add(Restrictions.eq("dp.tipo", TipoDocumentoEnum.HDA));
	    criteria.add(Subqueries.notExists(dt));
	    
	    List<HDA> hdaList = criteria.list();
	    
	    hdaList.forEach((hda) -> hda.setDataInicio(dataReferencia));
		
		return hdaList;
	}
	
	public List<DocumentoProtocolo> diagnosticoPs(List<HDA> hdas){
		List<DocumentoProtocolo> docs = new ArrayList<>();
		DocumentoProtocolo diagnosticoAtendimento = null;
		
		if (!hdas.isEmpty() && hdas.size() > 0){
			
			
			/*DIAGNOSTICO DO ATENDIMENTO - ALTA PACIENTE*/
			diagnosticoAtendimento = new DocumentoProtocolo();
			diagnosticoAtendimento.setCodigo(hdas.get(0).getId()); // Mesmo id do HDA
			diagnosticoAtendimento.setAtendimento(hdas.get(0).getAtendimento().getId());
			diagnosticoAtendimento.setConselho(hdas.get(0).getPrestador().getConselho().getNome());
			diagnosticoAtendimento.setPrestador(hdas.get(0).getPrestador().getNome());
			diagnosticoAtendimento.setDataReferencia(hdas.get(0).getAtendimento().getDataAlta());
			diagnosticoAtendimento.setDataHoraCriacao(hdas.get(0).getAtendimento().getDataAlta());
			diagnosticoAtendimento.setDataHoraImpressao(hdas.get(0).getAtendimento().getDataAlta());
			diagnosticoAtendimento.setDescricao(TipoDocumentoEnum.DIAGNOSTICO_DO_ATENDIMENTO.getLabel());
			diagnosticoAtendimento.setTipo(TipoDocumentoEnum.DIAGNOSTICO_DO_ATENDIMENTO);
			
			docs.add(diagnosticoAtendimento);
			
		}
		return docs;
			
	}
	
}
