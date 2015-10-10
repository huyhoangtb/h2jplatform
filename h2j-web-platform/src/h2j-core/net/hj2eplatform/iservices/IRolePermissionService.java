/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.models.RolePermission;

/**
 *
 * @author HuyHoang
 */
public interface IRolePermissionService extends ILazyDataSupportMapFilter<RolePermission> {

    static final String ROLE_PERMISSION_STATUS = "ROLE_PERMISSION_STATUS";
    static final String PERMISSION_STATUS = "PERMISSION_STATUS";
    static final String ROLE_ID = "ROLE_ID";
    static final String QUERY_PERMISSION_ROLE_TYPE = "QUERY_PERMISSION_ROLE_TYPE";
    static final String TYPE_PERMISSION_IN_ROLE = "TYPE_PERMISSION_IN_ROLE";
    static final String TYPE_PERMISSION_NOT_IN_ROLE = "TYPE_PERMISSION_NOT_IN_ROLE";
    static final String PERMISSION_NAME = "PERMISSION_NAME";
}
