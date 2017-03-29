package br.com.vah.protocolo.constants;

import br.com.vah.protocolo.entities.usrdbvah.Historico;
import br.com.vah.protocolo.entities.usrdbvah.SetorProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.User;

/**
 * Created by Jairoportela on 21/03/2017.
 */
public enum AcaoHistoricoEnum {
  CRIACAO("Criado por <b>%s</b>"),
  MOVIMENTO("Movimentado por <b>%s</b>: de <b>%s</b> para <b>%s</b>"),
  RECEBIMENTO("Recebido por <b>%s</b>"),
  RECUSA("Recusado por <b>%s</b>"),
  CORRECAO("Corrigido por <b>%s</b>"),
  ARQUIVAMENTO("Arquivado por <b>%s</b>");

  private String label;

  AcaoHistoricoEnum(String label) {
    this.label = label;
  }

  public String textoFormatado(Historico historico) {
    switch (this) {
      case MOVIMENTO:
        SetorProtocolo origem = historico.getOrigem();
        SetorProtocolo destino = historico.getDestino();
        if (origem != null && destino != null) {
          User autor = historico.getAutor();
          return String.format(label, autor.getTitle() == null ? autor.getLogin() : autor.getTitle(), origem.getTitle(), destino.getTitle());
        } else {
          return "";
        }
      default:
        User autor = historico.getAutor();
        return String.format(label, autor.getTitle() == null ? autor.getLogin() : autor.getTitle());

    }
  }

}
