package br.com.vah.protocolo.converters;

import br.com.vah.protocolo.entities.dbamv.ProFat;
import br.com.vah.protocolo.service.ProFatService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ProFatConverter implements Converter {

  private
  @Inject
  ProFatService service;

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value != null && value.trim().length() > 0) {
      return service.find(value);
    } else {
      return null;
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value != null) {
      return ((ProFat) value).getIdStr();
    } else {
      return null;
    }
  }
}