/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.iservices;

import java.util.List;
import java.util.List;
import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.models.SystemResource;

/**
 *
 * @author Huy Hoang
 */
public interface IResourceService extends ILazyDataSupportMapFilter<SystemResource>{
    public static final String SEARCH_DATA = "SEARCH_DATA";
    public static final String SEARCH_ORG = "SEARCH_ORG";
    public static final String SEARCH_FOLDER_PATH = "SEARCH_FOLDER_PATH";
    
    /**
     * resourceId cách nhau bởi dấu phẩy 2132,43242,432432,423423,234
     * @param resourceArrId
     * @return 
     */
    public List<SystemResource> getSystemResourceList(String resourceArrId);
}
