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
import net.hj2eplatform.models.Human;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@FacesConverter("humanSuggestConverter")
public class HumanSuggestConverter implements Converter, java.io.Serializable {

    private IHumanService humanService;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        Long humanId = null;
        if (value == null) {
            return null;
        }
        try {
            humanId = Long.valueOf(value);
        } catch (Exception e) {
            Human human = new Human();
            human.setFullName(value);
            return human;
        }
        if (humanId == -1) {
            return null;
        }
        humanService = (IHumanService) ctx.getBean("humanServiceImpl");
        return humanService.loadEntity(Human.class, humanId);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (value == null || value instanceof Integer || value instanceof String) {
            return "-1";
        }
        Human human = ((Human) value);
        if (human.getHumanId() == null) {
            return human.getFullName();
        }
       
        return ((Human) value).getHumanId().toString();
    }
}
