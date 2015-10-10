/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import net.hj2eplatform.core.component.LazyDataSupportMapFilterAndObject;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.OrganizationLazyDataModel;
import net.hj2eplatform.dto.LocationComObj;
import net.hj2eplatform.dto.OrgPartnerDto;
import net.hj2eplatform.dto.OrgSearchDto;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IGenerateService;
import net.hj2eplatform.iservices.ILocationService;
import net.hj2eplatform.iservices.IOrgPartnerService;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.iservices.IHumanDtoService;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.utils.ControllerName;

import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.utils.DefinePermission;
import net.hj2eplatform.utils.OrgType;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.HumanType;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("orgPartnerController")
@Scope("view")
public class OrgPartnerController implements Serializable {

    private OrgPartnerDto organization;
    private List<OrgPartnerDto> organizationList;
    private LazyDataSupportMapFilter organizationLazyDataModel;
    private LazyDataSupportMapFilterAndObject<OrgPartnerDto> orgPartnerDtoDataModel;
    private UploadedFile logo;
    private String commandButton;
    @Autowired
    private IOrganizationService orgService;
    @Autowired
    private IOrgPartnerService orgPartnerService;
    @Autowired
    private IGenerateService generateService;
    @Autowired
    private IHumanDtoService humanDtoService;
    @Autowired
    private ILocationService locationService;
    private String searchOrganizationData;
    private String deputyName;
    private String dilogControl = "false";
    private LocationComObj locationComObj;
    @ManagedProperty("#{param.viewType}")
    private String viewType;
    private HumanDto human;
    private HumanDto humanDto;
    private Integer currentTab;
    private LazyDataSupportMapFilter<HumanDto> humanLazyDataModel;
    private HumanDto searchData;
    private Integer onAction = 0;
    public static Integer CREATE_STAFF4ORG = 1;
    public static Integer UPDATE_STAFF4ORG = 2;
    private String disableCommBtn = "true";
    private OrgSearchDto orgSearchDto;
    private List<Location> locationOrgList;
    private static final String ORG_TRANS_GROUP = "ORG_TRANS_GROUP";
    private static final String ORG_SUB_TYPE = "ORG_SUB_TYPE";
    private static final String ORG_TYPE_ALL = "ORG_TYPE_ALL";
    private List<SelectItem> orgSubTypeSelectList;
    // loai nhom giao dich --> dai ly
    private List<SelectItem> orgTransGroupSelectList;

    @PostConstruct
    private void init() {
        commandButton = "index";
        organizationLazyDataModel = new LazyDataSupportMapFilter(orgPartnerService);
        human = new HumanDto();
        humanDto = new HumanDto();
        locationComObj = new LocationComObj();
        locationComObj.init();
        String orgId = ControllerUtils.getRequestParameter("orgId");
        if (orgId != null) {
            this.organization = orgPartnerService.loadOrgPartner(orgId);
            if (organization.getLogoImgId() != null) {
                ResourceManagerController resource = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
                resource.loadMyResoure(organization.getLogoImgId());
            }
            disableCommBtn = "false";
        } else {
            organization = new OrgPartnerDto();
        }

        this.humanLazyDataModel = new LazyDataSupportMapFilter<HumanDto>(humanDtoService);
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ID, this.organization.getOrganizationId() == null ? null : this.organization.getOrganizationId().toString());
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.USER_PARENT_PATH, this.organization.getPath());
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, this.organization.getRootId().toString());

        this.humanLazyDataModel.getFilters().put(IHumanDtoService.STAFF_TYPE, HumanType.STAFF.toString());
        searchData = new HumanDto();

//        orgPartnerDtoDataModel = new LazyDataSupportMapFilterAndObject<OrgPartnerDto>(orgPartnerService);
        orgSearchDto = new OrgSearchDto();

        locationOrgList = new ArrayList<Location>();
        organizationList = new ArrayList<OrgPartnerDto>();
        organizationList = orgPartnerService.listOrgParterDto(orgSearchDto);
    }

    public void searchHuman() {
        Map filter = new HashMap<String, String>();
        filter.put(IHumanDtoService.USER_USERNAME, this.searchData.getUsername());
        filter.put(IHumanDtoService.USER_EMAIL, this.searchData.getEmailAddress());
        filter.put(IHumanDtoService.USER_FULLNAME, this.searchData.getFullName());
        filter.put(IHumanDtoService.USER_TEL, this.searchData.getTel());
        filter.put(IHumanDtoService.STAFF_TYPE, HumanType.STAFF.toString());
        if (searchData.getGender() != null) {
            filter.put(IHumanDtoService.USER_GENDER, this.searchData.getGender().toString());
        }

        filter.put(IHumanDtoService.ORG_TYPE, getOrgType().toString());

        filter.put(IHumanDtoService.USER_PARENT_PATH, this.organization.getPath());
        filter.put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, this.organization.getRootId().toString());
        filter.put(IHumanDtoService.USER_ORGNAZATION_ID, this.organization.getOrganizationId() == null ? null : this.organization.getOrganizationId().toString());

        if (this.searchData.getBirthday() != null) {
            filter.put(IHumanDtoService.USER_BITHDAY, DateTimeUtils.convertDateToString(this.searchData.getBirthday(), DateTimeUtils.ddMMyyyy));
        }

        this.humanLazyDataModel.setFilters(filter);

    }

    public void saveHuman4Org() {
        try {
            human.setUserType(HumanType.STAFF.toInteger());
            this.humanDtoService.saveHumanOnly(human, organization.cloneOrg(), HumanType.STAFF);
            human = new HumanDto();
            this.onAction = 0;
            this.currentTab = 1;
            searchHuman();
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
//            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void saveUserOnly() {
        try {
            this.humanDtoService.saveUserOnly(human);
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
        }
    }

    public void updateUserPassword() {
        try {
            this.humanDtoService.updateUserPassword(human.cloneUser());
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
        }
    }

    public void saveHumanTour4Org() {
        System.out.println("Call >>>>>>>>> save human");
        try {
            humanDto.setUserType(HumanType.STAFF.toInteger());

            this.humanDtoService.saveHuman(humanDto, organization.cloneOrg(), HumanType.STAFF);
            humanDto = new HumanDto();
            this.onAction = 0;
            this.currentTab = 1;
            searchHuman();
            commandButton = "cusHumanList";
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
//            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void saveInfo() {
        try {
            Organization org = this.organization.cloneOrg();
            orgService.saveOrg(org);
            ControllerUtils.addSuccessMessage("Cập nhật thành công");
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public String onStartNewOrgCus(String pageNext) {
        organization = new OrgPartnerDto();
//        ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        return pageNext;
    }

    public void updateOrgPartner() {
        try {
            Organization org = this.organization.cloneOrg();
            org.setParentId(orgService.loadEntity(Organization.class, organization.getParentId()));
            org.setGroupPartnerId(AuthenticationController.getCurrentOrg().getRootId());
//            org.setRootId(Long.MIN_VALUE);
            orgService.saveOrg(org);
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_organization_success"));
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void commandBtn(String str) {
        System.out.println("commandbutton >>>>>>>>> " + str);
        commandButton = str;
    }

    public int countList() {
        return locationOrgList.size();
    }

    public void recreateNew() {
        this.organization = new OrgPartnerDto();

    }

    public String createOrgPartner() {
        try {
            Organization org = this.organization.cloneOrg();
            AuthenticationController controller = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
            org.setParentId(controller.getOrganization());
            org.setOrgType(this.getOrgType());
            org.setGroupPartnerId(controller.getOrganization().getRootId());
            orgService.saveOrg(org);
            this.organization.setOrganizationId(org.getOrganizationId());
            this.organization.setRootId(org.getRootId());
            this.organization.setOrgType(org.getOrgType());
            this.organization.setParentId(org.getParentId().getOrganizationId());
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_organization_success"));
            return ControllerUtils.forwardToPage("/app/administrator/core/partner/supplier/edit").append("?orgId=").append(organization.getOrganizationId()).toString();
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
            return null;
        } catch (Exception ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
            return null;
//            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void searchOrganization() {
        this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_DATA, searchOrganizationData);

        Location location = locationComObj.getNationalSelected();
        if (location != null) {
            this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_NATIONAL_ID, location.getLocationId().toString());
        }
        location = locationComObj.getProvinceSelected();
        if (location != null) {
            this.organizationLazyDataModel.getFilters().put(OrganizationLazyDataModel.ORGANIZATION_SEARCH_PROVINCE_ID, location.getLocationId().toString());
        }
        organizationLazyDataModel.getFilters().put(IOrgPartnerService.DEPUTY_NAME, this.deputyName);

    }

    public void deletePublicOrganization() {
        this.orgPartnerService.removeEntity(organization);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("organization_delete_success"));
        this.organization = new OrgPartnerDto();
        this.dilogControl = "false";
    }

    public void editHuman4Org() {
        onAction = UPDATE_STAFF4ORG;
        if (AuthenticationController.isGrantedSingle(DefinePermission.ROLE_SUPER_ADMIN.toString())) {
            this.currentTab = 2; // active tab thu 3
        } else {
            this.currentTab = 1; // active tab thu 3
        }

    }

    public void onStartCreatedHuman4Org() {
        onAction = CREATE_STAFF4ORG;
        if (AuthenticationController.isGrantedSingle(DefinePermission.ROLE_SUPER_ADMIN.toString())) {
            this.currentTab = 2; // active tab thu 3
        } else {
            this.currentTab = 1; // active tab thu 3
        }
        this.human = new HumanDto();
    }

    public void deleteOrganization() {
        try {
            deletePublicOrganization();

        } catch (Exception ex) {
//            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("organization_cant_delete"));
        }
    }

    public List<Location> suggetLocation(String locationName) {
        return locationService.suggetPlaceLocation(locationName);
    }

    public Integer getOnAction() {
        return onAction;
    }

    public void setOnAction(Integer onAction) {
        this.onAction = onAction;
    }

    public String getDilogControl() {
        return dilogControl;
    }

    public void setDilogControl(String dilogControl) {
        this.dilogControl = dilogControl;
    }

    public OrgPartnerDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrgPartnerDto organization) {
        this.organization = organization;
    }

    public LazyDataSupportMapFilter getOrganizationLazyDataModel() {
        organizationLazyDataModel.getFilters().put(IOrgPartnerService.ORGANIZATION_TYPE, viewType);
        return organizationLazyDataModel;
    }

    public void setOrganizationLazyDataModel(LazyDataSupportMapFilter organizationLazyDataModel) {
        this.organizationLazyDataModel = organizationLazyDataModel;
    }

    public IOrgPartnerService getOrganizationService() {
        return orgPartnerService;
    }

    public void setOrganizationService(IOrgPartnerService orgPartnerService) {
        this.orgPartnerService = orgPartnerService;
    }

    public UploadedFile getLogo() {
        return logo;
    }

    public String getSearchOrganizationData() {
        return searchOrganizationData;
    }

    public void setSearchOrganizationData(String searchOrganizationData) {
        this.searchOrganizationData = searchOrganizationData;
    }

    public void setLogo(UploadedFile logo) {
        this.logo = logo;
    }

//
//    public Organization getOrganizationSelection() {
//        return organizationSelection;
//    }
//
//    public void setOrganizationSelection(Organization organizationSelection) {
//        this.organizationSelection = organizationSelection;
//    }
    public LocationComObj getLocationComObj() {
        return locationComObj;
    }

    public void setLocationComObj(LocationComObj locationComObj) {
        this.locationComObj = locationComObj;
    }

    public String getViewType() {
//        System.out.println("viewtype: " + viewType);
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getDeputyName() {
        return deputyName;
    }

    public void setDeputyName(String deputyName) {
        this.deputyName = deputyName;
    }

    public void closeDilogControl() {
        this.dilogControl = "false";
    }

    public void initViewAsPopup(String viewType) {
        this.viewType = viewType;
        this.dilogControl = "true";
    }

    public void initView(String viewType) {
        this.viewType = viewType;
    }

    // orgtype: NCC, AGENT; SUBTYPE: nha cung cap khach san, du thuyen, tiny type: dai ly du lich cap may
    private void initOrgTypeAll() {
        Integer orgType = getOrgType();
        this.orgTransGroupSelectList = generateService.getParramItems(ORG_TYPE_ALL, ORG_TRANS_GROUP);
        if (orgType != OrgType.AGENCY.toInteger()) {
            this.orgSubTypeSelectList = generateService.getParramItems(ORG_TYPE_ALL, ORG_SUB_TYPE);

        }
    }

    private Integer getOrgType() {
        int orgType = 0;
        if (viewType == null) {
            return orgType;
        }
        if (viewType.compareTo(H2jWebContext.ORG_CUSTOMER_VIEW_PARRAM) == 0) {
            orgType = OrgType.ORG_CUSTOMER.toInteger();
        } else if (viewType.compareTo(H2jWebContext.ORG_PARTNER_VIEW_PARRAM) == 0) {
            orgType = OrgType.SUPPLIER.toInteger();
        } else if (viewType.compareTo(H2jWebContext.AGENT_PARTNER_VIEW_PARRAM) == 0) {
            orgType = OrgType.AGENCY.toInteger();
        } else {
            orgType = OrgType.ORG_H2J_CENTER.toInteger();
        }
        return orgType;
    }

    public HumanDto getHuman() {
        return human;
    }

    public LazyDataSupportMapFilter<HumanDto> getHumanLazyDataModel() {
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.ORG_TYPE, getOrgType().toString());
        return humanLazyDataModel;
    }

    public void setHumanLazyDataModel(LazyDataSupportMapFilter<HumanDto> humanLazyDataModel) {
        this.humanLazyDataModel = humanLazyDataModel;
    }

    public void setHuman(HumanDto human) {
        this.human = human;
    }

    public Integer getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(Integer currentTab) {
        this.currentTab = currentTab;
    }

    public void onTabChange(TabChangeEvent event) {
        TabView tv = (TabView) event.getComponent();
        this.currentTab = tv.getChildren().indexOf(event.getTab());
//        System.out.println("tabInbdex: " + tv.getChildren().indexOf(event.getTab()));

    }

    public String getOrgTransGroupName(Integer orgTransGroup) {
        if (orgTransGroup == null || orgTransGroup == 0) {
            return "--";
        }

        for (SelectItem item : getOrgTransGroupSelectList()) {
            if (item.getValue() != null && item.getValue().toString().compareTo(orgTransGroup.toString()) == 0) {
                return item.getLabel();
            }
        }
        return "--";
    }

    public String getOrgSubTypeName(Integer orgSubType) {
        if (orgSubType == null || orgSubType == 0) {
            return "--";
        }

        for (SelectItem item : getOrgSubTypeSelectList()) {
            if (item.getValue() != null && item.getValue().toString().compareTo(orgSubType.toString()) == 0) {
                return item.getLabel();
            }
        }
        return "--";
    }

    public HumanDto getSearchData() {
        return searchData;
    }

    public void setSearchData(HumanDto searchData) {
        this.searchData = searchData;
    }

    public String getCommandButton() {
        return commandButton;
    }

    public void setCommandButton(String commandButton) {
        this.commandButton = commandButton;
    }

    public List<SelectItem> getOrgSubTypeSelectList() {
        if (orgSubTypeSelectList == null) {
            initOrgTypeAll();
        }
        return orgSubTypeSelectList;
    }

    public List<SelectItem> getOrgTransGroupSelectList() {
        if (orgTransGroupSelectList == null) {
            initOrgTypeAll();
        }
        return orgTransGroupSelectList;
    }

    public String getDisableCommBtn() {
        return disableCommBtn;
    }

    public void setDisableCommBtn(String disableCommBtn) {
        this.disableCommBtn = disableCommBtn;
    }

    public LazyDataSupportMapFilterAndObject<OrgPartnerDto> getOrgPartnerDtoDataModel() {
        return orgPartnerDtoDataModel;
    }

    public void setOrgPartnerDtoDataModel(LazyDataSupportMapFilterAndObject<OrgPartnerDto> orgPartnerDtoDataModel) {
        this.orgPartnerDtoDataModel = orgPartnerDtoDataModel;
    }

    public OrgSearchDto getOrgSearchDto() {
        return orgSearchDto;
    }

    public void setOrgSearchDto(OrgSearchDto orgSearchDto) {
        this.orgSearchDto = orgSearchDto;
    }

    public void searchOrgDto() {
        Location location = locationComObj.getNationalSelected();
        if (location != null) {
            orgSearchDto.setNationalId(location.getLocationId());
        }
        Location location2 = locationComObj.getProvinceSelected();
        if (location2 != null) {
            orgSearchDto.setProvinceId(location2.getLocationId());
        }

//        orgPartnerDtoDataModel = new LazyDataSupportMapFilterAndObject<OrgPartnerDto>(orgPartnerService);
//        orgPartnerDtoDataModel.setSearchObject(orgSearchDto);
        organizationList = orgPartnerService.listOrgParterDto(orgSearchDto);

    }

    public void orgListLocation(Long orgId) {
        locationOrgList = locationService.getListLocationByOrg(orgId);
    }

    public List<Location> getLocationOrgList() {
        return locationOrgList;
    }

    public void setLocationOrgList(List<Location> locationOrgList) {
        this.locationOrgList = locationOrgList;
    }

    public List<OrgPartnerDto> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<OrgPartnerDto> organizationList) {
        this.organizationList = organizationList;
    }

    public HumanDto getHumanDto() {
        return humanDto;
    }

    public void setHumanDto(HumanDto humanDto) {
        this.humanDto = humanDto;
    }

}
