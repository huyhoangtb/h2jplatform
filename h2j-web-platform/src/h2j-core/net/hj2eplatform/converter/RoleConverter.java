/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.hj2eplatform.core.iservices.IAbstractService;
import net.hj2eplatform.models.Role;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@FacesConverter("roleConverter")
public class RoleConverter implements Converter, java.io.Serializable {

    private IAbstractService service;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        service = (IAbstractService) ctx.getBean("roleServiceImpl");
        if (value == null || Long.valueOf(value) == -1L) {
            return null;
        }

        return service.loadEntity(Role.class, Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null || value instanceof Integer) {
            return "-1";
        }
        return ((Role) value).getRoleId().toString();
    }
}
