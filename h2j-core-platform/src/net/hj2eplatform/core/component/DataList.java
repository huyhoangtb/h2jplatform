package net.hj2eplatform.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.DataModel;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

public class DataList extends org.primefaces.component.datalist.DataList implements java.io.Serializable {

    public boolean isLazy() {
        return getDataModel() instanceof LazyDataModel;
    }

    public void loadLazyData() {
        DataModel<?> lazyModel = getDataModel();
//        LazyDataSupportMapFilter<?> lazyModel = (LazyDataSupportMapFilter<?>) getDataModel();

        int row = getRows();
        if (lazyModel instanceof LazyDataSupportMapFilter<?>) {
            LazyDataSupportMapFilter<?> lazyModelExe = (LazyDataSupportMapFilter<?>) getDataModel();
            Map m = lazyModelExe.getFilters();
            if (m == null) {
                m = new HashMap<String, String>();
            }
            List<?> data = lazyModelExe.load(getFirst(), row, null, null, m);

            lazyModelExe.setPageSize(row);
            lazyModelExe.setWrappedData(data);
        } else {
             LazyDataSupportMapFilterAndObject<?> lazyModelExe = (LazyDataSupportMapFilterAndObject<?>) getDataModel();
            Map m = lazyModelExe.getFilters();
            if (m == null) {
                m = new HashMap<String, String>();
            }
            List<?> data = lazyModelExe.load(getFirst(), row, null, null, m);

            lazyModelExe.setPageSize(row);
            lazyModelExe.setWrappedData(data);
        }

        // Update paginator for callback
        if (this.isPaginator()) {
            RequestContext requestContext = RequestContext.getCurrentInstance();

            if (requestContext != null) {
                requestContext.addCallbackParam("totalRecords", lazyModel.getRowCount());
            }
        }
    }
}
