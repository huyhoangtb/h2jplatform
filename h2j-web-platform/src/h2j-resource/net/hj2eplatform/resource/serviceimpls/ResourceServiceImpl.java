/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.serviceimpls;

import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.models.SystemResource;
import net.hj2eplatform.resource.iservices.IResourceService;
import static net.hj2eplatform.resource.iservices.IResourceService.SEARCH_DATA;
import net.hj2eplatform.core.serviceimpls.AbstractService;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Huy Hoang
 */
public class ResourceServiceImpl extends AbstractService<SystemResource> implements IResourceService, java.io.Serializable {

    @Override
    public List<SystemResource> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filter) {
        StringBuilder std = new StringBuilder("select resource from SystemResource resource where resource.resourceId = resource.resourceId ");

        buildQuery(filter, std);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            std.append(" order by resource.createDate DESC, resource.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
             std.append(" order by resource.createDate DESC");
        }
                
        Query query = em.createQuery(std.toString());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int counter(Map<String, Object> filter) {
        StringBuilder counter = new StringBuilder("select count(resource) from SystemResource resource where resource.resourceId = resource.resourceId ");
        buildQuery(filter, counter);
        Query counterQuery = em.createQuery(counter.toString());
        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    public void buildQuery(Map<String, Object> filter, StringBuilder std) {
        String searchData = (String)filter.get(SEARCH_DATA);
        String searchOrg = (String)filter.get(SEARCH_ORG);
        String folderPath = (String)filter.get(SEARCH_FOLDER_PATH);
        if (searchData != null) {
            std.append("  and (resource.fileName like '%").append(searchData).append("%'")
                   
                    .append(" or resource.metaAlt like '%").append(searchData).append("%')");
        }
        if (searchOrg != null) {
            std.append(" and resource.rootOrgId=").append(searchData);
        }
        if (folderPath != null && folderPath.trim().compareTo("") != 0) {
            std.append(" and resource.folderPath='").append(folderPath).append("'");;
        }
    }

    public List<SystemResource> getSystemResourceList(String resourceArrId) {
        String sql = " select sr from  SystemResource sr where sr.resourceId in (" + resourceArrId + ")";
        Query query = em.createQuery(sql);
        return query.getResultList();
    }
    
    @Override
    public SystemResource getRowKey(String rowKey) {
        if (rowKey == null || rowKey.trim().compareTo("") == 0) {
            return null;
        }
        return (SystemResource) loadEntity(SystemResource.class, Long.valueOf(rowKey));
    }
}
