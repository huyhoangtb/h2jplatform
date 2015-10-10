/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import net.hj2eplatform.core.component.LazyDataSupportMapFilterAndObject;
import net.hj2eplatform.dto.LocationSearch;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.core.utils.AccentRemover;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.SystemDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
@Controller("locationController")
@Scope("view")
public class LocationController implements Serializable {

    private Location location;
    private Location parentLocation;
    private List<Location> suggetionList;
    @Autowired
    private ILocationService locationService;
    public static List<Location> LOCATION_LIST;
    private LazyDataSupportMapFilterAndObject<Location> locationDataModel;
    private LocationSearch locationSearch;
    private String showLocationPopup = "false";

    public static List<Location> instalLocationList() {
//        if(LOCATION_LIST != null) {
//            return  LOCATION_LIST;
//        }
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        ILocationService locationService = (ILocationService) ctx.getBean("locationServiceImpl");
        LOCATION_LIST = locationService.getLocationList(null);
        return LOCATION_LIST;
    }

    @PostConstruct
    public void init() {
        String locationId = ControllerUtils.getRequestParameter("locationId");
        if (locationId != null) {
            location = locationService.loadEntity(Location.class, Long.valueOf(locationId));
            ResourceManagerController resource = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
            resource.loadMyResoure(location.getImgId());
            if(location.getParentId() != null) {
                this.parentLocation = locationService.loadEntity(Location.class, location.getParentId());
            }
        } else {
            location = new Location();
        }
        locationDataModel = new LazyDataSupportMapFilterAndObject(locationService);
        locationSearch = new LocationSearch();
//        locationSearch.setLocationType(1);
        locationDataModel.setSearchObject(locationSearch);
    }

    public void retypeLocation() {
        location = new Location();
    }

    public void updateLocation() {
        if (!validateLocation()) {
            return;
        }
        if (parentLocation != null) {
            location.setParentId(parentLocation.getLocationId());
            location.setPath(parentLocation.getPath() + location.getLocationId() + "_");
        } else {
            location.setPath(location.getLocationId() + "_");
            location.setParentId(null);
        }
        updateLocationImage();
        locationService.saveEntity(location);
        ControllerUtils.addSuccessMessage("Cập nhật thông tin địa danh thành công!");
    }

    public void deleteLocation() {
        List<Location> locationList = locationService.getLocationList(location.getLocationId());
        if (locationList != null && locationList.size() > 0) {
            ControllerUtils.addAlertMessage("Không thể xóa địa danh này! Vui lòng xóa các địa danh con trước!");
            return;
        }
        locationService.removeEntity(location);
        location = new Location();
        ControllerUtils.addSuccessMessage("Xóa địa danh thành công!");
        showLocationPopup = "false";
    }

    public void startAddSubLocation() {
        this.parentLocation = location;
        location = new Location();
        location.setType(5);
        location.setStatus(1);
        showLocationPopup = "true";
    }

    public String startCreateLocation() {
        location = new Location();
        location.setType(5);
        location.setStatus(1);
        this.parentLocation = new Location();
        return "create_location";
    }

    public void createLocation() {
        if (!validateLocation()) {
            return;
        }
        location.setLocationId(locationService.getSequence(SystemDefine.SEQUENCE_LOCATION_ID).longValue());
        if (parentLocation != null) {
            location.setParentId(parentLocation.getLocationId());
            location.setPath(parentLocation.getPath() + location.getLocationId() + "_");
        } else {
            location.setPath(location.getLocationId() + "_");
            location.setParentId(null);
        }
        updateLocationImage();
        locationService.persistEntity(location);
        ControllerUtils.addSuccessMessage("Cập nhật thông tin địa danh thành công!");
    }

    private void updateLocationImage() {
        ResourceManagerController resourceManagerController = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
        if (resourceManagerController.getMyResource() != null && resourceManagerController.getMyResource().getResourceId() != null) {
            location.setImgId(resourceManagerController.getMyResource().getResourceId());
        }
    }

    public boolean validateLocation() {
        if (location.getLocationName() == null || location.getLocationName().trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Bạn chưa nhập tên địa danh! Vui lòng kiếm tra lại.");
            return false;
        }
        if (location.getType() == null || location.getType() <= 0) {
            ControllerUtils.addErrorMessage("Vui lòng chọn loại địa danh.");
            return false;
        }
        return true;
    }

    public void viewLocationDetail() {
        if (location.getParentId() != null) {
            this.parentLocation = locationService.loadEntity(Location.class, location.getParentId());
        }
        showLocationPopup = "true";
    }

    public void viewLocationDetailClick(long l) {
        this.parentLocation = locationService.loadEntity(Location.class, l);
        showLocationPopup = "true";
    }

    public LazyDataSupportMapFilterAndObject<Location> getLocationDataModel() {
        return locationDataModel;
    }

    public void setLocationDataModel(LazyDataSupportMapFilterAndObject<Location> locationDataModel) {
        this.locationDataModel = locationDataModel;
    }

    public LocationSearch getLocationSearch() {
        return locationSearch;
    }

    public String getShowLocationPopup() {
        return showLocationPopup;
    }

    public void setShowLocationPopup(String showLocationPopup) {
        this.showLocationPopup = showLocationPopup;
    }

    public void setLocationSearch(LocationSearch locationSearch) {
        this.locationSearch = locationSearch;
    }

    public static LocationController curentInstance() {
        LocationController lc = ControllerUtils.getBean(ControllerName.LOCATION_CONTROLLER);
        return lc;
    }

    public ILocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(ILocationService locationService) {
        this.locationService = locationService;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> suggetLocation(String locationName) {
        return locationService.suggetPlaceLocation(locationName);
    }

    public List<Location> suggetPlace(String locationName) {
        return locationService.suggetPlaceLocation(locationName, null);
    }

    public List<Location> suggetPlaceLocation(String locationName) {
        return locationService.suggetPlaceLocation(locationName, getLocationListSeletedId());
    }

    public static List<SelectItem> instalLocationListItem() {
        List<Location> locations = instalLocationList();
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Location location : locations) {
            items.add(new SelectItem(location.getLocationCode(), location.getLocationName()));
        }
        return items;
    }

    public static SelectItem getSelectItem(String locationCode) {
        List<SelectItem> locations = instalLocationListItem();
        for (SelectItem location : locations) {
            if (((String) location.getValue()).compareTo(locationCode) == 0) {
                return location;
            }
        }
        return null;
    }

    public List<Location> getSuggetionList() {
        return suggetionList;
    }

    public void setSuggetionList(List<Location> suggetionList) {
        this.suggetionList = suggetionList;
    }

    public static LocationController instance() {
        LocationController controller = ControllerUtils.getBean(ControllerName.LOCATION_CONTROLLER);
        return controller;
    }

    public Location getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    public String getLocationListSeletedId() {

        if (this.suggetionList == null || this.suggetionList.size() < 1) {
            return null;
        }
        StringBuilder stb = new StringBuilder();
        List<String> list = new ArrayList<String>();
        for (Location location1 : suggetionList) {
            String[] arr = location1.getPath().split("_");
            for (String string : arr) {
                if (!DataValidator.checkValueInArray(list, string)) {
                    list.add(string);
                    stb.append(string).append(",");
                }
            }
        }
        return stb.deleteCharAt(stb.length() - 1).toString();
    }

}
