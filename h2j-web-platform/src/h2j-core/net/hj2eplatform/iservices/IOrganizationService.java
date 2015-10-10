/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import java.util.List;
import javax.faces.model.SelectItem;
import net.hj2eplatform.models.Organization;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
public interface IOrganizationService extends ILazyDataSupportMapFilter<Organization> {

    public Organization getOrgByID(Long orgID);
    
    public Organization getRootOrgById(Long orgId);

    public List<Organization> getOrganizationList(String searchInput);

    public List<Organization> getOrganizationByParrent(Long parentId);

    public List<Organization> getOrganizationByName(String organizationName);

    public Organization getOrganizationByCode(String code);

    public Organization getRootOrg(Long orgId);

    public void checkExits(Organization organization);

    public List<Organization> sugetOrgCus(String searchInput);

    public List<SelectItem> getOrg4SelectListByRootId(Long rootId);

    public List<Organization> getOrganizationRoot();

    public List<Organization> getOrganizationListByRoot(Long rootId);

    public List<Organization> getOrganizationByParrentId(Long parentId, Boolean orgFoundation);

    public List<Organization> getOrganizationByParrentId(Long parentId);

    public void saveOrg(Organization organization);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> sugetSuplier(String searchInput);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> sugetAgent(String searchInput);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Organization> sugetOrgSuplier(String searchInput, int orgType, Long groupOrgId);

    public List<Organization> getOrganizationByName(String organizationName, Long rootOrg, Integer orgType, Integer groupId);
}
