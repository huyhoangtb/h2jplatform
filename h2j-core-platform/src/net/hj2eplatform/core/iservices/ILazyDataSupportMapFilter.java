package net.hj2eplatform.core.iservices;

import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
@Repository
@Transactional
public interface ILazyDataSupportMapFilter<T> extends IAbstractService<T> {

    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);

    public int counter(Map<String, Object> filters);
    public T getRowKey(String rowKey);
}
