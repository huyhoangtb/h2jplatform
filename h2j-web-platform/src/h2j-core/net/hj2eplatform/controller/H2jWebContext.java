/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.hj2eplatform.context.H2jContextDefine;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("webContext")
@Scope("session")
public class H2jWebContext implements Serializable{

    private Map store;
    private String backUrl;
    @Autowired
    private ILocationService locationService;

    @PostConstruct
    private void init() {
        store = new HashMap();
        store.put(ORG_CUSTOMER_VIEW_PARRAM, ORG_CUSTOMER_VIEW_PARRAM);
        store.put(ORG_PARTNER_VIEW_PARRAM, ORG_PARTNER_VIEW_PARRAM);
        store.put(ORG_MY_VIEW_PARRAM, ORG_MY_VIEW_PARRAM);
        store.put(AGENT_PARTNER_VIEW_PARRAM, AGENT_PARTNER_VIEW_PARRAM);
        store.put(SUPPLIER_TOUR_VIEW_PARRAM, SUPPLIER_TOUR_VIEW_PARRAM);
    }

    public Map context() {
        return store;
    }

    public void returnBackUrl() {
        try {
            String url = null;
            // redirect ve url truoc do
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(ControllerUtils.getRequest(), ControllerUtils.getResponse());
            if (savedRequest == null) {
                this.backUrl = url;
            }
            url = savedRequest.getRedirectUrl();
            if (url != null) {
                this.backUrl = url;
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            }
        } catch (Exception e) {

        }

    }

    public String getParam(String param) {
        return ControllerUtils.getRequestParameter(param);
    }

    public Organization getRootOrganization() {
        return H2jContextDefine.getH2jRootOrg();
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getNationalLocationDefault() {
        return WEB_STARTED_DEFAULT_LOCATION;
    }

    public List<SelectItem> getStartLocation() {
        List<SelectItem> locationList = ControllerUtils.getSessionParamester(WEB_STARTED_DEFAULT_LOCATION);
        if (locationList == null) {
            List<Location> locations = locationService.getLocationByParentList(SystemConfig.getResource("hj2eplatform.languagle.translateDefault"));
            locationList = new ArrayList<SelectItem>();
            locationList.add(new SelectItem(0L, "Chọn nơi khởi hành"));
            for (Location location : locations) {
                locationList.add(new SelectItem(location.getLocationId(), location.getLocationName()));
            }
            ControllerUtils.setSessionParamester(WEB_STARTED_DEFAULT_LOCATION, locationList);
        }
        return locationList;
    }
    
     public static H2jWebContext curentInstance() {
        H2jWebContext controller = ControllerUtils.getBean("webContext");
        return controller;
    }
    public static final String WEB_STARTED_DEFAULT_LOCATION = "WEB_STARTED_DEFAULT_LOCATION";
    public static final String WEB_NATIONAL_DEFAULT_LOCATION = "WEB_NATIONAL_DEFAULT_LOCATION";
    public static final String ORG_CUSTOMER_VIEW_PARRAM = "ORG_CUSTOMER";
    public static final String RETAIL_CUSTOMER = "RETAIL_CUSTOMER";
    public static final String ORG_PARTNER_VIEW_PARRAM = "SUPPLIER_PARTNER";
    public static final String SUPPLIER_TOUR_VIEW_PARRAM = "SUPPLIER_TOUR";
    public static final String AGENT_PARTNER_VIEW_PARRAM = "AGENT_PARTNER";
    public static final String ORG_MY_VIEW_PARRAM = "ORG_VIEW";
}
