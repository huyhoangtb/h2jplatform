/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import java.util.List;
import net.hj2eplatform.dto.OrgPartnerDto;

/**
 *
 * @author HuyHoang
 */
public interface IOrgPartnerService extends ILazyDataSupportMapFilter<OrgPartnerDto> {

    public final static String ORGANIZATION_SEARCH_DATA = "ORGANIZATION_SEARCH_DATA";
    public final static String ORGANIZATION_SEARCH_NATIONAL_ID = "ORGANIZATION_SEARCH_NATIONAL_ID";
    public final static String ORGANIZATION_SEARCH_PROVINCE_ID = "ORGANIZATION_SEARCH_PROVINCE_ID";
    public final static String ORGANIZATION_TYPE = "ORGANIZATION_TYPE";
    public final static String PARRENT_ID = "PARRENT_ID";
    public final static String DEPUTY_NAME = "deputyName";

    public OrgPartnerDto loadOrgPartner(String orgId);
    
    public List<OrgPartnerDto> listOrgParterDto(Object object);

}
