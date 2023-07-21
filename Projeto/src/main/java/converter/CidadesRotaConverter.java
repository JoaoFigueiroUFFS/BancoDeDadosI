package converter; 

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CidadesRota;

@FacesConverter(forClass = CidadesRota.class, value = "CidadesRotaConverter")
public class CidadesRotaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (CidadesRota) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof CidadesRota) {
            CidadesRota entity = (CidadesRota) value;
            if (entity instanceof CidadesRota && entity.getIdCidadeRota() != null) {
                uiComponent.getAttributes().put(entity.getIdCidadeRota().toString(), entity);
                return entity.getIdCidadeRota().toString();
            }
        }
        return "";
    }
}
