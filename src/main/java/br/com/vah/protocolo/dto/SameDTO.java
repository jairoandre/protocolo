package br.com.vah.protocolo.dto;

import br.com.vah.protocolo.entities.dbamv.Atendimento;
import br.com.vah.protocolo.entities.dbamv.RegFaturamento;
import br.com.vah.protocolo.entities.usrdbvah.*;

import java.io.Serializable;

/**
 * Created by Jairoportela on 06/04/2017.
 */
public class SameDTO implements Serializable {

    private Armario armario;
    private ItemArmario itemArmario;
    private Envelope envelope;
    private Protocolo protocolo;
    private Boolean selected = false;

    public String getDescricao() {
        RegFaturamento conta = protocolo.getContaFaturamento();
        Atendimento atendimento = protocolo.getAtendimento();
        return String.format("%d - %d - %s", conta == null ? 0 : conta.getId(),
                atendimento.getId(), atendimento.getPaciente().getName());
    }

    public String getLocalizacao() {
        if (armario == null) {
            if (envelope == null) {
                return "N/A";
            } else {
                Caixa caixa = envelope.getCaixa();
                return String.format("%s - %s", caixa.getTitulo(), envelope.getTitulo());
            }
        } else {
            return String.format("%s - Linha %s; Coluna %s", armario.getTitulo(), itemArmario.getLinha(), itemArmario.getColuna());
        }
    }

    public Armario getArmario() {
        return armario;
    }

    public void setArmario(Armario armario) {
        this.armario = armario;
    }

    public ItemArmario getItemArmario() {
        return itemArmario;
    }

    public void setItemArmario(ItemArmario itemArmario) {
        this.itemArmario = itemArmario;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public Integer getSelectedInteger() {
        return selected ? 1 : 0;
    }

    public String getRowKey() {
        if (protocolo == null) {
            return "";
        } else {
            return String.format("%s", protocolo.getId());
        }
    }
}
