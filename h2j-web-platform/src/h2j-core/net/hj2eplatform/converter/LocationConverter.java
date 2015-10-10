/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@FacesConverter("locationConverter")
public class LocationConverter implements Converter, java.io.Serializable {

    private ILocationService locationService;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        locationService = (ILocationService) ctx.getBean("locationServiceImpl");
        try {
            Long locationId = Long.valueOf(value);
            if (value == null || locationId == -1L) {
                return null;
            }

            return locationService.loadEntity(Location.class, locationId);
        } catch (NumberFormatException e) {
            return locationService.getLocation4Converter(value);
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value instanceof Integer || value instanceof Long) {
            return "-1";
        }
        Location location = (Location) value;
        if (location.getLocationId() == null) {
            return "-1";
        }
        return location.getLocationId().toString();
    }
}
