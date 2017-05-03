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
import br.com.vah.protocolo.entities.dbamv.ClassificacaoDeRisco;
import br.com.vah.protocolo.entities.dbamv.HDA;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

public class ClassificacaoDeRiscoSrv extends AbstractSrv<ClassificacaoDeRisco> {

	public ClassificacaoDeRiscoSrv() {
		super(ClassificacaoDeRisco.class);
	}

	public List<ClassificacaoDeRisco> consultarClassificacao(Protocolo protocolo, Date inicio, Date fim, Date dataReferencia) {

		Session session = getEm().unwrap(Session.class);

		Criteria criteria = session.createCriteria(ClassificacaoDeRisco.class, "class");

		criteria.add(Restrictions.eq("atendimento", protocolo.getAtendimento()));

		DetachedCriteria dt = DetachedCriteria.forClass(DocumentoProtocolo.class, "dp");
		dt.setProjection(Projections.id());
		dt.add(Restrictions.eqProperty("class.id", "dp.codigo"));
		dt.add(Restrictions.eq("dp.tipo", TipoDocumentoEnum.CLASSIFICACAO_DE_RISCO));
		criteria.add(Subqueries.notExists(dt));

		List<ClassificacaoDeRisco> classficacao = criteria.list();

		classficacao.forEach((classi) -> classi.setDataPreAtendimento(dataReferencia));

		return classficacao;
	}
	
public List<DocumentoProtocolo> guiaSadt(List<HDA> hdas){
		
		List<DocumentoProtocolo> docs = new ArrayList<>();
		
		Long[] conv = {222l, 93l, 94l, 101l, 192l, 198l, 92l, 85l, 132l, 83l, 194l, 226l, 227l, 133l,
				223l, 116l, 218l, 117l, 220l, 110l, 111l, 103l, 228l, 40l, 8l, 96l, 108l};
		
		Boolean possuiSADT = Boolean.TRUE;
		
		DocumentoProtocolo guiaSADT = null;
		
		if (!hdas.isEmpty() && hdas.size() > 0){

      HDA hda = hdas.get(0);
			
			/*GUIA SADT*/
			for (int i = 0; i < conv.length; i++){
				if (hda.getAtendimento().getConvenio().getId() == conv[i]){
					possuiSADT = Boolean.FALSE;
					break;
				}
			}
			
			if (possuiSADT){
				
				guiaSADT = new DocumentoProtocolo();
				guiaSADT.setCodigo(hda.getId()); // Mesmo id do HDA
				guiaSADT.setAtendimento(hda.getAtendimento().getId());
				guiaSADT.setDescricao(TipoDocumentoEnum.GUIA_SADT.getLabel());
				guiaSADT.setTipo(TipoDocumentoEnum.GUIA_SADT);
        guiaSADT.setDataHoraCriacao(hda.getDataInicio());
        guiaSADT.setDataReferencia(hda.getDataInicio());
        guiaSADT.setDataHoraImpressao(hda.getDataInicio());
        docs.add(guiaSADT);
			}
		}
		
		return docs;
	}
}
