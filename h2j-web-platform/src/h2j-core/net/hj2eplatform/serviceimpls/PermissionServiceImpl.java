/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.dto.PermissionLazyDataModel;
import net.hj2eplatform.iservices.IPermissionService;

import net.hj2eplatform.models.Permission;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission> implements IPermissionService, java.io.Serializable {

    static Logger logger = Logger.getLogger(PermissionServiceImpl.class);

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Permission getPermissionByName(String permissionName) {
        try {
            String sql = "Select per from Permission per where per.permissionName = :permissionName";
            Query query = em.createQuery(sql).setParameter("permissionName", permissionName);
            return (Permission) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Permission getPermissionByCode(String permissionCode) {
        try {
            String sql = "Select per from Permission per where per.permissionCode = :permissionCode";
            Query query = em.createQuery(sql).setParameter("permissionCode", permissionCode);
            return (Permission) query.getSingleResult();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public List<Permission> getOrganizationList(Permission searchInput) {
        String sql = "select per from Permission per where per.permissionCode = :permissionCode and per.permissionName = :permissionName "
                + "and per.status = :status";
        Query query = em.createQuery(sql);
        query.setParameter("permissionCode", searchInput.getPermissionCode());
        query.setParameter("permissionName", searchInput.getPermissionName());
        query.setParameter("status", searchInput.getStatus());
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Permission> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select per from Permission per  where per.permissionId = per.permissionId ");
        builddingQuery(sql, filters);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  per.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } 

        Query query = em.createQuery(sql.toString());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(per) from Permission per where per.permissionId = per.permissionId  ");


        Query counterQuery = em.createQuery(builddingQuery(counter, filters).toString());
        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    private StringBuffer builddingQuery(StringBuffer sql, Map<String, Object> filters) {
        String permissionCode = (String)filters.get(PermissionLazyDataModel.PERMISSION_CODE);
        String permissionName = (String)filters.get(PermissionLazyDataModel.PERMISSION_NAME);
        String permissionStatus = (String)filters.get(PermissionLazyDataModel.PERMISSION_STATUS);
        if (permissionCode != null && permissionCode.compareTo("") != 0) {
            sql.append(" and upper(per.permissionCode) = '").append(permissionCode.toUpperCase()).append("'");
        }
        if (permissionStatus != null) {
            sql.append(" and per.status = '").append(permissionStatus).append("'");
        }

        if (permissionName != null && permissionName.compareTo("") != 0) {
            sql.append(" and per.permissionName like '%").append(permissionName).append("%'");
        }
        return sql;
    }

    @Override
    public Permission getRowKey(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
