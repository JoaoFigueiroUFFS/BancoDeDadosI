package converter; 

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Rota;

@FacesConverter(forClass = Rota.class, value = "RotaConverter")
public class RotaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Rota) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Rota) {
            Rota entity = (Rota) value;
            if (entity instanceof Rota && entity.getIdRota() != null) {
                uiComponent.getAttributes().put(entity.getIdRota().toString(), entity);
                return entity.getIdRota().toString();
            }
        }
        return "";
    }
}
