/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.converter;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@FacesConverter("organizationConverter")
public class OrganizationAutoConverter implements Converter, java.io.Serializable {

    private IOrganizationService organizationService;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        organizationService = (IOrganizationService) ctx.getBean("organizationServiceImpl");
        if (value == null || value.trim().equals("") || value.trim().equals("null")) {
            return null;
        } else {

            try {

                Long number = Long.parseLong(value);

                return organizationService.loadEntity(Organization.class, number);

            } catch (NumberFormatException exception) {
                List<Organization> orgs = organizationService.getOrganizationByName(value);
                if (orgs != null && orgs.size() == 1) {
                    return orgs.get(0);
                }
                return null;
//                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Không tìm thấy dữ liệu người dùng, vui lòng không copy pates dữ liệu tổ chức." + value, "Không đúng dữ liệu tổ chức người dùng!"));
            }
        }


    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Organization) value).getOrganizationId());
        }
    }
}
