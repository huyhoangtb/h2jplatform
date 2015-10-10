/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;
//import oracle.toplink.essentials.platform.database.oracle.OraclePlatform;
//import 
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import net.hj2eplatform.dto.OrganizationTreeComObj;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.iservices.IHumanDtoService;
import net.hj2eplatform.iservices.IHumanService;
import net.hj2eplatform.iservices.IUserService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.HumanType;
import net.hj2eplatform.utils.UserGender;
import net.hj2eplatform.utils.UserStatus;
import org.primefaces.event.NodeSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author HuyHoang
 */
@Controller("humanController")
@Scope("view")
public class HumanController implements Serializable {

    private LazyDataSupportMapFilter<HumanDto> humanLazyDataModel;
    @Autowired
    private IUserService userService;
    @Autowired
    private IHumanDtoService humanDtoService;
    @Autowired
    private IHumanService humanService;
    private OrganizationTreeComObj organizationTree;
    @Autowired
    private IOrganizationService organizationService;
    private HumanDto human;
    private HumanDto searchData;
    private String dilogControl = "false";
    private Organization organization;
    private Organization organizationSearch;
    private List<SelectItem> userStatuList;
    private List<SelectItem> userGenderList;
    public static String RESTORE_STAFF_ON_REQUEST = "RESTORE_STAFF_ON_REQUEST";
    public static String RESTORE_ORG_ON_REQUEST = "RESTORE_ORG_ON_REQUEST";

    @PostConstruct
    public void init() {
        this.humanLazyDataModel = new LazyDataSupportMapFilter<HumanDto>(humanDtoService);
        humanLazyDataModel.getFilters().put(IHumanDtoService.ORG_TYPE, AuthenticationController.getCurrentOrg() == null ? null : AuthenticationController.getCurrentOrg().getOrgType().toString());
        humanLazyDataModel.getFilters().put(IHumanDtoService.STAFF_TYPE, HumanType.STAFF.toString());
        humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, AuthenticationController.getCurrentOrg().getRootId().toString());

        organizationTree = new OrganizationTreeComObj(organizationService);
        String humanIdParam = ControllerUtils.getRequestParameter("humanId");
        if (humanIdParam == null) {
            human = new HumanDto();
            AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
            String orgIdParam = ControllerUtils.getRequestParameter("orgId");
            if (orgIdParam != null) {
                this.organization = organizationService.loadEntity(Organization.class, Long.valueOf(orgIdParam));
                if (this.organization == null) {
                    this.organization = authenticationController.getOrganization();
                }
            } else {
                this.organization = authenticationController.getOrganization();
            }


        } else {
            this.human = humanDtoService.loadHumanDto(Long.valueOf(humanIdParam));
            System.out.println("humanOrg: " + human.getOrganizationId());
            Organization orgRestore = organizationService.loadEntity(Organization.class, this.human.getOrganizationId());
            if (orgRestore != null) {
                this.organization = orgRestore;
            }

            UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
            userController.setHuman4InitPermission(human);
        }

//        String orgIdParam = ControllerUtils.getRequestParameter("orgId");




        searchData = new HumanDto();

        this.organizationSearch = new Organization();
        this.userGenderList = UserGender.toSelectItem();
        this.userStatuList = UserStatus.toSelectItem();

    }

    public List<Human> getHumanInOrg() {
        return this.humanService.getHumanInOrg(this.organization.getOrganizationId());
    }

    public List<Human> getHumanInOrg(Long orgId) {
        return this.humanService.getHumanInOrg(orgId);
    }

    public static HumanController currentInstance() {
        HumanController humanController = ControllerUtils.getBean(ControllerName.STAFF_CONTROLLER);
        return humanController;
    }

    public String onStartCreatedNewHuman() {
        human = new HumanDto();
        this.organization = new Organization();
        return "edit_staff_admin";
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
        this.humanDtoService.setParameter4Search(organizationSearch, humanLazyDataModel, searchData, AuthenticationController.getCurrentOrg().getOrgType());
        humanLazyDataModel.getFilters().put(IHumanDtoService.STAFF_TYPE, HumanType.STAFF.toString());
        humanLazyDataModel.getFilters().put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, AuthenticationController.getCurrentOrg().getRootId().toString());
    }

    public void savePasswordUserAccountOnly() {
        try {
            this.humanDtoService.updateUserPassword(human.cloneUser());
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void saveUserOnly() {
        try {
            this.humanDtoService.saveUserOnly(human);
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }

    }

    public void saveHumanOnlyAndContinues() {
        try {
            human.setUserType(HumanType.STAFF.toInteger());
            this.humanDtoService.saveHumanOnly(human, organization, HumanType.STAFF);
            human = new HumanDto();
            UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
            userController.setHuman4InitPermission(human);
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public String saveHumanOnly() {
        try {
            human.setUserType(HumanType.STAFF.toInteger());
            this.humanDtoService.saveHumanOnly(human, organization, HumanType.STAFF);
            return "/app/administrator/core/human/edit_form_full?faces-redirect=true&humanId=" + human.getHumanId();
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
        return null;
    }

    public void saveHuman() {
        try {
            human.setUserType(HumanType.STAFF.toInteger());
            this.humanDtoService.saveHuman(human, organization, HumanType.STAFF);
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

//    public String editDetailHuman() {
//        try {
//            this.organization = this.organizationService.loadEntity(Organization.class, this.human.getOrganizationId());
//            ControllerUtils.getSessionMap().put(RESTORE_STAFF_ON_REQUEST, this.human);
//            ControllerUtils.getSessionMap().put(RESTORE_ORG_ON_REQUEST, this.organization);
//            return "edit_staff_admin";
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
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

    public void onNodeSelected(NodeSelectEvent event) {
        this.organizationSearch = (Organization) event.getTreeNode().getData();
        Map filter = this.humanLazyDataModel.getFilters();
        if (filter == null) {
            filter = new HashMap<String, String>();
        }

        filter.put(IHumanDtoService.USER_PARENT_PATH, organizationSearch.getPath());
        filter.put(IHumanDtoService.USER_ORGNAZATION_ID, organizationSearch.getOrganizationId().toString());
        this.humanLazyDataModel.setFilters(filter);
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

    public OrganizationTreeComObj getOrganizationTree() {
        return organizationTree;
    }

    public void setOrganizationTree(OrganizationTreeComObj organizationTree) {
        this.organizationTree = organizationTree;
    }

    public Organization getOrganizationSearch() {
        return organizationSearch;
    }

    public void setOrganizationSearch(Organization organizationSearch) {
        this.organizationSearch = organizationSearch;
    }
}
