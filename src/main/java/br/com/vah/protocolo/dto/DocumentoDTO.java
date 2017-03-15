package br.com.vah.protocolo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

import br.com.vah.protocolo.constants.EstadosProtocoloEnum;
import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.*;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoManual;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

public class DocumentoDTO {

  private Protocolo protocolo;
  private TipoDocumentoEnum tipo;
  private PrescricaoMedica prescricao;
  private AvisoCirurgia aviso;
  private RegistroDocumento registro;
  private EvolucaoEnfermagem evolucao;
  private DocumentoManual docManual;
  private String descricao;
  private Date data;
  private String codigo;
  private String prestador;
  private Date dataHoraCriacao;
  private Date dataHoraImpressao;
  private String conselho;
  private ItemProtocolo itemProtocolo;
  private Boolean selected = false;
  private Protocolo protocoloItem;

  public DocumentoDTO(PrescricaoMedica prescricao, TipoDocumentoEnum tipo) {
    setFieldsForPrescricao(prescricao, tipo);
  }

  private void setFieldsForPrescricao(PrescricaoMedica prescricao, TipoDocumentoEnum tipo) {
    this.data = prescricao.getDatePreMed();
    this.prescricao = prescricao;
    this.codigo = String.valueOf(prescricao.getId());
    this.prestador = prescricao.getPrestador().getNome();
    this.dataHoraCriacao = prescricao.getDataHoraCriacao();
    this.dataHoraImpressao = prescricao.getDataHoraImpressao();
    this.tipo = tipo;
    if (TipoDocumentoEnum.EVOLUCAO.equals(tipo)) {
      this.descricao = "Evolução";
    } else {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
      this.descricao = String.format("Prescrição - de %s à %s", sdf.format(prescricao.getDataHoraCriacao()), sdf.format(prescricao.getDataValidade()));
    }
    // Atributos inferidos
    resolverConselho();
    this.prestador = this.conselho + " - " + this.prestador;
  }

  public DocumentoDTO(AvisoCirurgia aviso, TipoDocumentoEnum tipo) {
    setFieldsForAvisoCirurgia(aviso, tipo);
  }

  private void setFieldsForAvisoCirurgia(AvisoCirurgia aviso, TipoDocumentoEnum tipo) {
    this.data = aviso.getInicioCirurgia();
    this.aviso = aviso;
    this.codigo = String.valueOf(aviso.getId());
    this.tipo = tipo;
  }

  public DocumentoDTO(RegistroDocumento registro) {
    setFieldsForRegistroDocumento(registro);
  }

  private void setFieldsForRegistroDocumento(RegistroDocumento registro) {
    this.data = registro.getDataRegistro();
    this.registro = registro;
    this.dataHoraCriacao = registro.getDataRegistro();
    this.codigo = String.valueOf(registro.getId());
    this.descricao = registro.getDocumento().getDescricao();
    this.tipo = TipoDocumentoEnum.REGISTRO_DOCUMENTO;
    this.prestador = registro.getNomeUsuario();
  }

  public DocumentoDTO(EvolucaoEnfermagem evolucao) {
    setFieldsForEvolucaoEnfermagem(evolucao);
  }

  private void setFieldsForEvolucaoEnfermagem(EvolucaoEnfermagem evolucao) {
    this.data = evolucao.getData();
    this.evolucao = evolucao;
    this.dataHoraCriacao = evolucao.getHora();
    this.dataHoraImpressao = evolucao.getDataImpressao();
    this.codigo = evolucao.getId().toString();
    this.descricao = "Evolução/Anotação";
    this.tipo = TipoDocumentoEnum.EVOLUCAO_ENFERMAGEM;
    this.prestador = String.format("COREN - %s", evolucao.getPrestador().getNome());
  }

  public DocumentoDTO(Protocolo protocolo) {
    setFieldsProtocoloFilho(protocolo);
  }

  private void setFieldsProtocoloFilho(Protocolo protocoloItem) {
    this.data = protocoloItem.getDataEnvio();
    this.protocoloItem = protocoloItem;
    if (protocoloItem.getContaFaturamento() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      RegFaturamento conta = protocoloItem.getContaFaturamento();
      String from = sdf.format(conta.getInicio());
      String to = conta.getFim() == null ? "_" : sdf.format(conta.getFim());
      this.descricao = String.format("Conta: %d (de %s à %s)", conta.getId(), from, to);
    } else {
      try {
        int countDesc = 0;
        StringJoiner str = new StringJoiner(", ");
        for (ItemProtocolo item : protocoloItem.getItens()) {
          if (TipoDocumentoEnum.PROTOCOLO.equals(item.getTipo())) {
            Protocolo prt = item.getProtocoloItem();
            if (prt != null) {
              if (prt.getContaFaturamento() != null) {
                str.add(prt.getContaFaturamento().getId().toString());
                if (countDesc++ > 3) {
                  break;
                }
              }
            }
          }
        }
        this.descricao = str.toString();
      } catch (Exception e) {
        this.descricao =  "-";
      }
    }
    if (protocoloItem.getAtendimento() != null) {
      Atendimento atd = protocoloItem.getAtendimento();
      this.prestador = String.format("%s - %d - %s", atd.getConvenio().getTitle(), atd.getId(), atd.getPaciente().getName());;
    }
    this.dataHoraCriacao = protocoloItem.getDataEnvio();
    this.codigo = String.valueOf(protocoloItem.getId());
    this.tipo = TipoDocumentoEnum.PROTOCOLO;
  }

  private void setFieldsDocManual(DocumentoManual docManual) {
    this.data = docManual.getDataCriacao();
    this.docManual = docManual;
    this.codigo = String.format("%d", docManual.getCodigo());
    this.descricao = docManual.getTipo().getLabel();
    this.prestador = docManual.getObservacao();
    this.tipo = TipoDocumentoEnum.DOCUMENTO_MANUAL;
    this.dataHoraCriacao = docManual.getDataCriacao();
    this.dataHoraImpressao = docManual.getDataImpressao();
  }

  public DocumentoDTO(ItemProtocolo itemProtocolo) {
    this.itemProtocolo = itemProtocolo;
    switch (itemProtocolo.getTipo()) {
      case PRESCRICAO:
      case EVOLUCAO:
        setFieldsForPrescricao(itemProtocolo.getPrescricaoMedica(), itemProtocolo.getTipo());
        break;
      case DESCRICAO_CIRURGICA:
      case FOLHA_ANESTESICA:
        setFieldsForAvisoCirurgia(itemProtocolo.getAvisoCirurgia(), itemProtocolo.getTipo());
        break;
      case REGISTRO_DOCUMENTO:
        setFieldsForRegistroDocumento(itemProtocolo.getRegistroDocumento());
        break;
      case PROTOCOLO:
        setFieldsProtocoloFilho(itemProtocolo.getProtocoloItem());
        break;
      case DOCUMENTO_MANUAL:
        setFieldsDocManual(itemProtocolo.getDocumentoManual());
        break;
      case EVOLUCAO_ENFERMAGEM:
        setFieldsForEvolucaoEnfermagem(itemProtocolo.getEvolucaoEnfermagem());
        break;
      default:
        break;
    }
  }

  public ItemProtocolo getItemProtocolo() {
    return this.itemProtocolo;
  }

  public ItemProtocolo criarItemProtocolo() {
    ItemProtocolo itemProtocolo = new ItemProtocolo();
    itemProtocolo.setProtocolo(protocolo);
    itemProtocolo.setTipo(tipo);
    if (prescricao != null) {
      itemProtocolo.setPrescricaoMedica(prescricao);
    } else if (aviso != null) {
      itemProtocolo.setAvisoCirurgia(aviso);
    } else if (registro != null) {
      itemProtocolo.setRegistroDocumento(registro);
    } else if (protocoloItem != null) {
      itemProtocolo.setProtocoloItem(protocoloItem);
    } else if (evolucao != null) {
      itemProtocolo.setEvolucaoEnfermagem(evolucao);
    }
    return itemProtocolo;
  }

  private void resolverConselho() {
    if (prescricao.getTipoPrescricao().equals("E")) {
      conselho = "COREN";
    } else {
      conselho = "CRM";
    }
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getPrestador() {
    return prestador;
  }

  public void setPrestador(String prestador) {
    this.prestador = prestador;
  }

  public RegistroDocumento getRegistro() {
    return registro;
  }

  public void setRegistro(RegistroDocumento registro) {
    this.registro = registro;
  }

  public Date getDataHoraCriacao() {
    return dataHoraCriacao;
  }

  public void setDataHoraCriacao(Date dataHoraCriacao) {
    this.dataHoraCriacao = dataHoraCriacao;
  }

  public Date getDataHoraImpressao() {
    return dataHoraImpressao;
  }

  public void setDataHoraImpressao(Date dataHoraImpressao) {
    this.dataHoraImpressao = dataHoraImpressao;
  }

  public TipoDocumentoEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoDocumentoEnum tipo) {
    this.tipo = tipo;
  }

  public PrescricaoMedica getPrescricao() {
    return prescricao;
  }

  public void setPrescricao(PrescricaoMedica prescricao) {
    this.prescricao = prescricao;
  }

  public AvisoCirurgia getAviso() {
    return aviso;
  }

  public void setAviso(AvisoCirurgia aviso) {
    this.aviso = aviso;
  }

  public EvolucaoEnfermagem getEvolucao() {
    return evolucao;
  }

  public void setEvolucao(EvolucaoEnfermagem evolucao) {
    this.evolucao = evolucao;
  }

  public Protocolo getProtocoloItem() {
    return protocoloItem;
  }

  public void setProtocoloItem(Protocolo protocoloItem) {
    this.protocoloItem = protocoloItem;
  }

  public String getConselho() {
    return conselho;
  }

  public void setConselho(String conselho) {
    this.conselho = conselho;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
}
