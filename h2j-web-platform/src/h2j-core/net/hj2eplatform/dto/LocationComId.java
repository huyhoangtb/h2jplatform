/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import static net.hj2eplatform.controller.H2jWebContext.WEB_NATIONAL_DEFAULT_LOCATION;
import static net.hj2eplatform.controller.H2jWebContext.WEB_STARTED_DEFAULT_LOCATION;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.core.utils.SystemConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
public class LocationComId implements Serializable {

    private List<SelectItem> nationalList;
    private List<SelectItem> provinceList;
    private List<SelectItem> districtList;
    private List<SelectItem> streetList;
    private Long nationalSelected;
    private Long provinceSelected;
    private Long districtSelected;
    private Long streetSelected;
    private ILocationService locationService;

    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        locationService = (ILocationService) ctx.getBean("locationServiceImpl");

    }

    public Location getLocation(Long id) {
        if (id == null) {
            return null;
        }
        return locationService.loadEntity(Location.class, id);
    }

//    public Location getLocation(Long id) {
//        if (id == null) {
//            return null;
//        }
//        return locationService.loadEntity(Location.class, id);
//    }
    public List<SelectItem> getStreet4Change() {
        initStreetLocation();
        if (this.districtSelected == null) {
            return this.streetList;
        }
        List<Location> locations = locationService.getLocationList(this.districtSelected);
        if (locations == null && locations.size() < 1) {
            return this.streetList;
        }
        for (Location location : locations) {
            this.streetList.add(new SelectItem(location.getLocationId(), location.getLocationName()));
        }
        return this.streetList;
    }

    public List<SelectItem> getDistrict4Change() {
        initDistrictLocation();
        if (this.provinceSelected == null) {
            this.districtSelected = null;
            return this.districtList;
        }

        List<Location> locations = locationService.getLocationList(this.provinceSelected);
        if (locations == null && locations.size() < 1) {
            return districtList;
        }
        for (Location location : locations) {
            this.districtList.add(new SelectItem(location.getLocationId(), location.getLocationName()));
        }
        return this.districtList;
    }

    public void changeProvince(ValueChangeEvent changeListener) {
        this.provinceSelected = (Long) changeListener.getNewValue();
        getDistrict4Change();
    }

    public void changeNational(ValueChangeEvent changeListener) {
        this.nationalSelected = (Long) changeListener.getNewValue();
        getProvince4Change();
    }

    public List<SelectItem> getProvince4Change() {
        initProvinceLocation();
        if (this.nationalSelected == null) {
            this.provinceSelected = null;
            return this.provinceList;
        }

        List<Location> locations = locationService.getLocationList(this.nationalSelected);
        if (locations == null && locations.size() < 1) {
            return this.provinceList;
        }
        for (Location location : locations) {
            this.provinceList.add(new SelectItem(location.getLocationId(), location.getLocationName()));
        }
        return this.provinceList;
    }

    public List<SelectItem> getNationalDefault() {
        this.nationalList = new ArrayList<SelectItem>();
        this.nationalList.add(new SelectItem(-1L, ResourceMessages.getResource("location_label_choice_national")));
        List<Location> locations = locationService.getLocationList(null);
        for (Location location : locations) {
            this.nationalList.add(new SelectItem(location.getLocationId(), location.getLocationName()));

        }
        return this.nationalList;
    }

    public List<SelectItem> getAllLocation() {
        this.nationalList = ControllerUtils.getSessionParamester(WEB_NATIONAL_DEFAULT_LOCATION);
        if (nationalList == null) {
            getNationalDefault();
            ControllerUtils.setSessionParamester(WEB_NATIONAL_DEFAULT_LOCATION, nationalList);
        }
        return nationalList;
    }

    public List<SelectItem> getStartLocation() {
        this.provinceList = ControllerUtils.getSessionParamester(WEB_STARTED_DEFAULT_LOCATION);
        if (provinceList == null) {
            List<Location> locations = locationService.getLocationByParentList(SystemConfig.getResource("hj2eplatform.languagle.default"));
            provinceList = new ArrayList<SelectItem>();
            provinceList.add(new SelectItem(0L, "Chọn nơi khởi hành"));
            for (Location location : locations) {
                provinceList.add(new SelectItem(location.getLocationId(), location.getLocationName()));
            }
            ControllerUtils.setSessionParamester(WEB_STARTED_DEFAULT_LOCATION, provinceList);
        }
        return provinceList;
    }

    private void initProvinceLocation() {
        this.provinceList = new ArrayList<SelectItem>();
        this.provinceList.add(new SelectItem(-1L, ResourceMessages.getResource("location_label_choice_province")));
    }

    private void initStreetLocation() {
        this.streetList = new ArrayList<SelectItem>();
        this.streetList.add(new SelectItem(-1L, ResourceMessages.getResource("location_label_choice_street")));
    }

    private void initNationalLocation() {
        this.nationalList = new ArrayList<SelectItem>();
        this.nationalList.add(new SelectItem(-1L, ResourceMessages.getResource("location_label_choice_national")));
    }

    private void initDistrictLocation() {
        this.districtList = new ArrayList<SelectItem>();
        this.districtList.add(new SelectItem(-1L, ResourceMessages.getResource("location_label_choice_district")));
    }

    public List<SelectItem> getNationalList() {
        return nationalList;
    }

    public void setNationalList(List<SelectItem> nationalList) {
        this.nationalList = nationalList;
    }

    public List<SelectItem> getProvinceList() {
        if (provinceList == null) {
            getProvince4Change();
        }
        return provinceList;
    }

    public void setProvinceList(List<SelectItem> provinceList) {

        this.provinceList = provinceList;
    }

    public List<SelectItem> getDistrictList() {
        if (districtList == null) {
            getDistrict4Change();
        }
        return districtList;
    }

    public void setDistrictList(List<SelectItem> districtList) {
        this.districtList = districtList;
    }

    public List<SelectItem> getStreetList() {
        return streetList;
    }

    public void setStreetList(List<SelectItem> streetList) {
        this.streetList = streetList;
    }

    public Long getNationalSelected() {
        return nationalSelected;
    }

    public void setNationalSelected(Long nationalSelected) {
        this.nationalSelected = nationalSelected;
    }

    public Long getProvinceSelected() {
        return provinceSelected;
    }

    public void setProvinceSelected(Long provinceSelected) {
        this.provinceSelected = provinceSelected;
    }

    public Long getDistrictSelected() {
        return districtSelected;
    }

    public void setDistrictSelected(Long districtSelected) {
        this.districtSelected = districtSelected;
    }

    public Long getStreetSelected() {
        return streetSelected;
    }

    public void setStreetSelected(Long streetSelected) {
        this.streetSelected = streetSelected;
    }

    public ILocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(ILocationService locationService) {
        this.locationService = locationService;
    }
}
