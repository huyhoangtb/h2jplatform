/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.iservices.IRoleService;
import net.hj2eplatform.models.Role;
import net.hj2eplatform.models.UserRole;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements IRoleService, java.io.Serializable {

    static Logger logger = Logger.getLogger(RoleServiceImpl.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Role> getRole4Complete(String completeInput, Long userId) {
        try {
            String sql = "Select role.* from role role where role.status=1 and upper(role.role_name) like '%" + completeInput.toUpperCase() + "%'";
            if(userId != null) {
                 sql = sql + " and not exists "
                    + " (select 1 from user_role ur where ur.role_id = role.role_id and ur.user_id = " + userId + ")";
            }
                    
                   
            Query query = em.createNativeQuery(sql, Role.class);
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Role getRoleByName(String roleName) {
        try {
            String sql = "Select role from Role role where role.roleName = :roleName";
            Query query = em.createQuery(sql).setParameter("roleName", roleName);
            return (Role) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Role> getRole4User(Long userId) {
        String sql = "select r.* from role r inner join user_role ur on r.role_id = ur.role_id where r.status = 1 and ur.user_id = ?1";
        Query query = em.createNativeQuery(sql, Role.class).setParameter(1, userId);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeUserRole(Long userId, Long roleId) {
        String sql = "delete  from UserRole ur where ur.userId = :userId and ur.roleId = :roleId";
        Query query = em.createQuery(sql).setParameter("userId", userId).setParameter("roleId", roleId);
        System.out.println("delete role: " + query.executeUpdate());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<UserRole> getUserRoleList(Long userId) {
        String sql = "select ur from UserRole ur where ur.userRoleId = :userRoleId";
        Query query = em.createQuery(sql).setParameter("userRoleId", userId);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Role> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select role from Role role  where role.roleId = role.roleId ");
        builddingQuery(sql, filters);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  role.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }

        Query query = em.createQuery(sql.toString());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(role) from Role role  where role.roleId = role.roleId ");


        Query counterQuery = em.createQuery(builddingQuery(counter, filters).toString());
        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    private StringBuffer builddingQuery(StringBuffer sql, Map<String, Object> filters) {
        String roleName = (String)filters.get(ROLE_NAME);
        String roleStatus = (String)filters.get(ROLE_STATUS);
        String roleType = (String)filters.get(ROLE_TYPE);
        String organizationId = (String)filters.get(ROLE_ORGANIZATION_ID);
        System.out.println("rolename: " + roleName);
        if (roleName != null && roleName.compareTo("") != 0) {
            sql.append(" and upper(role.roleName) like '%").append(roleName.toUpperCase()).append("%'");
        }
        if (roleStatus != null) {
            sql.append(" and role.status = ").append(roleStatus);
        }
        if (roleType != null) {
            sql.append(" and role.roleType = ").append(roleStatus);
        }

        if (organizationId != null) {
            sql.append(" and role.organizationId = ").append(organizationId).append("");
        }
//        User user = (User) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        if (user != null) {
//            sql.append(" and per.organizationId = ").append(user.getOrganizationId()).append("");
//        }


        return sql;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Role getRowKey(String rowKey) {
        try {
            String sql = "Select role from Role role where role.roleId = :roleId";
            Query query = em.createQuery(sql).setParameter("roleId", Long.valueOf(rowKey));
            return (Role) query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
