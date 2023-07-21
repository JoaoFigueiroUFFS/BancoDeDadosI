package converter; 

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Cidade;

@FacesConverter(forClass = Cidade.class, value = "CidadeConverter")
public class CidadeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Cidade) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Cidade) {
            Cidade entity = (Cidade) value;
            if (entity instanceof Cidade && entity.getIdCidade() != null) {
                uiComponent.getAttributes().put(entity.getIdCidade().toString(), entity);
                return entity.getIdCidade().toString();
            }
        }
        return "";
    }
}
