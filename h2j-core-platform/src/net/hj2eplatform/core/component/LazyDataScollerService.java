/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilterAndObject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class LazyDataScollerService<T> extends LazyDataModel<T> implements  java.io.Serializable {

    private ILazyDataSupportMapFilterAndObject<T> lazyService;
    private Map<String, Object> filters;
    private Object searchObject;

    public LazyDataScollerService(ILazyDataSupportMapFilterAndObject<T> lazyService) {
        this.lazyService = lazyService;

        filters = new HashMap<String, Object>();
    }   

    @Override
    public Object getRowKey(T t) {
        if (t == null) {
            return null;
        }
        return ((LazyObject) t).getObjectKey();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filter) {
        List<T> tobjectList = lazyService.load(first, pageSize, sortField, sortOrder, filters, searchObject);
        if (tobjectList == null || tobjectList.size() < 1) {
            return null;
        }
        return tobjectList;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public <E> E getSearchObject() {
        if (searchObject == null) {
            return null;
        }
        return (E) searchObject;
    }

    public void setSearchObject(Object searchObject) {
        this.searchObject = searchObject;
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
