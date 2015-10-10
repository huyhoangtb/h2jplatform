/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import java.util.List;
import net.hj2eplatform.models.Role;
import net.hj2eplatform.models.UserRole;

/**
 *
 * @author HuyHoang
 */
public interface IRoleService extends ILazyDataSupportMapFilter<Role> {

    public static final String ROLE_NAME = "ROLE_NAME";
    public static final String ROLE_STATUS = "ROLE_STATUS";
    public static final String ROLE_TYPE = "ROLE_TYPE";
    public static final String ROLE_ORGANIZATION_ID = "ROLE_ORGANIZATION_ID";

    public Role getRoleByName(String name);
    public List<Role> getRole4Complete(String completeInput, Long userId);
    public List<UserRole> getUserRoleList(Long userId);
    public void removeUserRole(Long userId, Long roleId);
    public List<Role> getRole4User(Long userId);
}
