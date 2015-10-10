/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.dto.RolePermissionDto;

/**
 *
 * @author HuyHoang
 */
public interface IRolePermissionServiceDto extends ILazyDataSupportMapFilter<RolePermissionDto> {

    public static final String ROLE_PERMISSION_STATUS = "ROLE_PERMISSION_STATUS";
    public static final String ROLE_ID = "ROLE_ID";
    
    public static final String QUERY_PERMISSION_ROLE_TYPE = "QUERY_PERMISSION_ROLE_TYPE";
    public static final String TYPE_PERMISSION_IN_ROLE = "TYPE_PERMISSION_IN_ROLE";
    public static final String TYPE_PERMISSION_NOT_IN_ROLE = "TYPE_PERMISSION_NOT_IN_ROLE";

    public static final String PERMISSION_CODE = "PERMISSION_CODE";
    public static final String PERMISSION_NAME = "PERMISSION_NAME";
}
