/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hj2eplatform.iservices.IPermissionService;
import net.hj2eplatform.models.Permission;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author HuyHoang
 */

public class PermissionLazyDataModel extends LazyDataModel<Permission>  implements  java.io.Serializable{

    private IPermissionService pesmissionService;
    public static final String PERMISSION_CODE = "PERMISSION_CODE";
    public static final String PERMISSION_NAME = "PERMISSION_NAME";
    public static final String PERMISSION_STATUS = "PERMISSION_STATUS";
    private Map<String, String> filters;

    public PermissionLazyDataModel(IPermissionService pesmissionService) {
        this.pesmissionService = pesmissionService;

        filters = new HashMap<String, String>();
    }
    static Logger logger = Logger.getLogger(PermissionLazyDataModel.class);

    @Override
    public Permission getRowData(String rowKey) {
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        Permission permission = pesmissionService.getPermissionByCode(rowKey);
        return permission;

    }

    @Override
    public Object getRowKey(Permission permission) {
        return permission.getPermissionCode();
    }

    @Override
    public List<Permission> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filters.putAll(this.filters);
        int counter = pesmissionService.counter(filters);
        this.setRowCount(counter == 0 ? -1 : counter);
        List<Permission> permissionList = pesmissionService.load(first, pageSize, sortField, sortOrder, filters);
        if (permissionList == null || permissionList.size() < 1) {
            return null;
        }
        return pesmissionService.load(first, pageSize, sortField, sortOrder, filters);
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    @Override
    public void setRowIndex(final int rowIndex) {
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }
}
