/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilterAndObject;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class LazyDataSupportMapFilterAndObject<T> extends LazyDataModel<T> implements java.io.Serializable {

    private ILazyDataSupportMapFilterAndObject<T> lazyService;
    private Map<String, String> filters;
    private Object searchObject;

    public LazyDataSupportMapFilterAndObject(ILazyDataSupportMapFilterAndObject<T> lazyService) {
        this.lazyService = lazyService;

        filters = new HashMap<String, String>();
    }
    static Logger logger = Logger.getLogger(LazyDataSupportMapFilterAndObject.class);

    @Override
    public T getRowData(String rowKey) {

        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        // neu tim khong thay trong list hien tai moi truy cap vao co so du lieu lay du lieu
        if (getWrappedData() != null) {
            for (T t : (List<T>) getWrappedData()) {
                if (((LazyObject) t).getObjectKey().compareTo(rowKey) == 0) {
                    return t;
                }
            }
        }

        T t = lazyService.getRowKey(rowKey);
        return t;

    }

    @Override
    public Object getRowKey(T t) {
        if (t == null) {
            return null;
        }
        return ((LazyObject) t).getObjectKey();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filters.putAll(this.filters);
        int counter = lazyService.counter(filters, searchObject);
        this.setRowCount(counter == 0 ? -1 : counter);
        List<T> tobjectList = lazyService.load(first, pageSize, sortField, sortOrder, filters, searchObject);
        if (tobjectList == null || tobjectList.size() < 1) {
            return null;
        }
        return tobjectList;
    }

//    @Override
//    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
//        
//    }
    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
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
