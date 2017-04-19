package br.com.vah.protocolo.dto;

import br.com.vah.protocolo.constants.TipoDocumentoEnum;
import br.com.vah.protocolo.entities.dbamv.*;
import br.com.vah.protocolo.entities.usrdbvah.CaixaEntrada;
import br.com.vah.protocolo.entities.usrdbvah.DocumentoProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.ItemProtocolo;
import br.com.vah.protocolo.entities.usrdbvah.Protocolo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentoDTO {

  private Protocolo protocolo;
  private TipoDocumentoEnum tipo;
  private String descricao;
  private Long codigo;
  private String conselho;
  private String prestador;
  private String consPrestConv;
  private Date dataReferencia;
  private Date dataHoraCriacao;
  private Date dataHoraImpressao;
  private DocumentoProtocolo documento;
  private Protocolo filho;
  private ItemProtocolo itemProtocolo;
  private CaixaEntrada caixa;

  private Boolean selected = false;

  public DocumentoDTO() {
  }

  public void createDocumento() {
    this.documento = new DocumentoProtocolo();
    this.documento.setCodigo(codigo);
    this.documento.setTipo(tipo);
    this.documento.setDescricao(descricao);
    this.documento.setConselho(conselho);
    this.documento.setPrestador(prestador);
    this.documento.setDataReferencia(dataReferencia);
    this.documento.setDataHoraCriacao(dataHoraCriacao);
    this.documento.setDataHoraImpressao(dataHoraImpressao);
  }

  public DocumentoDTO(PrescricaoMedica prescricao, Boolean isPresc) {
    this.codigo = prescricao.getId();
    this.tipo = isPresc ? TipoDocumentoEnum.PRESCRICAO : TipoDocumentoEnum.EVOLUCAO;
    this.descricao = this.tipo.getLabel();
    this.dataReferencia = prescricao.getDataReferencia();
    Prestador prestador = prescricao.getPrestador();
    if (prestador == null) {
      this.conselho = "N/A";
      this.prestador = "N/A";
    } else {
      this.conselho = prestador.getConselho().getNome();
      this.prestador = prestador.getNome();
    }
    this.dataHoraCriacao = prescricao.getDataHoraCriacao();
    this.dataHoraImpressao = prescricao.getDataHoraImpressao();
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    createDocumento();
  }

  public DocumentoDTO(AvisoCirurgia aviso) {
    this.codigo = aviso.getId();
    this.tipo = TipoDocumentoEnum.DESCRICAO_CIRURGICA;
    this.descricao = aviso.getSituacao();
    this.conselho = "N/A";
    this.prestador = "N/A";
    this.dataReferencia = aviso.getDataReferencia();
    this.dataHoraCriacao = aviso.getInicioCirurgia();
    this.dataHoraImpressao = aviso.getInicioCirurgia();
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    createDocumento();
  }

  public DocumentoDTO criarFolhaAnestesica() {
    DocumentoDTO dto = new DocumentoDTO();
    dto.codigo = codigo;
    dto.tipo = TipoDocumentoEnum.FOLHA_ANESTESICA;
    dto.descricao = descricao;
    dto.conselho = "N/A";
    dto.prestador = "N/A";
    dto.dataReferencia = dataReferencia;
    dto.dataHoraCriacao = dataHoraCriacao;
    dto.dataHoraImpressao = dataHoraImpressao;
    dto.createDocumento();
    return dto;
  }

  /* DOCUMENTOS PARA O PROTOCOLO PRONTO SOCORRO*/
  public DocumentoDTO(HDA hda) {
    this.codigo = hda.getId();
    this.tipo = TipoDocumentoEnum.HDA;
    this.descricao = "";
    this.conselho = hda.getPrestador().getConselho().getNome();
    this.prestador = hda.getPrestador().getNome();
    this.dataReferencia = hda.getDataInicio();
    this.dataHoraCriacao = hda.getDataInicio();
    this.dataHoraImpressao = hda.getDataInicio();
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    createDocumento();
  }

  public DocumentoDTO(ClassificacaoDeRisco classificacao) {
    this.codigo = classificacao.getId();
    this.tipo = TipoDocumentoEnum.CLASSIFICACAO_DE_RISCO;
    this.descricao = "";
    this.conselho = "";
    this.prestador = classificacao.getUsuario();
    this.dataReferencia = classificacao.getDataPreAtendimento();
    this.dataHoraCriacao = classificacao.getDataPreAtendimento();
    this.dataHoraImpressao = classificacao.getDataPreAtendimentoFim();
    this.consPrestConv = String.format("%s", this.prestador);
    createDocumento();
  }

  public DocumentoDTO(DocumentoProtocolo docPs) {
    this.codigo = docPs.getCodigo();
    this.tipo = docPs.getTipo();
    this.descricao = docPs.getDescricao();
    this.conselho = "";
    this.prestador = "";
    this.consPrestConv = String.format("%s", this.descricao);
    createDocumento();
  }

  /* FIM DOCUMENTOS PARA O PRONTO SOCORRO */
  public DocumentoDTO(RegistroDocumento registro) {
    this.codigo = registro.getId();
    this.tipo = TipoDocumentoEnum.REGISTRO_DOCUMENTO;
    this.descricao = registro.getDocumento().getDescricao();
    this.conselho = registro.getNomeUsuario();
    this.prestador = "N/A";
    this.dataReferencia = registro.getDataReferencia();
    this.dataHoraCriacao = registro.getDataRegistro();
    this.dataHoraImpressao = registro.getDataRegistro();
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    createDocumento();
  }

  public DocumentoDTO(EvolucaoEnfermagem evolucao) {
    this.codigo = evolucao.getId();
    this.tipo = TipoDocumentoEnum.EVOLUCAO_ENFERMAGEM;
    this.descricao = tipo.getLabel();
    Prestador prestador = evolucao.getPrestador();
    if (prestador == null) {
      this.prestador = "N/A";
      this.conselho = "N/A";
    } else {
      this.conselho = prestador.getConselho().getNome();
      this.prestador = prestador.getNome();
    }
    this.dataReferencia = evolucao.getDataReferencia();
    this.dataHoraCriacao = evolucao.getHora();
    this.dataHoraImpressao = evolucao.getDataImpressao();
    this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    createDocumento();
  }

  public void setFields(DocumentoProtocolo documento) {
    this.documento = documento;
    this.codigo = documento.getCodigo();
    this.tipo = documento.getTipo();
    this.descricao = documento.getDescricao();
    if (this.descricao == null) {
      this.descricao = documento.getTipo().getLabel();
    }
    this.conselho = documento.getConselho();
    this.prestador = documento.getPrestador();
    this.dataReferencia = documento.getDataReferencia();
    this.dataHoraCriacao = documento.getDataHoraCriacao();
    this.dataHoraImpressao = documento.getDataHoraImpressao();
    if (this.conselho != null && this.prestador != null) {
      this.consPrestConv = String.format("%s - %s", this.conselho, this.prestador);
    } else {
      this.consPrestConv = "N/A";
    }

  }

  public void setFields(Protocolo filho) {
    this.filho = filho;
    this.codigo = filho.getId();
    this.tipo = TipoDocumentoEnum.PROTOCOLO;
    RegFaturamento conta = filho.getContaFaturamento();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
    this.descricao = conta == null ? "Remessa" : String.format("%d - (%s à %s)", conta.getId(), sdf.format(conta.getInicio()), sdf.format(conta.getFim()));
    this.conselho = "-";
    this.prestador = "-";
    this.dataHoraCriacao = filho.getDataResposta();
    this.dataHoraImpressao = filho.getDataEnvio();
    Atendimento atendimento = filho.getAtendimento();
    if (atendimento != null) {
      this.consPrestConv = String.format("%s - %d - %s", atendimento.getConvenio().getTitle(), atendimento.getId(), atendimento.getPaciente().getName());
    } else {
      this.consPrestConv = "Remessa";
    }
  }

  public void setFields(CaixaEntrada caixa) {
    Protocolo protocolo = caixa.getProtocolo();
    if (protocolo != null) {
      this.setFields(caixa.getProtocolo());
    } else {
      this.setFields(caixa.getDocumento());
    }
    this.setCaixa(caixa);
  }

  public DocumentoDTO(CaixaEntrada caixa) {
    setFields(caixa);
  }

  public DocumentoDTO(ItemProtocolo item, Boolean selected) {
    this.itemProtocolo = item;
    if (item.getDocumento() != null) {
      setFields(item.getDocumento());
    } else if (item.getFilho() != null) {
      setFields(item.getFilho());
    }
    this.caixa = item.getCaixa();
    this.protocolo = item.getProtocolo();
    this.selected = selected;
  }

  public ItemProtocolo getItemProtocolo() {
    return this.itemProtocolo;
  }

  public ItemProtocolo criarItemProtocoloSeNecessario(Protocolo protocolo) {
    if (itemProtocolo == null) {
      ItemProtocolo newItem = new ItemProtocolo();
      newItem.setProtocolo(protocolo);
      newItem.setDocumento(documento);
      newItem.setFilho(filho);
      newItem.setCaixa(caixa);
      this.protocolo = protocolo;
      itemProtocolo = newItem;
    }
    return itemProtocolo;
  }

  public Protocolo getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(Protocolo protocolo) {
    this.protocolo = protocolo;
  }

  public Long getCodigo() {
    return codigo;
  }

  public void setCodigo(Long codigo) {
    this.codigo = codigo;
  }

  public String getPrestador() {
    return prestador;
  }

  public void setPrestador(String prestador) {
    this.prestador = prestador;
  }

  public Date getDataReferencia() {
    return dataReferencia;
  }

  public void setDataReferencia(Date dataReferencia) {
    this.dataReferencia = dataReferencia;
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

  public String getConselho() {
    return conselho;
  }

  public void setConselho(String conselho) {
    this.conselho = conselho;
  }

  public String getConsPrestConv() {
    return consPrestConv;
  }

  public void setConsPrestConv(String consPrestConv) {
    this.consPrestConv = consPrestConv;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public DocumentoProtocolo getDocumento() {
    return documento;
  }

  public void setDocumento(DocumentoProtocolo documento) {
    this.documento = documento;
  }

  public Protocolo getFilho() {
    return filho;
  }

  public void setFilho(Protocolo filho) {
    this.filho = filho;
  }

  public void setItemProtocolo(ItemProtocolo itemProtocolo) {
    this.itemProtocolo = itemProtocolo;
  }

  public CaixaEntrada getCaixa() {
    return caixa;
  }

  public void setCaixa(CaixaEntrada caixa) {
    this.caixa = caixa;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public Integer getSelectedInteger() {
    return selected ? 1 : 0;
  }

  public String getRowKey() {
    if (codigo != null && tipo != null) {
      return String.format("%d%s", codigo, tipo);
    }
    return "";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DocumentoDTO) {
      DocumentoDTO dto = (DocumentoDTO) obj;
      if (dto.getTipo() != null && dto.getCodigo() != null) {
        return tipo.equals(dto.getTipo()) && codigo.equals(dto.getCodigo());
      }
    }
    return false;
  }

}
