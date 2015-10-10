/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.hj2eplatform.iservices.IHumanService;
import net.hj2eplatform.models.Permission;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@FacesConverter("humanConverter")
public class HumanConverter implements Converter, java.io.Serializable {

    private IHumanService humanService;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        

        if (value == null || Long.valueOf(value) == -1L) {
            return null;
        }
        humanService = (IHumanService) ctx.getBean("humanServiceImpl");
        return humanService.loadEntity(Human.class, Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null || value instanceof Integer) {
            return "-1";
        }
        return ((Human) value).getHumanId().toString();
    }
}
