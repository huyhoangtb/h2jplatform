/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.dto.UserPermissionDto;

/**
 *
 * @author HuyHoang
 */
public interface IUserPermissionServiceDto extends ILazyDataSupportMapFilter<UserPermissionDto> {

    public static final String  USER_PERMISSION_STATUS = " USER_PERMISSION_STATUS";
    public static final String  PERMISSION_STATUS = " PERMISSION_STATUS";
    public static final String USER_ID = "USER_ID";
    public static final String ORG_ID = "ORG_ID";
    
    public static final String QUERY_PERMISSION_USER_TYPE = "QUERY_PERMISSION_USER_TYPE";
    public static final String TYPE_PERMISSION_IN_USER = "TYPE_PERMISSION_IN_USER";
    public static final String TYPE_PERMISSION_NOT_IN_USER = "TYPE_PERMISSION_NOT_IN_USER";

    public static final String PERMISSION_CODE = "PERMISSION_CODE";
    public static final String PERMISSION_NAME = "PERMISSION_NAME";
}
