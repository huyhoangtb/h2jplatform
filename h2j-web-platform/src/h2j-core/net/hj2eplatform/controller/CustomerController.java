/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.iservices.IHumanDtoService;
import net.hj2eplatform.iservices.IHumanService;
import net.hj2eplatform.iservices.IUserService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.core.utils.SystemDefine;
import net.hj2eplatform.iservices.IGenerateService;
import net.hj2eplatform.utils.HumanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author HuyHoang
 */
@Controller("customerController")
@Scope("view")
public class CustomerController implements Serializable {

    private LazyDataSupportMapFilter<HumanDto> humanLazyDataModel;
    @Autowired
    private IUserService userService;
    @Autowired
    private IHumanDtoService humanDtoService;
    @Autowired
    private IHumanService humanService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IGenerateService generateService;
    private HumanDto human;
    private HumanDto searchData;
    private String dilogControl = "false";
    private Organization organization;
    private Organization organizationSearch;
    private List<SelectItem> userStatuList;
    private List<SelectItem> userGenderList;
    public static String RESTORE_CUSTOMER_ON_REQUEST = "RESTORE_CUSTOMER_ON_REQUEST";
    public static String RESTORE_ORG_ON_REQUEST = "RESTORE_ORG_ON_REQUEST";

    @PostConstruct
    public void init() {
        this.humanLazyDataModel = new LazyDataSupportMapFilter<HumanDto>(humanDtoService);
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.STAFF_TYPE, HumanType.RETAIL_CUSTOMER.toString());
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.ORG_TYPE, AuthenticationController.getCurrentOrg().getOrgType().toString());
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, AuthenticationController.getCurrentOrg().getRootId().toString());
        String humanIdParam = ControllerUtils.getRequestParameter("cusId");
        if (humanIdParam == null) {
            human = new HumanDto();
            this.organization = new Organization();

        } else {
            this.human = humanDtoService.loadHumanDto(Long.valueOf(humanIdParam));
            Organization orgRestore = organizationService.loadEntity(Organization.class, human.getOrganizationId());
            if (orgRestore != null) {
                this.organization = orgRestore;
            } else {
                this.organization = new Organization();
            }
            UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
            userController.setHuman4InitPermission(human);
        }

//        
//        this.organization = new Organization();
//
//        Organization orgRestore = (Organization) ControllerUtils.getSessionMap().get(RESTORE_ORG_ON_REQUEST);
//        if (orgRestore != null) {
//            this.organization = orgRestore;
//            ControllerUtils.getSessionMap().remove(RESTORE_ORG_ON_REQUEST);
//        }
        searchData = new HumanDto();

        this.organizationSearch = new Organization();
        this.userGenderList = this.generateService.getParramItems(SystemDefine.APP_USER_GROUP_CODE, SystemDefine.USER_GENDER);
        this.userStatuList = this.generateService.getParramItems(SystemDefine.APP_USER_GROUP_CODE, SystemDefine.USER_STATUS);

    }

    public String onStartCreatedNewCustomer() {
        human = new HumanDto();
        this.organization = new Organization();
        return "add_new_customer";
    }

    public void deleteHuman() {
        try {
            deletePublicHuman();
        } catch (Exception ex) {
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("user_cannot_delete_success"));
        }
    }

    public void deletePublicHuman() {
        Users user = human.cloneUser();
        this.userService.removeEntity(user);
        this.humanService.removeEntity(human.cloneHuman());

        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("user_delete_success"));
        this.organization = new Organization();
        this.human = null;
        this.dilogControl = "false";
    }

    public void searchHuman() {
        Map filter = new HashMap<String, String>();
        if (organizationSearch != null && organizationSearch.getOrganizationId() != null) {
            filter.put(IHumanDtoService.USER_PARENT_PATH, organizationSearch.getPath());
            filter.put(IHumanDtoService.USER_ORGNAZATION_ID, organizationSearch.getOrganizationId().toString());
            filter.put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, organizationSearch.getRootId().toString());
        }
        filter.put(IHumanDtoService.USER_USERNAME, this.searchData.getUsername());
        filter.put(IHumanDtoService.USER_EMAIL, this.searchData.getEmailAddress());
        filter.put(IHumanDtoService.USER_FULLNAME, this.searchData.getFullName());
        filter.put(IHumanDtoService.USER_TEL, this.searchData.getTel());
        filter.put(IHumanDtoService.USER_GENDER, this.searchData.getGender().toString());
        filter.put(IHumanDtoService.STAFF_TYPE, HumanType.RETAIL_CUSTOMER.toString());
        filter.put(IHumanDtoService.ORG_TYPE, AuthenticationController.getCurrentOrg().getOrgType().toString());
        this.humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, AuthenticationController.getCurrentOrg().getRootId().toString());
        if (this.searchData.getBirthday() != null) {
            filter.put(IHumanDtoService.USER_BITHDAY, DateTimeUtils.convertDateToString(this.searchData.getBirthday(), DateTimeUtils.ddMMyyyy));
        }

        this.humanLazyDataModel.setFilters(filter);
    }

    public void saveHuman() {
        try {
            human.setUserType(HumanType.RETAIL_CUSTOMER.toInteger());
            this.humanDtoService.saveHuman(this.human, organization, HumanType.RETAIL_CUSTOMER);

        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
//            ex.printStackTrace();
//            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public String editDetailCustomer() {
        try {
            this.organization = this.organizationService.loadEntity(Organization.class, this.human.getOrganizationId());
            ControllerUtils.getSessionMap().put(RESTORE_CUSTOMER_ON_REQUEST, this.human);
            ControllerUtils.getSessionMap().put(RESTORE_ORG_ON_REQUEST, this.organization);
            return "add_new_customer";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

//        ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, this.organization);
    }

    public void viewDetailHuman() {
        try {
            this.organization = this.organizationService.loadEntity(Organization.class, this.human.getOrganizationId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.dilogControl = "true";

//        ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, this.organization);
    }

    public void closeDetailHuman() {
//       ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        this.organization = new Organization();
        this.human = new HumanDto();
        this.dilogControl = "false";
    }

    public List<SelectItem> getUserStatuList() {
        return userStatuList;
    }

    public void setUserStatuList(List<SelectItem> userStatuList) {
        this.userStatuList = userStatuList;
    }

    public List<SelectItem> getUserGenderList() {
        return userGenderList;
    }

    public void setUserGenderList(List<SelectItem> userGenderList) {
        this.userGenderList = userGenderList;
    }

    public String getDilogControl() {
        return dilogControl;
    }

    public HumanDto getSearchData() {
        return searchData;
    }

    public void setSearchData(HumanDto searchData) {
        this.searchData = searchData;
    }

    public void setDilogControl(String dilogControl) {
        this.dilogControl = dilogControl;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public LazyDataSupportMapFilter<HumanDto> getHumanLazyDataModel() {
        return humanLazyDataModel;
    }

    public void setHumanLazyDataModel(LazyDataSupportMapFilter<HumanDto> humanLazyDataModel) {
        this.humanLazyDataModel = humanLazyDataModel;
    }

    public IHumanDtoService getHumanDtoService() {
        return humanDtoService;
    }

    public void setHumanDtoService(IHumanDtoService humanDtoService) {
        this.humanDtoService = humanDtoService;
    }

    public IHumanService getHumanService() {
        return humanService;
    }

    public void setHumanService(IHumanService humanService) {
        this.humanService = humanService;
    }

    public IOrganizationService getOrganizationService() {
        return organizationService;
    }

    public void setOrganizationService(IOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public HumanDto getHuman() {
        return human;
    }

    public void setHuman(HumanDto human) {
        this.human = human;
    }

    public Organization getOrganizationSearch() {
        return organizationSearch;
    }

    public IGenerateService getGenerateService() {
        return generateService;
    }

    public void setGenerateService(IGenerateService generateService) {
        this.generateService = generateService;
    }

    public void setOrganizationSearch(Organization organizationSearch) {
        this.organizationSearch = organizationSearch;
    }
}
