package net.hj2eplatform.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

public class DataGrid extends org.primefaces.component.datagrid.DataGrid implements  java.io.Serializable {

    public boolean isLazy() {
        return getDataModel() instanceof LazyDataModel;
    }

    public void loadLazyData() {
        LazyDataSupportMapFilter<?> lazyModel = (LazyDataSupportMapFilter<?>) getDataModel();
        int row = getRows();
        Map m = lazyModel.getFilters();
        if (m == null) {
            m = new HashMap<String, String>();
        }
        List<?> data = lazyModel.load(getFirst(), row, null, null, m);
        lazyModel.setPageSize(getRows());
        lazyModel.setWrappedData(data);

        // Update paginator for callback
        if (this.isPaginator()) {
            RequestContext requestContext = RequestContext.getCurrentInstance();

            if (requestContext != null) {
                requestContext.addCallbackParam("totalRecords", lazyModel.getRowCount());
            }
        }
    }
}
