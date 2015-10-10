/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilterAndObject;
import java.util.List;
import net.hj2eplatform.models.Location;

/**
 *
 * @author HuyHoang
 */
public interface ILocationService extends ILazyDataSupportMapFilterAndObject<Location> {

    public List<Location> getLocationList(Long parentId);

    public List<Location> getLocationByParentList(String parentCode);

    public List<Location> suggetLocation(String locationName);

    public List<Location> suggetPlaceLocation(String locationName);

    public List<Location> suggetPlaceLocation(String locationName, String selectedIdArr);

    public List<Long> findChildLocationId(Long parrentId);

    public String getPathLocation(Long locationId);

    public Location getLocation4Converter(String locationName);
    
    public Location getDetailLocation(String locationID);
    
    public List<Location> getListPathLocation(String pathID);
    
    public List<Location> getListLocationByOrg(Long orgId);
}
