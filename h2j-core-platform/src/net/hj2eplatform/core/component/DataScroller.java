package net.hj2eplatform.core.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;

public class DataScroller extends org.primefaces.component.datascroller.DataScroller  implements  java.io.Serializable{

    public boolean isLazy() {
        return getDataModel() instanceof LazyDataModel;
    }

    public void loadLazyData() {
        LazyDataScollerService<?> lazyModel = (LazyDataScollerService<?>) getDataModel();
        int row = getChunkSize();
        Map m = lazyModel.getFilters();
        if (m == null) {
            m = new HashMap<String, String>();
        }
        
        List<?> data = lazyModel.load(getFirst(), row, null, null, m);
        lazyModel.setWrappedData(data);
//super.
    }
}
