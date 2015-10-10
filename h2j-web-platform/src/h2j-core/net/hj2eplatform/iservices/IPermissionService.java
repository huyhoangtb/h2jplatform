/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import java.util.List;
import net.hj2eplatform.models.Permission;

/**
 *
 * @author HuyHoang
 */
public interface IPermissionService extends ILazyDataSupportMapFilter<Permission> {

    public List<Permission> getOrganizationList(Permission searchInput);

    public Permission getPermissionByName(String name);

    public Permission getPermissionByCode(String code);

}
