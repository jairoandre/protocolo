package br.com.vah.protocolo.service;

import br.com.vah.protocolo.constants.AcaoHistoricoEnum;
import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.dto.DocumentoDTO;
import br.com.vah.protocolo.dto.SameDTO;
import br.com.vah.protocolo.entities.dbamv.Convenio;
import br.com.vah.protocolo.entities.usrdbvah.*;
import br.com.vah.protocolo.util.ViewUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.swing.text.View;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Jairoportela on 05/04/2017.
 */
@Stateless
public class SameSrv implements Serializable {

    private @Inject CaixaSrv caixaSrv;

    private @Inject CaixaEntradaSrv caixaEntradaSrv;

    private @Inject EnvelopeSrv envelopeSrv;

    private @Inject ArmarioSrv armarioSrv;

    private @Inject ProtocoloSrv protocoloSrv;

    public void arquivar(List<DocumentoDTO> dtos, Map<String, Object> params) {

        Integer tipo = (Integer) params.get("tipo");
        Long id = (Long) params.get("id");
        User user = (User) params.get("user");
        SetorProtocolo origem = (SetorProtocolo) params.get("origem");

        // Arquivamento em armário
        if (tipo.equals(0)) {
            Armario armario = armarioSrv.find(id);
            String linha = (String) params.get("linha");
            String coluna = (String) params.get("coluna");
            String nomeSala = ViewUtils.truncString(armario.getSala().getTitulo(), 40);
            String nomeArmario = ViewUtils.truncString(armario.getTitulo(), 40);

            String descricao = String.format("%s;%s;%s;%s", nomeSala, nomeArmario, linha, coluna);

            dtos.forEach((dto) -> {
                Protocolo protocolo = dto.getFilho();
                ItemArmario itemArmario = new ItemArmario();
                itemArmario.setProtocolo(protocolo);
                itemArmario.setArmario(armario);
                itemArmario.setLinha(linha);
                itemArmario.setColuna(coluna);
                armario.getItens().add(itemArmario);
                // Seta a vinculação
                CaixaEntrada caixaEntrada = dto.getCaixa();
                caixaEntrada.setVinculado(true);

                caixaEntradaSrv.update(caixaEntrada);

                Protocolo attProt = protocoloSrv.find(protocolo.getId());
                protocoloSrv.addHistorico(attProt, user, origem, null, descricao, AcaoHistoricoEnum.ARQUIVAMENTO);
                attProt.setLocalizacao(descricao);
                attProt.setEstado(EstadosProtocoloEnum.ARQUIVADO);

                protocoloSrv.update(attProt);
            });

            armarioSrv.update(armario);

        } else {
            Envelope envelope = envelopeSrv.find(id);

            String nomeCaixa = ViewUtils.truncString(envelope.getCaixa().getTitulo(), 30);
            String nomeEnvelope = ViewUtils.truncString(envelope.getTitulo(), 30);
            Sala sala = envelope.getCaixa().getSala();
            String nomeSala = ViewUtils.truncString(sala.getTitulo(), 30);

            StringJoiner joiner = new StringJoiner(";");
            joiner.add(nomeSala);
            joiner.add(nomeCaixa);
            joiner.add(nomeEnvelope);

            String descricao = joiner.toString();

            dtos.forEach((dto) -> {
                Protocolo protocolo = dto.getFilho();
                envelope.getItens().add(protocolo);
                // Seta a vinculação
                CaixaEntrada caixaEntrada = dto.getCaixa();
                caixaEntrada.setVinculado(true);

                caixaEntradaSrv.update(caixaEntrada);

                Protocolo attProt = protocoloSrv.find(protocolo.getId());
                protocoloSrv.addHistorico(attProt, user, origem, null, descricao, AcaoHistoricoEnum.ARQUIVAMENTO);
                attProt.setLocalizacao(descricao);
                attProt.setEstado(EstadosProtocoloEnum.ARQUIVADO);

                protocoloSrv.update(attProt);
            });

            envelopeSrv.update(envelope);

        }
    }

    public List<SameDTO> buscarArquivos(Long atendimento, String contas, Convenio convenio) {
        return null;
    }
}
