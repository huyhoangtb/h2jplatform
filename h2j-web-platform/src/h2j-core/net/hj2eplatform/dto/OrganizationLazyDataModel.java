/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Organization;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author HuyHoang
 */
public class OrganizationLazyDataModel extends LazyDataModel<Organization>  implements  java.io.Serializable{

    private IOrganizationService organizationService;
    public final static String ORGANIZATION_SEARCH_DATA = "ORGANIZATION_SEARCH_DATA";
    public final static String ORGANIZATION_SEARCH_NATIONAL_ID = "ORGANIZATION_SEARCH_NATIONAL_ID";
    public final static String ORGANIZATION_SEARCH_PROVINCE_ID = "ORGANIZATION_SEARCH_PROVINCE_ID";
    public final static String PARRENT_ID = "PARRENT_ID";
    public final static String IS_MANAGER_MY_ORG = "MANAGER_MY_ORG";
    private Map<String, String> filters;

    public OrganizationLazyDataModel(IOrganizationService organizationService) {
        this.organizationService = organizationService;

        filters = new HashMap<String, String>();
    }
    static Logger logger = Logger.getLogger(OrganizationLazyDataModel.class);

    @Override
    public Organization getRowData(String rowKey) {
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        Organization organization = organizationService.getOrganizationByCode(rowKey);
        return organization;
    }

    @Override
    public Object getRowKey(Organization organization) {
        return organization.getOrganizationCode();
    }

    @Override
    public List<Organization> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filters.putAll(this.filters);
        int counter = organizationService.counter(filters);
        this.setRowCount(counter == 0 ? -1 : counter);
        List<Organization> organizations = organizationService.load(first, pageSize, sortField, sortOrder, filters);
        if (organizations == null || organizations.size() < 1) {
            return null;
        }
        return organizationService.load(first, pageSize, sortField, sortOrder, filters);
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
