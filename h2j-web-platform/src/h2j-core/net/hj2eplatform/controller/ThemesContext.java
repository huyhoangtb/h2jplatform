/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.utils.DomainType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller(value = "Themes")
@Scope("session")
public class ThemesContext implements Serializable {

    private String currentTheme;
    private int domainType = DomainType.ADMINISTRATOR.toInteger();

    @PostConstruct
    public void init() {
        String domain = ControllerUtils.getRequest().getServerName();
        if (domain.startsWith("www.")) {
            domain = domain.substring(4, domain.length());
        }
        if(domain.startsWith("admin")) {
//            this.currentTheme = "/themes/mpay_admin/main_template.xhtml";
            this.currentTheme = "/themes/administrator/admin_tmpl.xhtml";
            domainType = DomainType.ADMINISTRATOR.toInteger();
        } else {
            this.currentTheme = "/themes/mpay_user_theme/main_template.xhtml";
            domainType = DomainType.CUSTOMER_SITE.toInteger();
        }
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public int getDomainType() {
        return domainType;
    }

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public static ThemesContext getInstance() {
        ThemesContext context = ControllerUtils.getBean("Themes");
        return context;
    }
}
