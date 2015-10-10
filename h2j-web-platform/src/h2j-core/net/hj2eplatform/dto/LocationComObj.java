/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
public class LocationComObj implements Serializable {

    private List<SelectItem> nationalList;
    private List<SelectItem> provinceList;
    private List<SelectItem> districtList;
    private List<SelectItem> streetList;
    private Location nationalSelected;
    private Location provinceSelected;
    private Location districtSelected;
    private Location streetSelected;
    private ILocationService locationService;

    public void init() {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        locationService = (ILocationService) ctx.getBean("locationServiceImpl");

    }

    public Location getLocation(BigInteger id) {
        if (id == null) {
            return null;
        }
        return locationService.loadEntity(Location.class, id.longValue());
    }

    public Location getLocation(Long id) {
        try {
            if (id == null && id > 0L) {
                return null;
            }
            return locationService.loadEntity(Location.class, id);
        } catch (Exception ex) {
            return null;
        }

    }

    public List<SelectItem> getStreet4Change() {
        initStreetLocation();
        if (this.districtSelected == null) {
            return this.streetList;
        }
        List<Location> locations = locationService.getLocationList(this.districtSelected.getLocationId());
        if (locations == null && locations.size() < 1) {
            return this.streetList;
        }
        for (Location location : locations) {
            this.streetList.add(new SelectItem(location, location.getLocationName()));
        }
        return this.streetList;
    }

    public List<SelectItem> getDistrict4Change() {
        initDistrictLocation();
        if (this.provinceSelected == null) {
            this.districtSelected = null;
            return this.districtList;
        }

        List<Location> locations = locationService.getLocationList(this.provinceSelected.getLocationId());
        if (locations == null && locations.size() < 1) {
            return districtList;
        }
        for (Location location : locations) {
            this.districtList.add(new SelectItem(location, location.getLocationName()));
        }
        return this.districtList;
    }

    public List<SelectItem> getProvince4Change() {
        initProvinceLocation();
        if (this.nationalSelected == null) {
            this.provinceSelected = null;
            return this.provinceList;
        }

        List<Location> locations = locationService.getLocationList(this.nationalSelected.getLocationId());
        if (locations == null && locations.size() < 1) {
            return this.provinceList;
        }
        for (Location location : locations) {
            this.provinceList.add(new SelectItem(location, location.getLocationName()));
        }
        return this.provinceList;
    }

    public List<SelectItem> getNationalDefault() {
        this.nationalList = new ArrayList<SelectItem>();
        this.nationalList.add(new SelectItem(-1, ResourceMessages.getResource("location_label_choice_national")));
        List<Location> locations = locationService.getLocationList(null);
        for (Location location : locations) {
            this.nationalList.add(new SelectItem(location, location.getLocationName()));

        }
        return this.nationalList;
    }

    private void initProvinceLocation() {
        this.provinceList = new ArrayList<SelectItem>();
        this.provinceList.add(new SelectItem(-1, ResourceMessages.getResource("location_label_choice_province")));
    }

    private void initStreetLocation() {
        this.streetList = new ArrayList<SelectItem>();
        this.streetList.add(new SelectItem(-1, ResourceMessages.getResource("location_label_choice_street")));
    }

    private void initNationalLocation() {
        this.nationalList = new ArrayList<SelectItem>();
        this.nationalList.add(new SelectItem(-1, ResourceMessages.getResource("location_label_choice_national")));
    }

    private void initDistrictLocation() {
        this.districtList = new ArrayList<SelectItem>();
        this.districtList.add(new SelectItem(-1, ResourceMessages.getResource("location_label_choice_district")));
    }

    public List<SelectItem> getNationalList() {
        return nationalList;
    }

    public void setNationalList(List<SelectItem> nationalList) {
        this.nationalList = nationalList;
    }

    public List<SelectItem> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<SelectItem> provinceList) {
        this.provinceList = provinceList;
    }

    public List<SelectItem> getDistrictList() {

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

    public Location getNationalSelected() {
        return nationalSelected;
    }

    public void setNationalSelected(Location nationalSelected) {

        this.nationalSelected = nationalSelected;
    }

    public Location getProvinceSelected() {
        return provinceSelected;
    }

    public void setProvinceSelected(Location provinceSelected) {
        this.provinceSelected = provinceSelected;
    }

    public Location getDistrictSelected() {
        return districtSelected;
    }

    public void setDistrictSelected(Location districtSelected) {
        this.districtSelected = districtSelected;
    }

    public Location getStreetSelected() {
        return streetSelected;
    }

    public void setStreetSelected(Location streetSelected) {
        this.streetSelected = streetSelected;
    }

    public ILocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(ILocationService locationService) {
        this.locationService = locationService;
    }
}
