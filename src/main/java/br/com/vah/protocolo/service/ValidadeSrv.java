package br.com.vah.protocolo.service;

import br.com.vah.protocolo.entities.usrdbvah.Protocolo;
import org.hibernate.SQLQuery;

import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jairoportela on 21/03/2017.
 */
@Stateless
public class ValidadeSrv extends AbstractSrv<Protocolo> {

  public ValidadeSrv() {
    super(Protocolo.class);
  }

  public List<Object[]> recuperarValidades(Protocolo protocolo, Date begin, Date end) {
    String sql =
        "SELECT " +
            "  CASE WHEN TAB_01.CD_PRE_MED = T.CD_PRE_MED " +
            "    THEN " +
            "      LEAST(TO_DATE((TO_CHAR(ATD.DT_ATENDIMENTO, 'DD/MM/YYYY') || ' ' || TO_CHAR(ATD.HR_ATENDIMENTO, 'HH24:MI')), " +
            "                    'DD/MM/YYYY HH24:MI'), " +
            "            (T.DT_VALIDADE)) " +
            " " +
            "  WHEN (U.CD_UNID_INT IN (7, 8, 9, 10)) AND " +
            "       (LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "        OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "          ORDER BY T.DT_REFERENCIA) NOT IN (7, 8, 9, 10)) " +
            "    THEN " +
            " " +
            "      FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT) " +
            " " +
            "  WHEN (U.CD_UNID_INT IN (7, 8, 9, 10)) AND " +
            "       ((LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "       OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "         ORDER BY T.DT_REFERENCIA)) = U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      (T.DT_VALIDADE - 1) " +
            " " +
            "  WHEN (U.CD_UNID_INT IN (7, 8, 9, 10)) AND " +
            "       (LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "        OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "          ORDER BY T.DT_REFERENCIA) <> U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT) " +
            " " +
            "  WHEN (U.CD_UNID_INT NOT IN (7, 8, 9, 10)) AND " +
            "       (LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "        OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "          ORDER BY T.DT_REFERENCIA) = U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      CASE WHEN T.DT_REFERENCIA = TRUNC(HR_LIB_MOV) AND (LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "                                                         OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "                                                           ORDER BY T.DT_REFERENCIA) <> U.CD_UNID_INT) " +
            "        THEN " +
            "          CASE WHEN HR_LIB_MOV < (T.DT_VALIDADE - 1) " +
            "            THEN " +
            "              (HR_LIB_MOV) - 1 / 24 / 60 / 60 " +
            "          ELSE " +
            "            (T.DT_VALIDADE - 1) " +
            "          END " +
            "      ELSE " +
            "        (T.DT_VALIDADE - 1) " +
            "      END " +
            " " +
            "  WHEN (U.CD_UNID_INT NOT IN (7, 8, 9, 10)) AND " +
            "       (LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "        OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "          ORDER BY T.DT_REFERENCIA) <> U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      CASE WHEN (LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "                 OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "                   ORDER BY T.DT_REFERENCIA) IN (7, 8, 9, 10)) " +
            "        THEN " +
            "          FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT) " +
            "      ELSE " +
            "        FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT) " +
            "      END " +
            " " +
            "  ELSE " +
            "    LEAST(FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT), T.DT_VALIDADE - 1) " +
            " " +
            "  END INICIO_VALIDADE, " +
            " " +
            "  CASE WHEN (TAB_01.CD_PRE_MED = T.CD_PRE_MED) AND (HR_LIB_MOV IS NOT NULL) AND " +
            "            ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "            OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "              ORDER BY T.DT_REFERENCIA)) = U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      CASE WHEN HR_LIB_MOV < T.DT_VALIDADE " +
            "        THEN " +
            "          (T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "      ELSE " +
            "        (T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "      END " +
            " " +
            "  WHEN (TAB_01.CD_PRE_MED = T.CD_PRE_MED) AND (HR_LIB_MOV IS NOT NULL) AND " +
            "       ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "       OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "         ORDER BY T.DT_REFERENCIA)) <> U.CD_UNID_INT) " +
            "    THEN " +
            " " +
            "      HR_LIB_MOV - 1 / 24 / 60 / 60 " +
            " " +
            "  WHEN (TAB_01.CD_PRE_MED = T.CD_PRE_MED) AND (HR_LIB_MOV IS NULL) " +
            "    THEN " +
            " " +
            "      (T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            " " +
            "  WHEN (U.CD_UNID_INT IN (7, 8, 9, 10)) AND " +
            "       ((LAG(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "       OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "         ORDER BY T.DT_REFERENCIA)) NOT IN (7, 8, 9, 10)) " +
            "    THEN " +
            " " +
            "      CASE WHEN HR_LIB_MOV IS NULL " +
            "        THEN " +
            "          (T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "      ELSE " +
            "        CASE WHEN (LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "                   OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "                     ORDER BY T.DT_REFERENCIA) = U.CD_UNID_INT) " +
            "          THEN " +
            "            (T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "        ELSE " +
            "          LEAST(HR_LIB_MOV, T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "        END " +
            " " +
            "      END " +
            " " +
            "  WHEN (U.CD_UNID_INT NOT IN (7, 8, 9, 10)) AND " +
            "       ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "       OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "         ORDER BY T.DT_REFERENCIA)) IN (7, 8, 9, 10)) " +
            "    THEN " +
            " " +
            "      LEAST(HR_LIB_MOV, T.DT_VALIDADE) - 1 / 24 / 60 / 60 " +
            "  WHEN (U.CD_UNID_INT NOT IN (7, 8, 9, 10)) AND " +
            "       ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "       OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "         ORDER BY T.DT_REFERENCIA)) NOT IN (7, 8, 9, 10)) " +
            "    THEN " +
            " " +
            "      CASE WHEN ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "      OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "        ORDER BY T.DT_REFERENCIA)) = U.CD_UNID_INT) " +
            "        THEN " +
            "          CASE WHEN T.DT_VALIDADE > HR_LIB_MOV " +
            "            THEN " +
            "              CASE WHEN ((LEAD(U.CD_UNID_INT, 2, U.CD_UNID_INT) " +
            "              OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "                ORDER BY T.DT_REFERENCIA)) <> U.CD_UNID_INT) " +
            "                THEN " +
            "                  HR_LIB_MOV - 1 / 24 / 60 / 60 " +
            "              ELSE " +
            "                T.DT_VALIDADE - 1 / 24 / 60 / 60 " +
            "              END " +
            "          ELSE " +
            "            T.DT_VALIDADE - 1 / 24 / 60 / 60 " +
            "          END " +
            "      ELSE " +
            "        CASE WHEN T.DT_VALIDADE < HR_LIB_MOV " +
            "          THEN " +
            "            T.DT_VALIDADE - 1 / 24 / 60 / 60 " +
            "        ELSE " +
            "          HR_LIB_MOV - 1 / 24 / 60 / 60 " +
            "        END " +
            "      END " +
            "  ELSE " +
            "    CASE WHEN HR_LIB_MOV IS NULL " +
            "      THEN " +
            "        T.DT_VALIDADE - 1 / 24 / 60 / 60 " +
            "    ELSE " +
            "      CASE WHEN ((LEAD(U.CD_UNID_INT, 1, U.CD_UNID_INT) " +
            "      OVER (PARTITION BY T.CD_ATENDIMENTO " +
            "        ORDER BY T.DT_REFERENCIA)) <> U.CD_UNID_INT) " +
            "        THEN " +
            "          HR_LIB_MOV - 1 / 24 / 60 / 60 " +
            "      ELSE " +
            "        T.DT_VALIDADE - 1 / 24 / 60 / 60 " +
            "      END " +
            "    END " +
            "  END FIM_VALIDADE, " +
            "T.CD_SETOR, " +
            "T.DT_REFERENCIA " +
            "FROM DBAMV.PRE_MED T " +
            "  LEFT JOIN DBAMV.UNID_INT U " +
            "    ON (T.CD_UNID_INT = U.CD_UNID_INT) " +
            "  JOIN DBAMV.TB_SETOR S " +
            "    ON (S.CD_SETOR = T.CD_SETOR) " +
            "  JOIN DBAMV.TB_ATENDIME ATD " +
            "    ON (ATD.CD_ATENDIMENTO = T.CD_ATENDIMENTO) " +
            "       AND (ATD.CD_MULTI_EMPRESA = 1) " +
            "       AND ATD.TP_ATENDIMENTO = 'I' " +
            "  LEFT JOIN (SELECT " +
            "               P.CD_ATENDIMENTO, " +
            "               MIN(P.CD_PRE_MED) CD_PRE_MED " +
            "             FROM DBAMV.PRE_MED P " +
            "             WHERE P.TP_PRE_MED = 'M' " +
            "                   AND P.FL_IMPRESSO = 'S' " +
            "             GROUP BY P.CD_ATENDIMENTO " +
            "            ) TAB_01 " +
            "    ON (T.CD_ATENDIMENTO = TAB_01.CD_ATENDIMENTO) " +
            "  JOIN (SELECT " +
            "          AA.CD_ATENDIMENTO, " +
            "          AA.DT_REFERENCIA, " +
            "          MIN(AA.CD_PRE_MED) CD_PRE_MED " +
            "        FROM DBAMV.PRE_MED AA " +
            "        WHERE AA.TP_PRE_MED = 'M' " +
            "              AND AA.FL_IMPRESSO = 'S' " +
            "        GROUP BY AA.CD_UNID_INT, AA.CD_ATENDIMENTO, AA.DT_REFERENCIA " +
            "       ) MEN " +
            "    ON (MEN.CD_ATENDIMENTO = T.CD_ATENDIMENTO) " +
            "       AND (MEN.DT_REFERENCIA = T.DT_REFERENCIA) " +
            "  JOIN (SELECT " +
            "          CD_ATENDIMENTO, " +
            "          DT_MOV_INT, " +
            "          HR_MOV_INT, " +
            "          DT_LIB_MOV, " +
            "          HR_LIB_MOV " +
            "        FROM MOV_INT " +
            "        ORDER BY HR_MOV_INT) MI " +
            "    ON (MI.CD_ATENDIMENTO = T.CD_ATENDIMENTO) " +
            "       AND (FNC_VAH_TIMESTAMP(MI.DT_MOV_INT, MI.HR_MOV_INT) <= (T.DH_CRIACAO)) " +
            "       AND ((MI.DT_LIB_MOV IS NULL) OR (FNC_VAH_TIMESTAMP(MI.DT_LIB_MOV, MI.HR_LIB_MOV) >= (T.DH_CRIACAO))) " +
            "WHERE T.CD_PRE_MED = MEN.CD_PRE_MED " +
            "      AND T.CD_ATENDIMENTO = :CD_ATENDIMENTO " +
            (begin != null && end != null ? "      AND T.DT_REFERENCIA BETWEEN :DT_BEGIN AND :DT_END " : "") +
            "      AND T.CD_PRE_MED IN (SELECT MIN(AA.CD_PRE_MED) CD_PRE_MED " +
            "                           FROM DBAMV.PRE_MED AA " +
            "                           WHERE AA.CD_ATENDIMENTO = T.CD_ATENDIMENTO " +
            "                                 AND AA.TP_PRE_MED = 'M' " +
            "                                 AND AA.FL_IMPRESSO = 'S' " +
            "                                 AND AA.DT_REFERENCIA = T.DT_REFERENCIA " +
            "                           GROUP BY AA.CD_UNID_INT " +
            ")";

    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter("CD_ATENDIMENTO", protocolo.getAtendimento().getId());
    if (begin != null && end != null) {
      query.setParameter("DT_BEGIN", begin);
      query.setParameter("DT_END", end);
    }

    List<Object[]> rows = query.list();

    rows = preencherGaps(rows);

    return rows.stream().filter((row) -> checkRow(row, protocolo)).collect(Collectors.toList());
  }

  private List<Object[]> preencherGaps(List<Object[]> rows) {
    List<Object[]> resultado = new ArrayList<>();
    Iterator<Object[]> iterator = rows.iterator();
    if (iterator.hasNext()) {
      Object[] ultimoVerificado = iterator.next();
      resultado.add(ultimoVerificado);
      while (iterator.hasNext()) {
        Object[] corrente = iterator.next();
        Date fimUltimo = (Date) ultimoVerificado[1];
        Date inicioCorrente = (Date) corrente[0];
        long t1 = fimUltimo.getTime();
        long t2 = inicioCorrente.getTime();

        if ((t2 - t1) > 1000) {
          Object[] gap = new Object[4];
          gap[0] = new Date(t1 + 1000);
          gap[1] = new Date(t2 - 1000);
          gap[2] = ultimoVerificado[2];
          gap[3] = ultimoVerificado[3];
          resultado.add(gap);
        }
        ultimoVerificado = corrente;
      }
    }

    return resultado;

  }

  private Boolean checkRow(Object[] row, Protocolo protocolo) {
    Date begin = (Date) row[0];
    Date end = (Date) row[1];
    Long idSetor = ((BigDecimal) row[2]).longValue();
    return begin.compareTo(end) != 0 && protocolo.getOrigem().getSetorMV().getId().longValue() == idSetor;
  }
}
