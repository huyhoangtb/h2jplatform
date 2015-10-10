/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.context;

import java.util.ArrayList;
import java.util.List;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Domain;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
public class H2jContextDefine {

    private static Organization h2jRootOrg;
    private static Domain domain;
    private static List<String> exchangeDomainList;
    public static final String DEFAULT_DOMAIN = "bookingtour.vn";

    public static Organization getH2jRootOrg() {
        if (h2jRootOrg == null) {
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                    getServletContext());
            IOrganizationService service = (IOrganizationService) ctx.getBean("organizationServiceImpl");
            h2jRootOrg = service.getOrganizationByCode("H2J_ORG_ROOT_CODE");
        }
        return h2jRootOrg;
    }

    public static List<String> getH2jDomainConfigList() {
        String domainList = SystemConfig.getResource("hj2eplatform.exchange.domain.list");
        List<String> ls = new ArrayList<String>();
        if (domainList == null || domainList.compareTo("hj2eplatform.exchange.domain.list") == 0) {
            return ls;
        }
        String[] domainArg = domainList.split(";");
        for (String domain : domainArg) {
            ls.add(domain);
        }
        return ls;
    }

    public static boolean checkH2jDomainConfig(String domainName) {
        List<String> ls = getH2jDomainConfigList();
        if (ls.size() == 0) {
            return false;
        }
        for (String domain : ls) {
            if (domain.compareTo(domainName) == 0) {
                return true;
            }
        }
        return false;
    }
}
