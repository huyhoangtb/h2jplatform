/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.dto.RolePermissionDto;
import net.hj2eplatform.iservices.IRolePermissionServiceDto;
import static net.hj2eplatform.iservices.IRolePermissionServiceDto.PERMISSION_CODE;
import static net.hj2eplatform.iservices.IRolePermissionServiceDto.QUERY_PERMISSION_ROLE_TYPE;
import static net.hj2eplatform.iservices.IRolePermissionServiceDto.ROLE_ID;
import static net.hj2eplatform.iservices.IRolePermissionServiceDto.TYPE_PERMISSION_IN_ROLE;
import static net.hj2eplatform.iservices.IRolePermissionServiceDto.TYPE_PERMISSION_NOT_IN_ROLE;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class RolePermissionServiceDtoImpl extends AbstractService<RolePermissionDto> implements IRolePermissionServiceDto, java.io.Serializable {

    static Logger logger = Logger.getLogger(RolePermissionServiceDtoImpl.class);

    @Override
    public List<RolePermissionDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        String strRoleId = (String)(String)filters.get(ROLE_ID);
//        System.out.println("roleId   " + strRoleId);
        String queryType = (String)(String)filters.get(QUERY_PERMISSION_ROLE_TYPE);
        StringBuffer querySql = null;
        Long roleId = null;
        if (strRoleId != null) {
            roleId = Long.valueOf(strRoleId);
        }
        if (queryType.compareTo(TYPE_PERMISSION_IN_ROLE) == 0) {
            if (roleId == null) {
                return null;
            }
            querySql = this.buildPermisionInRole(roleId);
            appendPermissionSearch(filters, querySql);
        } else if (queryType.compareTo(TYPE_PERMISSION_NOT_IN_ROLE) == 0) {
            querySql = this.buildPermissionNotInRole(roleId, filters);
        }


        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            querySql.append(" order by  p.").append(sortField).append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }
        Query query = em.createNativeQuery(querySql.toString(), RolePermissionDto.class);
        return query.getResultList();
    }

    @Override
    public int counter(Map<String, Object> filters) {
        String strRoleId = (String)(String)filters.get(ROLE_ID);
        String queryType = (String)(String)filters.get(QUERY_PERMISSION_ROLE_TYPE);
        StringBuffer querySql = null;
        Long roleId = null;
        if (strRoleId != null) {
            roleId = Long.valueOf(strRoleId);
        }
        if (queryType.compareTo(TYPE_PERMISSION_IN_ROLE) == 0) {
            if (roleId == null) {
                return 0;
            }
            querySql = this.countPermisionInRole(roleId);
            appendPermissionSearch(filters, querySql);
        } else if (queryType.compareTo(TYPE_PERMISSION_NOT_IN_ROLE) == 0) {
            querySql = this.countPermissionNotInRole(roleId, filters);
        }

        Query query = em.createNativeQuery(querySql.toString());
        return ((Long)  query.getSingleResult()).intValue();
    }

    private StringBuffer buildPermissionNotInRole(Long forRoleId, Map<String, Object> filter) {
        if (forRoleId == null) {
            return new StringBuffer("Select * from permission where status = 1");
        }
        StringBuffer buffer = new StringBuffer("select * from permission p "
                + " WHERE p.status = 1  ");
        this.appendPermissionSearch(filter, buffer);
        buffer.append(" and not EXISTS (select 1 from role_permission rp2 inner join permission p1 "
                + " on rp2.permission_id = p1.permission_id "
                + " WHERE p1.permission_id = p.permission_id and rp2.role_id =").append(forRoleId).append(")");
        return buffer;
    }

    private StringBuffer countPermissionNotInRole(Long forRoleId, Map<String, Object> filter) {
        if (forRoleId == null) {
            return new StringBuffer("Select count(*) from permission where status = 1");
        }
        StringBuffer buffer = new StringBuffer("select count(*) from permission p "
                + " WHERE p.status = 1 ");
        this.appendPermissionSearch(filter, buffer);
        buffer.append(" and not EXISTS (select 1 from role_permission rp2 inner join permission p1 "
                + " on rp2.permission_id = p1.permission_id "
                + " WHERE p1.permission_id = p.permission_id and rp2.role_id =").append(forRoleId).append(")");
        return buffer;
    }

    private StringBuffer buildPermisionInRole(Long roleId) {

        StringBuffer sql = new StringBuffer("select rp.role_permission_id,rp.role_id, p.permission_id,p.permission_code, p.permission_name, p.full_control, p.comments, rp.allow_view, rp.allow_edit, rp.allow_delete, rp.status from role_permission rp INNER JOIN permission p "
                + " on rp.permission_id = p.permission_id "
                + " WHERE p.status = 1 and rp.role_id = ").append(roleId);
        return sql;
    }

    private StringBuffer countPermisionInRole(Long roleId) {

        StringBuffer sql = new StringBuffer("select count(*) from role_permission rp INNER JOIN permission p "
                + " on rp.permission_id = p.permission_id "
                + " WHERE p.status = 1 and role_id = ").append(roleId);
        return sql;
    }

    @Override
    public RolePermissionDto getRowKey(String rowKey) {
        try {
            if (rowKey == null || rowKey.trim().compareTo("") == 0 || rowKey.compareTo("null") == 0) {
                return null;
            }
            String[] str = rowKey.split(" ");
            String sql = null;
            if (str.length == 2) {
                sql = "select rp.role_permission_id,rp.role_id, p.permission_id,p.permission_code, p.permission_name, p.full_control, p.comments, rp.allow_view, rp.allow_edit, rp.allow_delete, rp.status from role_permission rp INNER JOIN permission p "
                        + " on rp.permission_id = p.permission_id "
                        + " WHERE p.status = 1 and rp.role_id = " + str[1]
                        + " and rp.permission_id = " + str[0];
            } else {
                sql = " select * from permission pe where pe.permission_id = " + str[0];
            }
            Query query = em.createNativeQuery(sql, RolePermissionDto.class);
            return (RolePermissionDto) query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void appendPermissionSearch(Map<String, Object> filter, StringBuffer sql) {
        String permissionCode = (String)filter.get(PERMISSION_CODE);
        String permissionName = (String)filter.get(PERMISSION_NAME);
        String rolePermissionStatus = (String)filter.get(ROLE_PERMISSION_STATUS);
        if (permissionCode != null && permissionCode.trim().compareTo("") != 0) {
            sql.append(" and p.permission_code = '").append(permissionCode).append("'");
        }
        if (permissionName != null && permissionName.trim().compareTo("") != 0) {
            sql.append(" and p.permission_name like '%").append(permissionName).append("%'");
        }
        // truong hop co status chi ho tro tim kiem voi permission co trong nhom quyen
        if (rolePermissionStatus != null && rolePermissionStatus.trim().compareTo("-1") != 0) {
            sql.append(" and rp.status = ").append(rolePermissionStatus);
        }
    }
}
