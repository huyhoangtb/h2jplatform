/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import net.hj2eplatform.dto.OrganizationLazyDataModel;
import net.hj2eplatform.context.H2jContextDefine;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.utils.OrgType;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.core.utils.SystemDefine;
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
public class OrganizationServiceImpl extends AbstractService<Organization> implements IOrganizationService, java.io.Serializable {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    public Organization getRootOrgById(Long orgId) {
        try {
            String sql = "select o1 from Organization o1 where exists (select 1 from Organization o2 where o2.organizationId = :organizationId and o1.organizationId = o2.rootId )";
            Query query = em.createQuery(sql, Organization.class);
            query.setParameter("organizationId", orgId);
            query.setMaxResults(1);
            return (Organization) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param organization Hàm này chỉ áp dụng đối với partner
     */
    public void saveOrg(Organization organization) {
        boolean insert = false;
        Date today = new Date();
        validateOrganization(organization);
        Organization organizationParent = organization.getParentId();
        if (organizationParent == null) {
            organizationParent = AuthenticationController.getCurrentOrg();
            organization.setParentId(organizationParent);
        }
        if (organization.getOrganizationId() != null && organization.getOrganizationId() == organizationParent.getOrganizationId()) {
            throw new ValidateInputException(ResourceMessages.getResource("Tổ chức cha không thể là tổ chức hiện tại!"));
        }

        if (organization.getOrganizationId() == null || organization.getOrganizationId() == -1L) {
            insert = true;
            organization.setCreatedDate(today);
            organization.setCreatedStaff(AuthenticationController.getCurrentHuman().getHumanId());
            organization.setOrganizationId(getSequence(SystemDefine.SEQUENCE_ORGANIZATION_ID).longValue());
            organization.setOrganizationCode(getSequenceCode(SystemDefine.SEQUENCE_ORGANIZATION_CODE));
            organization.setRootId(organization.getOrganizationId());
        }
        organization.setModifiedDate(today);
        organization.setModifiedStaff(AuthenticationController.getCurrentHuman().getHumanId());
        organization.pullLocation();
        organization.pullLogo();

        if (organizationParent != null && organizationParent.getPath() != null
                // kiem tra lai parrent
                && !organizationParent.getPath().endsWith("_" + organization.getPath() + "_")) {
            organization.setPath(organizationParent.getPath() + organization.getOrganizationId() + "_");
        } else {
            organization.setPath(organization.getOrganizationId() + "_");
        }
        if (insert) {
            persistEntity(organization);
        } else {
            saveEntity(organization);
        }

    }

    private void validateOrganization(Organization organization) {
        if (organization.getLocationComObj() != null) {

            Location location = organization.getLocationComObj().getNationalSelected();
            if (location == null) {
                throw new ValidateInputException(ResourceMessages.getResource("location_error_not_enter_national"));
            }
            location = organization.getLocationComObj().getProvinceSelected();
            if (location == null) {
                throw new ValidateInputException(ResourceMessages.getResource("location_error_not_enter_province"));
            }

            if (organization.getOrganizationName() == null || organization.getOrganizationName().trim().compareTo("") == 0) {
                throw new ValidateInputException(ResourceMessages.getResource("not_enter_organization_name"));
            }
        }
        organization.setOrganizationName(DataValidator.deleteSpace(organization.getOrganizationName()));
        organization.setAddress(DataValidator.standardName(organization.getAddress()));
        DataValidator.validateEmailAdress(organization.getEmailAddress());
        DataValidator.validateWebsiteAddress(organization.getWebsite());
        if (organization.getWebsite() != null) {
            organization.setWebsite(organization.getWebsite().trim());
        }

        organization.setEmailAddress(organization.getEmailAddress().trim());
        this.checkExits(organization);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<Organization> sugetSuplier(String searchInput) {
        return sugetOrgSuplier(searchInput, OrgType.SUPPLIER.toInteger(), AuthenticationController.getCurrentOrg().getRootId());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<Organization> sugetOrgCus(String searchInput) {
        return sugetOrgSuplier(searchInput, OrgType.ORG_CUSTOMER.toInteger(), AuthenticationController.getCurrentOrg().getRootId());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<Organization> sugetAgent(String searchInput) {
        return sugetOrgSuplier(searchInput, OrgType.AGENCY.toInteger(), AuthenticationController.getCurrentOrg().getRootId());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<Organization> sugetOrgSuplier(String searchInput, int orgType, Long groupOrgId) {
        StringBuilder sql = new StringBuilder("select organization from Organization organization where "
                + " (LOWER(organization.organizationName)  like  '%" + searchInput.trim().toLowerCase() + "%'"
                + " or upper(organization.organizationCode) like " + "'%" + searchInput.trim().toLowerCase() + "%'"
                + " or upper(organization.shortName) like " + "'%" + searchInput.trim().toLowerCase() + "%')");

        sql.append(" and organization.organizationId = organization.rootId  ");
        if (orgType == OrgType.AGENCY.toInteger()) {// nha cung cap, dai ly deu la dai ly
            sql.append(" and (organization.orgType = 4 or  organization.orgType = 3 or organization.orgType = 1 )");
        } else {
            sql.append(" and organization.orgType =").append(orgType);
        }
        // cho phep lay du lieu cua nguoi to chuc san va du lieu cua chinh to chuc do
        sql.append(" and (organization.groupPartnerId = ").append(groupOrgId);
        if (groupOrgId != H2jContextDefine.getH2jRootOrg().getOrganizationId().longValue()) {
            sql.append(" or organization.groupPartnerId = ").append(H2jContextDefine.getH2jRootOrg().getOrganizationId());
        }
        sql.append(") ");

        Query query = em.createQuery(sql.toString());
        query.setFirstResult(0);
        query.setMaxResults(10);

        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<Organization> getOrganizationList(String searchInput) {
        StringBuilder sql = new StringBuilder("select organization from Organization organization where (LOWER(organization.organizationName)  like  '%" + searchInput.trim().toLowerCase() + "%'"
                + " or upper(organization.organizationCode) like " + "'%" + searchInput.trim().toLowerCase() + "%'"
                + " or upper(organization.shortName) like " + "'%" + searchInput.trim().toLowerCase() + "%')");
//                + " or upper(organization.enlishName) like :searchInput";

        // role admin thi tim trong to chuc cha
        if (AuthenticationController.getCurrentOrg().getOrgType() != null || AuthenticationController.getCurrentOrg().getOrgType() != OrgType.ORG_H2J_CENTER.toInteger()) {
            sql.append(" and organization.rootId = ").append(AuthenticationController.getCurrentOrg().getRootId());
        }
        sql.append(" and organization.orgType = ").append(AuthenticationController.getCurrentOrg().getOrgType());
        Query query = em.createQuery(sql.toString());

        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> getOrganizationByName(String organizationName) {
        String str = "select organization from Organization organization where LOWER(organization.organizationName) = :name";
        Query query = em.createQuery(str).setParameter("name", organizationName.toLowerCase());
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> getOrganizationByName(String organizationName, Long rootOrg, Integer orgType, Integer groupId) {
        String str = "select organization from Organization organization where LOWER(organization.organizationName) = :name "
                + " and organization.groupId = :groupId and organization.rootId = :rootId and organization.orgType = :orgType";
        Query query = em.createQuery(str).setParameter("name", organizationName.toLowerCase());
        query.setParameter("rootId", rootOrg);
        query.setParameter("orgType", orgType);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void checkExits(Organization organization) {
        StringBuffer str = new StringBuffer("select organization from Organization organization where (LOWER(organization.organizationName) = '");
        str.append(organization.getOrganizationName().toLowerCase()).append("'");

        if (organization.getShortName() != null && (organization.getShortName().compareTo("") != 0)) {
            str.append(" or  LOWER(organization.shortName) = '").append(organization.getShortName().toLowerCase()).append("'");
        }
        if (organization.getParentId() == null || organization.getParentId().getOrganizationId() == null) {
            str.append(" or organization.parentId is null ");
        } else {
            str.append(" or organization.parentId.organizationId = ").append(organization.getParentId().getOrganizationId());
        }
        str.append(")");
        AuthenticationController controller = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        // role admin thi tim trong to chuc cha
        if (controller.getOrganization().getOrgType() != null || controller.getOrganization().getOrgType() != OrgType.ORG_H2J_CENTER.toInteger()) {
            str.append(" and organization.rootId = ").append(controller.getOrganization().getRootId());
        }

        Query query = em.createQuery(str.toString());
        query.setFirstResult(0);
        query.setMaxResults(10);

        List<Organization> organizationList = query.getResultList();
        for (Organization org : organizationList) {
            if (organization.getOrganizationId() != null && org.getOrganizationId().longValue() == organization.getOrganizationId().longValue()) {
                continue;
            }
            if (org.getOrganizationName().toLowerCase().compareTo(organization.getOrganizationName().toLowerCase()) == 0) {
                throw new ValidateInputException(ResourceMessages.getResource("organization_exist"));
            }
            if (organization.getShortName() != null && org.getShortName() != null && organization.getShortName().compareTo("") != 0
                    && org.getShortName().toLowerCase().compareTo(organization.getShortName().toLowerCase()) == 0) {
                throw new ValidateInputException(ResourceMessages.getResource("shorname_exists"));
            }

        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select org from Organization org  where org.organizationId = org.organizationId ");

        builddingQuery(sql, filters);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  org.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
            sql.append(" order by  org.modifiedDate desc");
        }

        Query query = em.createQuery(sql.toString());
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(org) from Organization org where org.organizationId = org.organizationId  ");

        Query counterQuery = em.createQuery(builddingQuery(counter, filters).toString());
        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    private StringBuffer builddingQuery(StringBuffer sql, Map<String, Object> filters) {
        String data = (String) filters.get(OrganizationLazyDataModel.ORGANIZATION_SEARCH_DATA);
        String nationalId = (String) filters.get(OrganizationLazyDataModel.ORGANIZATION_SEARCH_NATIONAL_ID);
        String provinceId = (String) filters.get(OrganizationLazyDataModel.ORGANIZATION_SEARCH_PROVINCE_ID);
        String parrentId = (String) filters.get(OrganizationLazyDataModel.PARRENT_ID);
        String isManagerMyOrg = (String) filters.get(OrganizationLazyDataModel.IS_MANAGER_MY_ORG);
        if (data != null && data.compareTo("") != 0) {
            data = data.toUpperCase();
            if (data.startsWith("#")) {
                data = data.substring(1, data.length());
                sql.append(" and upper(org.organizationCode) = '").append(data).append("'");
            } else {
                sql.append(" and (upper(org.organizationName) like '%").append(data).append("%'")
                        .append(" or  upper(org.enlishName) like '%").append(data).append("%'")
                        .append(" or  upper(org.address) like '%").append(data).append("%'")
                        .append(" or  org.phone = '").append(data).append("'")
                        .append(" or  upper(org.emailAddress) = '").append(data).append("'")
                        .append(" or  upper(org.website) = '").append(data).append("')");
            }
        }
        sql.append(" and org.rootId = ").append(AuthenticationController.getCurrentOrg().getRootId());
        if (nationalId != null) {
            sql.append(" and org.nationnalId = ").append(nationalId);
        }
        if (provinceId != null) {
            sql.append(" and org.provinceId = ").append(provinceId);
        }
        if (parrentId != null) {
            sql.append(" and org.parentId.organizationId = ").append(parrentId);
        }

        return sql;
    }

    @Override
    public Organization getOrganizationByCode(String code) {

        String sql = "select org from Organization org where org.organizationCode = :organizationCode";
        Query counterQuery = em.createQuery(sql);
        counterQuery.setParameter("organizationCode", code);
        List<Organization> list = (List<Organization>) counterQuery.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Organization> getOrganizationByParrent(Long parentId) {
        if (parentId == null) {
            return this.getOrganizationRoot();
        } else {
            return this.getOrganizationByParrentId(parentId);
        }
    }

    @Override
    public List<Organization> getOrganizationRoot() {
        StringBuilder sql = new StringBuilder("select org.* from organization org where  org.organization_Id = ")
                .append(AuthenticationController.getCurrentOrg().getRootId());
        Query query = em.createNativeQuery(sql.toString(), Organization.class);

        return query.getResultList();
    }

    @Override
    public List<Organization> getOrganizationByParrentId(Long parentId) {
        StringBuilder sql = new StringBuilder("select org from Organization org where org.parentId.organizationId = :parentId")
                .append(" and org.rootId = ").append(AuthenticationController.getCurrentOrg().getRootId());
        Query counterQuery = em.createQuery(sql.toString());
        counterQuery.setParameter("parentId", parentId);
        return counterQuery.getResultList();
    }

    @Override
    public List<Organization> getOrganizationByParrentId(Long parentId, Boolean orgFoundation) {
        StringBuilder sql = new StringBuilder("select org from Organization org where org.parentId.organizationId = :parentId")
                .append(" and org.rootId = ").append(AuthenticationController.getCurrentOrg().getRootId());

        Query counterQuery = em.createQuery(sql.toString());
        counterQuery.setParameter("parentId", parentId);
        return counterQuery.getResultList();
    }

    public List<Organization> getOrganizationListByRoot(Long rootId) {
        StringBuilder sql = new StringBuilder("select org from Organization org where org.rootId = :rootId");
        Query counterQuery = em.createQuery(sql.toString());
        counterQuery.setParameter("rootId", rootId);
        return counterQuery.getResultList();
    }

    public List<SelectItem> getOrg4SelectListByRootId(Long rootId) {
        List<Organization> list = getOrganizationListByRoot(rootId);
        if (list == null) {
            return new ArrayList<SelectItem>();
        }
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Organization organization : list) {
            items.add(new SelectItem(organization.getOrganizationId(), organization.getOrganizationName()));
        }
        return items;
    }

    public Organization getRootOrg(Long orgId) {
        try {
            String sql = "select o1.* from organization o1, organization o2 where o1.organization_id = o2.root_id and o2.organization_id = ?1";
            Query counterQuery = em.createNativeQuery(sql.toString(), Organization.class);
            counterQuery.setParameter(1, orgId);
            return (Organization) counterQuery.getSingleResult();
        } catch (Exception e) {
        }
        return null;

    }

    @Override
    public Organization getRowKey(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Organization getOrgByID(Long orgID) {
        String sql = "select org from Organization org where org.organizationId = :orgID";
        Query counterQuery = em.createQuery(sql.toString());
        counterQuery.setParameter("orgID", orgID);
        return (Organization) counterQuery.getSingleResult();
    }
}
