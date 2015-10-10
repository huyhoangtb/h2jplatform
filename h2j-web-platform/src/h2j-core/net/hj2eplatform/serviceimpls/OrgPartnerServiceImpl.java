/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.controller.H2jWebContext;
import net.hj2eplatform.dto.OrgPartnerDto;
import net.hj2eplatform.dto.OrgSearchDto;
import net.hj2eplatform.iservices.IOrgPartnerService;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.utils.OrgType;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class OrgPartnerServiceImpl extends AbstractService<OrgPartnerDto> implements IOrgPartnerService, java.io.Serializable {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);
    private IOrganizationService organizationService;

    public OrgPartnerDto loadOrgPartner(String orgId) {
        try {
            String sql = "select org.*, s.full_name from  organization org left join human s  on org.organization_id = s.organization_id and user_type = 1 where org.organization_id = ?1";
            Query query = em.createNativeQuery(sql, OrgPartnerDto.class);
            query.setParameter(1, orgId);
            return (OrgPartnerDto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<OrgPartnerDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select org.*, s.full_name from  organization org left join human s  on org.organization_id = s.organization_id and user_type = 1 where 1=1 ");// chi lay nguoi dai dien

        builddingQuery(sql, filters);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  org.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
            sql.append(" order by  org.modified_date desc");
        }

        Query query = em.createNativeQuery(sql.toString(), OrgPartnerDto.class);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(*) from  organization org left join human s  on org.organization_id = s.organization_id and user_type = 1 where 1=1 ");// chi lay nguoi dai dien
        Query counterQuery = em.createNativeQuery(builddingQuery(counter, filters).toString());
        return ((java.math.BigDecimal) counterQuery.getSingleResult()).intValue();
    }

    private StringBuffer builddingQuery(StringBuffer sql, Map<String, Object> filters) {
        String data = (String)filters.get(ORGANIZATION_SEARCH_DATA);
        String nationalId = (String)filters.get(ORGANIZATION_SEARCH_NATIONAL_ID);
        String provinceId = (String)filters.get(ORGANIZATION_SEARCH_PROVINCE_ID);
        String viewType = (String)filters.get(ORGANIZATION_TYPE);
        String deputyName = (String)filters.get(DEPUTY_NAME);
        if (data != null && data.compareTo("") != 0) {
            data = data.toUpperCase().trim();
            if (data.startsWith("#")) {
                data = data.substring(1, data.length());
                sql.append(" and upper(org.organization_code) = '").append(data).append("'");
            } else {
                sql.append(" and (upper(org.organization_name) like '%").append(data).append("%'")
                        .append(" or  upper(org.enlish_name) like '%").append(data).append("%'")
                        .append(" or  upper(org.address) like '%").append(data).append("%'")
                        .append(" or  org.phone = '").append(data).append("'")
                        .append(" or  upper(org.email_address) = '").append(data).append("'")
                        .append(" or  upper(org.website) = '").append(data).append("')");
            }
        }

        if (nationalId != null) {
            sql.append(" and org.nationnal_Id = ").append(nationalId);
        }
        if (provinceId != null) {
            sql.append(" and org.province_id = ").append(provinceId);
        }
        if (deputyName != null && deputyName.trim().compareTo("") != 0) {
            sql.append(" and upper(s.full_name) like '%").append(deputyName.trim().toLowerCase()).append("%'");
        }
        int orgType = 0;
        if (viewType.compareTo(H2jWebContext.ORG_CUSTOMER_VIEW_PARRAM) == 0) {
            orgType = OrgType.ORG_CUSTOMER.toInteger();
        } else if (viewType.compareTo(H2jWebContext.ORG_PARTNER_VIEW_PARRAM) == 0) {
            orgType = OrgType.SUPPLIER.toInteger();
        } else if (viewType.compareTo(H2jWebContext.AGENT_PARTNER_VIEW_PARRAM) == 0) {
            orgType = OrgType.AGENCY.toInteger();
        } else {
            orgType = OrgType.ORG_H2J_CENTER.toInteger();
        }
        sql.append(" and org.org_Type = ").append(orgType);

//        AuthenticationController controller = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        // bỏ dong if neu muon thay to chuc con
//        if (controller.getOrganization().getOrgType() != null && controller.getOrganization().getOrgType().intValue() != OrgType.ORG_H2J_CENTER.toInteger()) {
//            sql.append(" and org.root_id = ").append(controller.getOrganization().getRootId());
            sql.append(" and org.group_partner_id =  ").append(AuthenticationController.getCurrentOrg().getRootId());
            sql.append(" and org.organization_id = org.root_id");//.append(AuthenticationController.getCurrentOrg().getRootId());
//        }
        return sql;
    }

    @Override
    public OrgPartnerDto getRowKey(String rowKey) {
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        try {
            StringBuffer sql = new StringBuffer("select org.*, s.full_name from  organization org left join human s  on org.organization_id = s.organization_id and s.user_type = 1 where "
                    + "org.organization_id = ").append(rowKey);

            return (OrgPartnerDto) em.createNativeQuery(sql.toString(), OrgPartnerDto.class).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    @Override
    public List<OrgPartnerDto> listOrgParterDto(Object object) {
        OrgSearchDto orgSearchDto = (OrgSearchDto) object;
        StringBuilder sql = new StringBuilder("SELECT organization.organization_id, organization.organization_name, location.location_name, organization.logo_img_id, "
                + " sr.meta_alt img_meta_alt, sr.meta_title img_meta_title, sr.file_real_name img_file_real_name, sr.folder_path img_folder_path,sr.folder_path, sr.file_real_name "
                + " FROM organization"
                + " left join system_resource sr on organization.logo_img_id = sr.resource_id "
                + " LEFT JOIN location ON location.location_id = organization.province_id"
                + " WHERE organization.organization_id = organization.root_id AND organization.org_type = 5  ");
        if (orgSearchDto != null && orgSearchDto.getOrgName() != null && !orgSearchDto.getOrgName().equals("")) {
            sql.append(" AND organization.organization_name LIKE '%").append(orgSearchDto.getOrgName()).append("%'");
        }
        if (orgSearchDto != null && orgSearchDto.getProvinceId() != null && orgSearchDto.getProvinceId() != 0) {
            sql.append(" AND organization.province_id =").append(orgSearchDto.getProvinceId());
        }
        if (orgSearchDto != null && orgSearchDto.getNationalId() != null && orgSearchDto.getNationalId() != 0) {
            sql.append(" AND organization.nationnal_id =").append(orgSearchDto.getNationalId());
        }
        if (orgSearchDto != null && orgSearchDto.getTrustType() != null && orgSearchDto.getTrustType() != 0) {
            sql.append(" AND organization.trust_type =").append(orgSearchDto.getTrustType());
        }
        Query query = em.createNativeQuery(sql.toString(), OrgPartnerDto.class);
        return query.getResultList();
    }
}
