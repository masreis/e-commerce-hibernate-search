package net.marcoreis.ecommerce.controlador.conversor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import net.marcoreis.ecommerce.negocio.GenericService;
import net.marcoreis.ecommerce.util.IPersistente;

import org.apache.log4j.Logger;

public abstract class GenericConverter implements Converter {
    private static Logger logger = Logger.getLogger(GenericConverter.class);

    public abstract Class<?> getClasse();

    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        try {
            Long id = Long.parseLong(value);
            Object objeto = new GenericService().findById(getClasse(), id);
            logger.info(objeto);
            return objeto;
        } catch (Exception e) {
            return null;
        }
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        IPersistente p = (IPersistente) value;
        return String.valueOf(p.getId());
    }

}
