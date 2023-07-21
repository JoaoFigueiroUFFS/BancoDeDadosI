package converter; 

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Bairro;

@FacesConverter(forClass = Bairro.class, value = "BairroConverter")
public class BairroConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Bairro) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Bairro) {
            Bairro entity = (Bairro) value;
            if (entity instanceof Bairro && entity.getIdBairro() != null) {
                uiComponent.getAttributes().put(entity.getIdBairro().toString(), entity);
                return entity.getIdBairro().toString();
            }
        }
        return "";
    }
}
