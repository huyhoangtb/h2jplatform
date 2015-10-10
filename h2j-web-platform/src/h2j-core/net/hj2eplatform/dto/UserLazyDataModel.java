/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hj2eplatform.iservices.IUserService;
import net.hj2eplatform.models.Users;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author HuyHoang
 */

public class UserLazyDataModel extends LazyDataModel<Users>  implements  java.io.Serializable{

    private IUserService userService;
    public final static String USER_SEARCH_DATA = "ORGANIZATION_SEARCH_DATA";
    public final static String USER_PARENT_PATH = "USER_PARENT_PATH";
    public final static String USER_ORGNAZATION_ID = "USER_ORGNAZATION_ID";
    public final static String USER_USERNAME = "USER_USERNAME";
    public final static String USER_EMAIL = "USER_EMAIL";
    public final static String USER_BITHDAY = "USER_BITHDAY";
    public final static String USER_FULLNAME = "USER_FULLNAME";
    public final static String PARRENT_ID = "PARRENT_ID";
    private Map<String, Object> filters;

    public UserLazyDataModel(IUserService userService) {
        this.userService = userService;

        filters = new HashMap<String, Object>();
    }
    static Logger logger = Logger.getLogger(UserLazyDataModel.class);

    @Override
    public Users getRowData(String rowKey) {
//        System.out.println("rowkey: " + rowKey);
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        Users user = userService.getUserById(new Long(rowKey));
//                organization.getLocationComObj();
        return user;

    }

    @Override
    public Object getRowKey(Users user) {
        return user.getUserId();
    }

    @Override
    public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filters.putAll(this.filters);
        int counter = userService.counter(filters);
        this.setRowCount(counter == 0 ? -1 : counter);
        List<Users> userList = userService.load(first, pageSize, sortField, sortOrder, filters);
        if (userList == null || userList.size() < 1) {
            return null;
        }
        return userService.load(first, pageSize, sortField, sortOrder, filters);
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
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
