-- DDL SISTEMA NOVO PROTOCOLO



-- SETOR

CREATE TABLE USRDBVAH.TB_NPTC_SETOR
( CD_SETOR NUMBER(*) PRIMARY KEY NOT NULL,
  NM_SETOR VARCHAR2(100),
  CD_NIVEL NUMBER(1),
  CD_SETOR_MV NUMBER(*),
  CONSTRAINT FK_NPTC_SETOR_1 FOREIGN KEY (CD_SETOR_MV) REFERENCES DBAMV.SETOR (CD_SETOR)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_SETOR;

-- USUÁRIO

CREATE TABLE USRDBVAH.TB_NPTC_USUARIO
( ID NUMBER(*) PRIMARY KEY NOT NULL,
  DS_LOGIN VARCHAR2(75),
  NM_TITULO VARCHAR2(200),
  CD_ROLE VARCHAR2(25),
  CD_SETOR NUMBER(*),
  CONSTRAINT FK_NPTC_USUARIO_1 FOREIGN KEY (CD_SETOR) REFERENCES USRDBVAH.TB_NPTC_SETOR (CD_SETOR)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_USUARIO;

-- PROTOCOLO

CREATE TABLE USRDBVAH.TB_NPTC_PROTOCOLO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  CD_ATENDIMENTO NUMBER(*),
  CD_REG_FAT NUMBER(*),
  CD_STATUS NUMBER(1),
  CD_SETOR_ORIGEM NUMBER(*),
  CD_SETOR_DESTINO NUMBER(*),
  DT_ENVIO DATE,
  DT_RESPOSTA DATE,
  SN_REENVIADO VARCHAR2(1),
  SN_ARQUIVADO VARCHAR2(1),
  CONSTRAINT FK_NPTC_PROTOCOLO_1 FOREIGN KEY (CD_SETOR_ORIGEM) REFERENCES USRDBVAH.TB_NPTC_SETOR (CD_SETOR),
  CONSTRAINT FK_NPTC_PROTOCOLO_2 FOREIGN KEY (CD_SETOR_DESTINO) REFERENCES USRDBVAH.TB_NPTC_SETOR (CD_SETOR),
  CONSTRAINT FK_NPTC_PROTOCOLO_3 FOREIGN KEY (CD_ATENDIMENTO) REFERENCES DBAMV.TB_ATENDIME (CD_ATENDIMENTO),
  CONSTRAINT FK_NPTC_PROTOCOLO_4 FOREIGN KEY (CD_REG_FAT) REFERENCES DBAMV.REG_FAT (CD_REG_FAT)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_PROTOCOLO;

-- DOCUMENTO MANUAL

CREATE TABLE USRDBVAH.TB_NPTC_DOC_MANUAL
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  CD_CODIGO NUMBER(*),
  CD_TIPO NUMBER(2),
  NM_OBSERVACAO VARCHAR(50),
  CD_PRESTADOR NUMBER(*),
  DH_CRIACAO DATE,
  DH_IMPRESSAO DATE,
  CONSTRAINT FK_NPTC_DOC_MANUAL_1 FOREIGN KEY (CD_PRESTADOR) REFERENCES DBAMV.PRESTADOR (CD_PRESTADOR)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_DOC_MANUAL;


-- ITEM  PROTOCOLO

CREATE TABLE USRDBVAH.TB_NPTC_IT_PROTOCOLO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_PROTOCOLO NUMBER(*) NOT NULL,
  CD_PRE_MED NUMBER(*),
  CD_AVISO_CIRURGIA NUMBER(*),
  CD_REGISTRO_DOCUMENTO NUMBER(*),
  ID_DOC_MANUAL NUMBER(*),
  ID_PROTOCOLO_REENVIO NUMBER(*),
  CD_TIPO VARCHAR2(50),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_1 FOREIGN KEY (ID_PROTOCOLO) REFERENCES USRDBVAH.TB_NPTC_PROTOCOLO (ID),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_2 FOREIGN KEY (CD_PRE_MED) REFERENCES DBAMV.PRE_MED (CD_PRE_MED),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_3 FOREIGN KEY (CD_AVISO_CIRURGIA) REFERENCES DBAMV.TB_AVISO_CIRURGIA (CD_AVISO_CIRURGIA),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_4 FOREIGN KEY (CD_REGISTRO_DOCUMENTO) REFERENCES DBAMV.REGISTRO_DOCUMENTO (CD_REGISTRO_DOCUMENTO),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_5 FOREIGN KEY (ID_DOC_MANUAL) REFERENCES USRDBVAH.TB_NPTC_DOC_MANUAL (ID),
  CONSTRAINT FK_NPTC_IT_PROTOCOLO_6 FOREIGN KEY (ID_PROTOCOLO_REENVIO) REFERENCES USRDBVAH.TB_NPTC_PROTOCOLO (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_IT_PROTOCOLO;

-- HISTÓRICO

CREATE TABLE USRDBVAH.TB_NPTC_HISTORICO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_PROTOCOLO NUMBER(*),
  ID_USER NUMBER(*),
  DT_ALTERACAO DATE,
  CD_STATUS NUMBER(1),
  CONSTRAINT FK_NPTC_HISTORICO_1 FOREIGN KEY (ID_PROTOCOLO) REFERENCES USRDBVAH.TB_NPTC_PROTOCOLO (ID),
  CONSTRAINT FK_NPTC_HISTORICO_2 FOREIGN KEY (ID_USER) REFERENCES USRDBVAH.TB_NPTC_USUARIO (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_HISTORICO;

-- COMENTARIO

CREATE TABLE USRDBVAH.TB_NPTC_COMENTARIO
(
  ID NUMBER(*) PRIMARY KEY NOT NULL,
  ID_PROTOCOLO NUMBER(*),
  ID_USER NUMBER(*),
  DT_CRIACAO DATE,
  NM_COMENTARIO VARCHAR2(255),
  CONSTRAINT FK_NPTC_COMENTARIO_1 FOREIGN KEY (ID_PROTOCOLO) REFERENCES USRDBVAH.TB_NPTC_PROTOCOLO (ID),
  CONSTRAINT FK_NPTC_COMENTARIO_2 FOREIGN KEY (ID_USER) REFERENCES USRDBVAH.TB_NPTC_USUARIO (ID)
);

CREATE SEQUENCE USRDBVAH.SEQ_NPTC_COMENTARIO;

DROP TABLE USRDBVAH.TB_NPTC_COMENTARIO;
DROP TABLE USRDBVAH.TB_NPTC_HISTORICO;
DROP TABLE USRDBVAH.TB_NPTC_IT_PROTOCOLO;
DROP TABLE USRDBVAH.TB_NPTC_DOC_MANUAL;
DROP TABLE USRDBVAH.TB_NPTC_PROTOCOLO;
DROP TABLE USRDBVAH.TB_NPTC_USUARIO;
DROP TABLE USRDBVAH.TB_NPTC_SETOR;

DROP SEQUENCE USRDBVAH.SEQ_NPTC_COMENTARIO;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_HISTORICO;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_PROTOCOLO;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_DOC_MANUAL;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_IT_PROTOCOLO;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_USUARIO;
DROP SEQUENCE USRDBVAH.SEQ_NPTC_SETOR;

