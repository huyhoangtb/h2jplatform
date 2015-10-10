/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.dto.UserPermissionDto;
import net.hj2eplatform.iservices.IUserPermissionServiceDto;
import static net.hj2eplatform.iservices.IUserPermissionServiceDto.ORG_ID;


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
public class UserPermissionServiceDtoImpl extends AbstractService<UserPermissionDto> implements IUserPermissionServiceDto, java.io.Serializable {

    static Logger logger = Logger.getLogger(UserPermissionServiceDtoImpl.class);

    @Override
    public List<UserPermissionDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        String strUserId = (String)(String)filters.get(USER_ID);

        String queryType = (String)(String)filters.get(QUERY_PERMISSION_USER_TYPE);
        StringBuffer querySql = null;

        if (queryType.compareTo(TYPE_PERMISSION_IN_USER) == 0) {
            if (strUserId == null) {
                return null;
            }
            querySql = this.buildPermisionInUser(strUserId);
            appendPermissionSearch(filters, querySql);
        } else if (queryType.compareTo(TYPE_PERMISSION_NOT_IN_USER) == 0) {
            querySql = this.buildPermissionNotInUser(strUserId, filters);
        }


        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            querySql.append(" order by  p.").append(sortField).append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }
        Query query = em.createNativeQuery(querySql.toString(), UserPermissionDto.class);
        return query.getResultList();
    }

    @Override
    public int counter(Map<String, Object> filters) {
        String strUserId = (String)(String)filters.get(USER_ID);
        String queryType = (String)(String)filters.get(QUERY_PERMISSION_USER_TYPE);
        StringBuffer querySql = null;


        if (queryType.compareTo(TYPE_PERMISSION_IN_USER) == 0) {
            if (strUserId == null) {
                return 0;
            }
            querySql = this.countPermisionInUser(strUserId);
            appendPermissionSearch(filters, querySql);
        } else if (queryType.compareTo(TYPE_PERMISSION_NOT_IN_USER) == 0) {
            querySql = this.countPermissionNotInUser(strUserId, filters);
        }

        Query query = em.createNativeQuery(querySql.toString());
        return ((Long) query.getSingleResult()).intValue();
    }

    private StringBuffer buildPermissionNotInUser(String forUserId, Map<String, Object> filter) {
        if (forUserId == null) {
            return new StringBuffer("Select * from permission where status = 1");
        }
        String strOrgId = (String)filter.get(ORG_ID);
        StringBuffer buffer = new StringBuffer("select * from permission p  WHERE p.status = 1  ");
        this.appendPermissionSearch(filter, buffer);
        buffer.append(sqlRejectPermissionInUser(forUserId, strOrgId));
        return buffer;
    }

    private StringBuffer countPermissionNotInUser(String forUserId, Map<String, Object> filter) {
        if (forUserId == null) {
            return new StringBuffer("Select count(*) from permission where status = 1");
        }
        String strOrgId = (String)filter.get(ORG_ID);
        StringBuffer buffer = new StringBuffer("select count(*) from permission p WHERE p.status = 1 ");
        this.appendPermissionSearch(filter, buffer);
        buffer.append(sqlRejectPermissionInUser(forUserId, strOrgId));
        return buffer;
    }



    private String sqlRejectPermissionInUser(String userId, String orgId) {
        String str = " and not EXISTS "
                + " (select 1 from user_permission up INNER JOIN permission p1 ON up.permission_id = p1.permission_id where p.permission_id = p1.permission_id and up.user_id= " + userId + ") "
                + " and not EXISTS  "
                + " (select 1 from role_permission rp inner join user_role ur on rp.role_id = ur.role_id "
                + " inner JOIN permission p2 on rp.permission_id = p2.permission_id where p.permission_id = p2.permission_id and ur.user_id=" + userId + ") ";
        if (orgId != null) { // phong sau nay su dung admin khong thuoc 1 to chuc nao
            str = str + " and not EXISTS  "
                    + " (SELECT 1 from role_permission rp inner join org_role orr on rp.role_id = orr.role_id "
                    + " inner join permission p3 on  rp.permission_id = p3.permission_id and orr.organization_id = " + orgId + ") "
                    + " and not EXISTS  "
                    + " (select 1 from org_permission op inner JOIN permission p4 on op.permission_id = p4.permission_id and op.organization_id= " + orgId + ") ";
        }
        return str;

    }
    private StringBuffer buildPermisionInUser(String userId) {
        StringBuffer sql = new StringBuffer("SELECT up.user_permission_id ,up.user_id, p.permission_id, p.permission_code, p.permission_name, p.full_control, p.comments, up.allow_view, up.allow_edit, up.allow_delete, up.status "
                + "from user_permission up inner join permission p on up.permission_id = p.permission_id"
                + " and up.user_id = ").append(userId);
        return sql;
    }
    private StringBuffer countPermisionInUser(String userId) {

        StringBuffer sql = new StringBuffer("select count(*) from user_permission up INNER JOIN permission p "
                + " on up.permission_id = p.permission_id "
                + " WHERE p.status = 1 and up.user_id = ").append(userId);
        return sql;
    }

    @Override
    public UserPermissionDto getRowKey(String rowKey) {
        try {
            if (rowKey == null || rowKey.trim().compareTo("") == 0 || rowKey.compareTo("null") == 0) {
                return null;
            }
            String[] str = rowKey.split(" ");
            String sql = null;
            if (str.length == 2) {
                sql = " SELECT up.user_permission_id ,up.user_id, p.permission_id, p.permission_code, p.permission_name, p.full_control, p.comments, up.allow_view, up.allow_edit, up.allow_delete, up.status "
                        + " from user_permission up INNER JOIN permission p "
                        + " on up.permission_id = p.permission_id "
                        + " WHERE p.status = 1 and p.permission_id = " + str[0]
                        + " and up.user_id = " + str[1];
                
            } else {
                sql = " select * from permission pe where pe.permission_id = " + str[0];
            }
            Query query = em.createNativeQuery(sql, UserPermissionDto.class);
            return (UserPermissionDto) query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void appendPermissionSearch(Map<String, Object> filter, StringBuffer sql) {
        String permissionCode = (String)filter.get(PERMISSION_CODE);
        String permissionName = (String)filter.get(PERMISSION_NAME);
        String rolePermissionStatus = (String)filter.get(USER_PERMISSION_STATUS);
        System.out.println("rolePermissionStatus: " + rolePermissionStatus);
        String permissionStatus = (String)filter.get(PERMISSION_STATUS);
        if (permissionCode != null && permissionCode.trim().compareTo("") != 0) {
            sql.append(" and p.permission_code = '").append(permissionCode).append("'");
        }
        if (permissionName != null && permissionName.trim().compareTo("") != 0) {
            sql.append(" and p.permission_name like '%").append(permissionName).append("%'");
        }
        // truong hop co status chi ho tro tim kiem voi permission co trong nhom quyen
        if (rolePermissionStatus != null && rolePermissionStatus.trim().compareTo("-1") != 0) {
            sql.append(" and up.status = ").append(rolePermissionStatus);
        }
        if (permissionStatus != null && permissionStatus.trim().compareTo("-1") != 0) {
            sql.append(" and p.status = ").append(rolePermissionStatus);
        }
    }
}
