/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author HuyHoang
 */
@Controller("h2jPlatformAppUtils")
@Scope("application")
public class CoreApplicationUtilController implements Serializable {

    public boolean isCurrentView(String viewId, String outcome, String paramester) {
        Boolean result = null;
        if (viewId == null || outcome == null) {
            return false;
        }
        StringBuilder outcomeStrd = new StringBuilder(outcome);
        if (outcome.endsWith(".jsf")) {
            outcomeStrd.delete(outcomeStrd.length() - 4, outcomeStrd.length());
            outcomeStrd.append(".xhtml");
        } else if (!outcome.contains(".")) {
            outcomeStrd.append(".xhtml");
        }
        result = (viewId.compareTo(outcomeStrd.toString()) == 0);
        //TODO: return false or ignore validate paramester if no parameter must be validate
        if (paramester == null || paramester.trim().compareTo("") == 0 || !result) {
            return result;
        }

        String[] paramArray = paramester.split("&");
        for (String eachParam : paramArray) {
            String[] eachParamArray = eachParam.split("=");
            if (eachParamArray.length != 2) {
                ControllerUtils.addAlertMessage("Configuration menu parameter faile: outcome = '" + outcome + "' param: '" + paramester + "' at: '" + eachParam + "'");
                return false;
            }
            String valueFromRequest = ControllerUtils.getRequestParameter(eachParamArray[0]);
            if (valueFromRequest != null && valueFromRequest.compareTo(eachParamArray[1]) != 0) {
                return false;
            }
        }
        return true;

    }

    public String encodeUrl(String url) {
        try {
            return java.net.URLEncoder.encode(url, "UTF8");
        } catch (Exception e) {

        }
        return "/";
    }

    public String concat(String st1, String st2) {
        StringBuilder result = new StringBuilder();
        result.append(st1 == null ? "" : st1);
        result.append(st2 == null ? "" : st2);
        return result.toString();
    }
  public String concatURL(String url,String paramester) {
        StringBuilder result = new StringBuilder(url);
        if(paramester != null) {
            if(!url.contains("?faces-redirect=true")) {
                result.append("?faces-redirect=true&");
            }
            result.append(paramester);
        }
        
        return result.toString();
    }
    public String concat(String st1, String st2, String st3) {
        StringBuilder result = new StringBuilder();
        result.append(st1 == null ? "" : st1);
        result.append(st2 == null ? "" : st2);
        result.append(st3 == null ? "" : st3);
        return result.toString();
    }
    public Integer getCurrentYear() {
        return DateTimeUtils.getCurrentYear();
    }
    public String getServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.images");
    }
    public String getServerAvatarImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.avatar.images");
    }
    public String getXlargeServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.xlarge.images");
    }
    public String getLargeServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.large.images");
    }
    public String getMediumServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.medium.images");
    }
    public String getThubmServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.thumb.images");
    }
    public String getSmallServerImage() {
        return SystemConfig.getResource("h2jeplatform.server.resource.thumb.images");
    }
}
