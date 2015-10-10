/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import net.hj2eplatform.dto.LocationComObj;
import net.hj2eplatform.dto.OrganizationLazyDataModel;
import net.hj2eplatform.dto.OrganizationTreeComObj;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.utils.ControllerName;

import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.core.utils.SystemDefine;
import org.primefaces.event.NodeSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("organizationController")
@Scope("view")
public class OrganizationController implements Serializable {

    private Organization organization;
    private Organization organizationParent;
    private Organization organizationSelection;
    private List<Organization> organizationList;
    private OrganizationLazyDataModel organizationLazyDataModel;
    private OrganizationTreeComObj organizationTree;
    @Autowired
    private IOrganizationService organizationService;
    private String searchOrganizationData;
    private String dilogControl = "false";
    private LocationComObj locationComObj;
    private static String ORGANIZATION_OBJ = "ORGANIZATION_OBJ";

    @PostConstruct
    private void init() {
//        Log4objUtils.addLog("Human", "10", "Trans_Total", "0", "100.000", Log4obj.LOG_INFO, "Tang so luong giao dich tu {0} len {1}", new String[]{"0", "100.000"});
        organizationLazyDataModel = new OrganizationLazyDataModel(organizationService);
//        organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.IS_MANAGER_MY_ORG, OrganizationLazyDataModel.IS_MANAGER_MY_ORG);
        locationComObj = new LocationComObj();
        locationComObj.init();
        organizationTree = new OrganizationTreeComObj(organizationService);
        String orgId = ControllerUtils.getRequestParameter("orgId");
        String orgParentId = ControllerUtils.getRequestParameter("orgParentId");
        if (orgId != null) {
            this.organization = organizationService.loadEntity(Organization.class, Long.valueOf(orgId));
            if (this.organization == null) {
                organization = new Organization();
            }
            if (organization.getLogoImgId() != null) {
                ResourceManagerController resource = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
                resource.loadMyResoure(organization.getLogoImgId());
            }
        } else {
            organization = new Organization();
        }
        if (orgParentId != null && orgParentId.trim().compareTo("") != 0) {
            if (organization != null && organization.isRoot()) {
                this.organizationParent = organization;
            } else {
                this.organizationParent = organizationService.loadEntity(Organization.class, Long.valueOf(orgParentId));
            }

        } else if (AuthenticationController.getCurrentOrg() != null) {
            this.organizationParent = AuthenticationController.getCurrentOrg();
        } else {
            this.organizationParent = this.organization.getParentId();
        }

    }

    public String onStartCreatedNewOrganization() {
        organization = new Organization();
//        ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        return "edit_organization";
    }

    public void searchOrganization() {

        organizationLazyDataModel = new OrganizationLazyDataModel(organizationService);
        this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_DATA, searchOrganizationData);
        if (this.organizationParent != null) {
            this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.PARRENT_ID, this.organizationParent.getOrganizationId().toString());
        }

        Location location = locationComObj.getNationalSelected();
        if (location != null) {
            this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_NATIONAL_ID, location.getLocationId().toString());
        }
        location = locationComObj.getProvinceSelected();
        if (location != null) {
            this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_PROVINCE_ID, location.getLocationId().toString());
        }

    }

    public void deletePublicOrganization() {
        this.organizationService.removeEntity(organization);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("organization_delete_success"));
        this.organization = new Organization();
        this.organizationParent = null;
        this.dilogControl = "false";
    }

    public void deleteOrganization() {
        try {
            deletePublicOrganization();

        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("organization_cant_delete"));
        }
    }

    public String updateOrganization() {
        try {
            if (organizationParent != null && organizationParent.getRootId().longValue() != organization.getOrganizationId().longValue() && organizationParent.getOrganizationId().longValue() == organization.getOrganizationId().longValue()) {
                ControllerUtils.addErrorMessage("Không thể chọn tổ chức cấp trên là " + organization.getOrganizationName());
                return null;
            }
            persitOrganization();
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_organization_success"));
            ControllerUtils.forwardToPage("/app/administrator/core/organization/edit")
                    .append("orgId=").append(organization.getOrganizationId())
                    .append("orgParentId").append(organizationParent.getOrganizationId());
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
        return null;
    }

    public void createOrganizationAndContinues() {
        try {
            persitOrganization();
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_organization_success"));
            this.organization = new Organization();
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public String createOrganization() {
        try {
            persitOrganization();
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_organization_success"));
            ControllerUtils.forwardToPage("/app/administrator/core/organization/edit")
                    .append("orgId=").append(organization.getOrganizationId())
                    .append("orgParentId").append(organizationParent.getOrganizationId());
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
        return null;
    }

    private void persitOrganization() {
        if (this.organizationParent != null) {
            organization.setParentId(organizationParent);
            organization.setRootId(this.organizationParent.getRootId());
        }
        Date today = new Date();
        validateOrganization();

        if (organization.getOrganizationId() == null) {
            organization.setCreatedDate(today);
            organization.setCreatedStaff(AuthenticationController.getCurrentHuman().getHumanId());
            organization.setOrganizationId(this.organizationService.getSequence(SystemDefine.SEQUENCE_ORGANIZATION_ID).longValue());
            organization.setOrganizationCode(this.organizationService.getSequenceCode(SystemDefine.SEQUENCE_ORGANIZATION_CODE));
        }
        organization.setModifiedDate(today);
        organization.setModifiedStaff(AuthenticationController.getCurrentHuman().getHumanId());
        organization.pullLocation();
        organization.pullLogo();
        organization.setOrgType(AuthenticationController.getCurrentOrg().getOrgType());
        if (organizationParent != null && organizationParent.getPath() != null) {
            if (organization.getOrganizationId().longValue() != this.organizationParent.getOrganizationId()) {
                organization.setPath(organizationParent.getPath() + organization.getOrganizationId() + "_");
            }
        } else {
            organization.setPath(organization.getOrganizationId() + "_");
        }

        this.organizationService.saveEntity(organization);
        if (this.organizationParent != null && organization.getOrganizationId().longValue() == this.organizationParent.getOrganizationId()) {
            this.organizationParent = organization;
        }
    }

    private void validateOrganization() {
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
        if (organization.getAddress() == null || organization.getAddress().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("not_enter_address"));
        }
//        if (organization.getPhone() == null || organization.getPhone().trim().compareTo("") == 0) {
//            throw new ValidateInputException(ResourceMessages.getResource("not_enter_phone"));
//        }

        this.organization.setOrganizationName(DataValidator.deleteSpace(organization.getOrganizationName()));
        this.organization.setAddress(DataValidator.standardName(organization.getAddress()));
        DataValidator.validateEmailAdress(this.organization.getEmailAddress());
        DataValidator.validateWebsiteAddress(this.organization.getWebsite());
        this.organizationService.checkExits(organization);
        this.organization.setGroupPartnerId(AuthenticationController.getCurrentOrg().getGroupPartnerId());
    }

    public void viewDetailOrganization() {
        this.dilogControl = "true";
        this.organizationParent = this.organization.getParentId();
//        ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, this.organization);
    }

    public void closeDetailOrganization() {
//       ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        this.organization = new Organization();
        this.dilogControl = "false";
    }

    public void onNodeSelected(NodeSelectEvent event) {
        this.organizationParent = (Organization) event.getTreeNode().getData();
        searchOrganization();
    }

    public OrganizationTreeComObj getOrganizationTree() {
        return organizationTree;
    }

    public void setOrganizationTree(OrganizationTreeComObj organizationTree) {
        this.organizationTree = organizationTree;
    }

    public LocationComObj getLocationComObj() {
        return locationComObj;
    }

    public void setLocationComObj(LocationComObj locationComObj) {
        this.locationComObj = locationComObj;
    }

    public String getDilogControl() {
        return dilogControl;
    }

    public void setDilogControl(String dilogControl) {
        this.dilogControl = dilogControl;
    }

    public void retypeOrganization() {
        this.organization = new Organization();
    }

    public List<Organization> completeOrganization(String query) {
        return organizationService.getOrganizationList(query);
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        System.out.println("setter: " + organization);
        this.organization = organization;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public Organization getOrganizationParent() {
        return organizationParent;
    }

    public void setOrganizationParent(Organization organizationParent) {
        this.organizationParent = organizationParent;
    }

    public String getSearchOrganizationData() {
        return searchOrganizationData;
    }

    public void setSearchOrganizationData(String searchOrganizationData) {
        this.searchOrganizationData = searchOrganizationData;
    }

    public OrganizationLazyDataModel getOrganizationLazyDataModel() {
        return organizationLazyDataModel;
    }

    public void setOrganizationLazyDataModel(OrganizationLazyDataModel organizationLazyDataModel) {
        this.organizationLazyDataModel = organizationLazyDataModel;
    }

    public Organization getOrganizationSelection() {
        return organizationSelection;
    }

    public void setOrganizationSelection(Organization organizationSelection) {
        this.organizationSelection = organizationSelection;
    }

    public IOrganizationService getOrganizationService() {
        return organizationService;
    }

    public void setOrganizationService(IOrganizationService organizationService) {
        this.organizationService = organizationService;
    }
}
