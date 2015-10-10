package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.hj2eplatform.dto.LocationSearch;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.models.Location;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HoangNH
 */
@Repository
@Transactional
public class LocationServiceImpl extends AbstractService<Location> implements ILocationService, java.io.Serializable {

    @PersistenceContext
    protected EntityManager em;
    static Logger logger = Logger.getLogger(LocationServiceImpl.class);

    @Override
    public List<Location> getLocationList(Long parentId) {
        if (parentId == null) {
            return this.getNationalList();
        }
        return getLocationListByParent(parentId);
    }

    private List<Location> getNationalList() {
        String sql = "select l from Location l where l.parentId is null order by l.type, l.orderShow, l.locationName";
        Query query = em.createQuery(sql);
        return query.getResultList();
    }

    private List<Location> getLocationListByParent(Long parentId) {
        String sql = "Select l from Location l where l.parentId = :parentId and l.status = 1 order by l.type, l.orderShow, l.locationName";
        Query query = em.createQuery(sql).setParameter("parentId", parentId);
        return query.getResultList();
    }

    public List<Location> getLocationByParentList(String parentCode) {
        String sql = "Select l.* from location l inner join location l2 on l.parent_id = l2.location_id and l2.location_Code = '" + parentCode + "'"
                + " where   l.status = 1 order by l.type, l.order_Show, l.location_Name";
        Query query = em.createNativeQuery(sql, Location.class);
        return query.getResultList();
    }
    /*
     * Sugget dia danh du lich
     */

    public Location getLocation4Converter(String locationName) {
        StringBuilder std = new StringBuilder("Select l from Location l where ");

        std.append(" ( l.locationName like '").append(locationName).append("') and l.status = 1 ");
        Query query = em.createQuery(std.toString());
        query.setFirstResult(0);
        query.setMaxResults(2);
        List<Location> locations = query.getResultList();
        if (locations == null || locations.isEmpty() || locations.size() > 1) {
            return null;
        }
        return locations.get(0);
    }

    public List<Location> suggetPlaceLocation(String locationName, String selectedIdArr) {
        StringBuilder std = new StringBuilder("Select l from Location l where ");

        std.append(" ( l.locationName like '%").append(locationName).append("%'");
        std.append(" or l.locationCode = '").append(locationName).append("'");
        std.append(" ) and l.status = 1 ");
        if (selectedIdArr != null && selectedIdArr.compareTo("") != 0) {
            std.append(" and l.locationId not in (").append(selectedIdArr).append(")");
        }
        std.append(" order by l.type desc, l.orderShow, l.locationName, l.locationCode");
        Query query = em.createQuery(std.toString());
        return query.getResultList();
    }
    /*
     * Sugget dia danh du lich
     */

    public List<Location> suggetPlaceLocation(String locationName) {
        StringBuilder std = new StringBuilder("Select l from Location l where ");
        std.append("( l.locationName like '%").append(locationName).append("%'");
        std.append("or l.locationCode = '").append(locationName).append("'");
        std.append(" ) and l.status = 1 order by l.type desc, l.orderShow, l.locationName, l.locationCode");
        Query query = em.createQuery(std.toString());
        return query.getResultList();
    }

    public List<Location> suggetLocation(String locationName) {
        StringBuilder std = new StringBuilder("Select l from Location l where ");
        std.append("( l.locationName like '%").append(locationName).append("%'");
        std.append("or l.locationCode = '").append(locationName).append("'");
        std.append(" ) and l.status = 1 order by l.type, l.orderShow, l.locationName, l.locationCode");
        Query query = em.createQuery(std.toString());
        return query.getResultList();
    }

    public List<Long> findChildLocationId(Long parrentId) {
        Location location = this.loadEntity(Location.class, parrentId);
        if (location == null) {
            return null;
        }
        String sql = "select location_id from location where path LIKE '" + location.getPath() + "%'";
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }

    public String getPathLocation(Long locationId) {
        if (locationId == null) {
            return null;
        }
        try {
            String sql = "select l from Location l where l.locationId = :locationId";
            Query query = em.createQuery(sql).setParameter("locationId", locationId);
            Location location = (Location) query.getSingleResult();
            return location.getPath().replaceAll("_", "|_");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Location> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, Object object) {
        StringBuilder sql = new StringBuilder("select l.* from location l where 1=1");
        buildSql(filters, object, sql);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  l.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
            sql.append(" order by l.order_show, l.location_name ");
        }

        Query query = em.createNativeQuery(sql.toString(), Location.class);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int counter(Map<String, Object> filter, Object object) {
        StringBuilder sql = new StringBuilder("SELECT count(*)  from location l  where 1=1 ");
        buildSql(filter, object, sql);
        Query counterQuery = em.createNativeQuery(sql.toString());

        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    public void buildSql(Map<String, Object> filter, Object object, StringBuilder sql) {
        LocationSearch locationSearch = (LocationSearch) object;
        if (locationSearch.getLocationType() != null && locationSearch.getLocationType() > 0) {
            sql.append(" and l.type = ").append(locationSearch.getLocationType());
        } else if (locationSearch.getLocationType() == null) {
            sql.append(" and l.type = 1");
        }
        if (locationSearch.getParentLocation() != null) {
            sql.append(" and l.parent_id = ").append(locationSearch.getParentLocation().getLocationId());
        }
        if (locationSearch.getLocationCode() != null && locationSearch.getLocationCode().trim().compareTo("") != 0) {
            sql.append(" and l.location_code = '").append(locationSearch.getLocationCode()).append("'");
        }
        if (locationSearch.getLocationName() != null && locationSearch.getLocationName().trim().compareTo("") != 0) {
            sql.append(" and l.location_name like '%").append(locationSearch.getLocationName()).append("%'");
        }
    }

    @Override
    public Location getRowKey(String rowKey) {
        if (rowKey == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder("select l.* from location l where location.location_id = ").append(rowKey);
        Query query = em.createNativeQuery(sql.toString(), Location.class);
        return (Location) query.getSingleResult();
    }

    @Override
    public Location getDetailLocation(String locationID) {
        StringBuilder sql = new StringBuilder("select l.* from location l where l.location_id = ").append(locationID);
        Query query = em.createNativeQuery(sql.toString(), Location.class);
        return (Location) query.getSingleResult();
    }

    @Override
    public List<Location> getListPathLocation(String pathID) {
        StringBuilder sql = new StringBuilder("select l.* from location l where l.location_id in ").append("(" + pathID + ")");
        Query query = em.createNativeQuery(sql.toString(), Location.class);
        return (List<Location>) query.getResultList();
    }

    @Override
    public List<Location> getListLocationByOrg(Long orgId) {
        String sql = "SELECT location.location_id, location.location_name"
                + " FROM location"
                + " INNER JOIN visit_plance ON location.location_id = visit_plance.location_id"
                + " WHERE visit_plance.object_type = 4  AND visit_plance.object_id = ?1";
        Query query = em.createNativeQuery(sql, Location.class);
        query.setParameter(1, orgId);
        return query.getResultList();
    }

}
