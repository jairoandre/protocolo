package br.com.vah.protocolo.util;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.function.BasicFunctionExpression;
import org.hibernate.jpa.criteria.expression.function.FunctionExpression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Selection;
import java.io.Serializable;

/**
 * Created by Jairoportela on 13/02/2017.
 */
public class StrFunction<T extends Number> extends BasicFunctionExpression<String> implements FunctionExpression<String>, Serializable {
  public static final String FCT_NAME = "str";

  private final Selection<T> selection;

  public StrFunction(CriteriaBuilder criteriaBuilder, Selection<T> selection) {
    super((CriteriaBuilderImpl) criteriaBuilder, String.class, FCT_NAME);
    this.selection = selection;
  }

  @Override
  public void registerParameters(ParameterRegistry registry) {
    Helper.possibleParameter(selection, registry);
  }

  @Override
  public String renderProjection(RenderingContext renderingContext) {
    return FCT_NAME + '(' + ((Renderable) selection).render(renderingContext) + ')';
  }
}
