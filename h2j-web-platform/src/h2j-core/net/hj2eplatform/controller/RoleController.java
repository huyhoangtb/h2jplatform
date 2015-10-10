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
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.RolePermissionDto;
import net.hj2eplatform.core.exception.MySQLDuplicateRecordException;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IPermissionService;
import net.hj2eplatform.iservices.IRolePermissionService;
import net.hj2eplatform.iservices.IRolePermissionServiceDto;
import net.hj2eplatform.iservices.IRoleService;
import net.hj2eplatform.models.Permission;
import net.hj2eplatform.models.Role;
import net.hj2eplatform.models.RolePermission;
import net.hj2eplatform.serviceimpls.RolePermissionServiceDtoImpl;
import net.hj2eplatform.utils.Constraint;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.StatusDefine;
import net.hj2eplatform.core.utils.SystemDefine;
import net.hj2eplatform.iservices.IGenerateService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author HuyHoang
 */
@Controller(ControllerName.ROLE_CONTROLLER)
@Scope("view")
public class RoleController implements Serializable {

    private LazyDataSupportMapFilter<Role> dataModelRole;
    private LazyDataSupportMapFilter<RolePermissionDto> permissionInRoleDataModel;
    private LazyDataSupportMapFilter<RolePermissionDto> permissionNotInRoleDataList;
    private List<RolePermissionDto> permissionInRoleSelects;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRolePermissionServiceDto rolePermissionServiceDto;
    @Autowired
    private IRolePermissionService rolePermissionService;
    @Autowired
    private IPermissionService permissionService;
      @Autowired
    private IGenerateService generateService;
    private Role role;
    private Role searchData;
    private String dilogControl = "false";
    private String dilogViewPermission = "false";
    private List<SelectItem> roleStatusList;
    private List<SelectItem> roleTypeList;
    private List<RolePermissionDto> selectionPermissionsInRoleList;
    public String leftFlag = Constraint.ROLE_INFOMATION;
    private Permission permissionInData;
    private Permission permissionNotInData;
    private RolePermissionDto selectedRolePermissionDto;
    private static final String RESTORE_ROLE_ON_REQUEST = "roleOnRequest";

    @PostConstruct
    public void init() {
        this.dataModelRole = new LazyDataSupportMapFilter<Role>(roleService);
        role = new Role();
        searchData = new Role();
        this.roleStatusList = this.generateService.getParramItems(SystemDefine.ROLE_GROUP_CODE, SystemDefine.ROLE_CODE_STATUS);
        this.roleTypeList = this.generateService.getParramItems(SystemDefine.ROLE_GROUP_CODE, SystemDefine.ROLE_CODE_STYLE);
          String roleId = ControllerUtils.getRequestParameter("roleId");
           Role restoreRole = null;
          if(roleId != null) {
              try {
                  restoreRole = this.roleService.loadEntity(Role.class, Long.valueOf(roleId));
              } catch(Exception ex) {
                  
              }
          }
          if(restoreRole == null) {
              restoreRole = (Role)ControllerUtils.getRequestMap().get(RESTORE_ROLE_ON_REQUEST);
          }
        if (restoreRole != null) {
            this.role = restoreRole;
        }
        initPermissionInRole();
        initPermissionNotinRole();
        this.permissionInData = new Permission();
        this.permissionNotInData = new Permission();
        selectionPermissionsInRoleList = new ArrayList<RolePermissionDto>();
    }

    private void initPermissionInRole() {
        Map<String, String> rolePermission = new HashMap<String, String>();
        rolePermission.put(IRolePermissionServiceDto.QUERY_PERMISSION_ROLE_TYPE, IRolePermissionServiceDto.TYPE_PERMISSION_IN_ROLE);

        if(this.role.getRoleId() != null) {
            rolePermission.put(IRolePermissionServiceDto.ROLE_ID, this.role.getRoleId().toString());
        }
        
        permissionInRoleDataModel = new LazyDataSupportMapFilter<RolePermissionDto>(rolePermissionServiceDto);
        permissionInRoleDataModel.setFilters(rolePermission);
    }

    private void initPermissionNotinRole() {
        Map<String, String> permission = new HashMap<String, String>();
        permission.put(IRolePermissionServiceDto.QUERY_PERMISSION_ROLE_TYPE, IRolePermissionServiceDto.TYPE_PERMISSION_NOT_IN_ROLE);
         if(this.role.getRoleId() != null) {
            permission.put(IRolePermissionServiceDto.ROLE_ID, this.role.getRoleId().toString());
        }
        permissionNotInRoleDataList = new LazyDataSupportMapFilter<RolePermissionDto>(rolePermissionServiceDto);
        permissionNotInRoleDataList.setFilters(permission);
    }

    public void addPermissionToRole(ActionEvent actionEvent) {
        try {
            for (RolePermissionDto dto : permissionInRoleSelects) {
                System.out.println(dto.getPermissionName());
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRolePermissionId(this.rolePermissionService.getSequence(SystemDefine.SEQUENCE_ROLE_PERMISSION_ID).longValue());
                rolePermission.setPermissionId(dto.getPermissionId());
                rolePermission.setRoleId(this.role.getRoleId());
                rolePermission.setStatus(StatusDefine.activate);
                rolePermission.setAllowDelete(dto.getAllowDelete());
                System.out.println("dto.getAllowDelete(): " + dto.getAllowDelete());
                rolePermission.setAllowEdit(dto.getAllowEdit());
                rolePermission.setAllowView(dto.getAllowView());
                this.rolePermissionService.saveEntity(rolePermission);
            }
            initPermissionNotinRole();
            this.permissionInRoleSelects = null;
            permissionInRoleDataModel.getFilters().put(RolePermissionServiceDtoImpl.ROLE_ID, this.role.getRoleId().toString());
            permissionNotInRoleDataList.getFilters().put(RolePermissionServiceDtoImpl.ROLE_ID, this.role.getRoleId().toString());
        } catch (MySQLDuplicateRecordException ed) {
            System.out.println("user refresh page");
        } catch (Exception exv) {
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }


    }

    public void startAddPermissionToRole() {
        this.dilogViewPermission = "true";
    }

    public void viewRoleInfo() {
        leftFlag = Constraint.ROLE_INFOMATION;
    }

    public void viewPermissionOnRole() {
        leftFlag = Constraint.ROLE_ADD_PERMISSION;
    }

    public String onRecreatedRole() {
        this.role = new Role();
        return "edit_role_admin";
    }

    public String onStartCreatedNewPermission() {
//        role = new Role();
//        role.setStatus(StatusDefine.activate);
//        viewStage = "role_add_new";
        return "edit_role_admin";
    }

    public void deleteRole() {
        try {
            deletePublicRole();

        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("permission_notification_delete_not_success"));
        }
    }
    public void viewDetailPermissionAsPopup() {
        Long permissionId = selectedRolePermissionDto.getPermissionId();
        System.out.println("permissionId: " + permissionId);
        ControllerUtils.getRequestMap().put("permissionId", permissionId);
        RequestContext.getCurrentInstance().openDialog("edit_permission_detail");

    }

    public void deletePublicRole() {
        this.roleService.removeEntity(role);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_delete_success"));
        this.role = new Role();

    }

    public void searchRole() {
        Map filter = new HashMap<String, String>();
        filter.put(IRoleService.ROLE_NAME, this.searchData.getRoleName());
        if (this.searchData.getStatus() > 0) {
            filter.put(IRoleService.ROLE_STATUS, this.searchData.getStatus());
        }
        if (this.searchData.getRoleType() != null && this.searchData.getRoleType() > 0) {
            filter.put(IRoleService.ROLE_STATUS, this.searchData.getRoleType());
        }
        OrganizationController organizationController = ControllerUtils.getBean(ControllerName.ORGANIZATION_CONTROLLER);
        if (organizationController.getOrganization() != null) {
            filter.put(IRoleService.ROLE_ORGANIZATION_ID, organizationController.getOrganization().getOrganizationId().toString());
        }
//        OrganizationController organizationController = ControllerUtils.getBean(ControllerName.ORGANIZATION_CONTROLLER);
//        if (organizationController.getOrganizationSelection() != null) {
//            filter.put(IRoleService.ROLE_ORGANIZATION_ID, organizationController.getOrganizationSelection().getOrganizationId().toString());
//        }
        this.dataModelRole.setFilters(filter);
    }

    public void saveRole() {
        try {
            validateRole();
            boolean isupdate = true;
            if (this.role.getRoleId() == null) {
                isupdate = false;
                this.role.setRoleId(this.roleService.getSequence(SystemDefine.SEQUENCE_ROLE_ID).longValue());
            }
            this.roleService.saveEntity(role);
            if (isupdate) {
                ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_update_success"));

            } else {
                ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_create_success"));
            }

        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public void validateRole() {

        if (this.role.getRoleName() == null || this.role.getRoleName().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("role_not_enter_name"));
        }
        if (this.role.getStatus() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("role_not_enter_status"));
        }
        if (this.role.getRoleType() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("role_not_enter_type"));
        }

        this.role.setRoleName(DataValidator.deleteSpace(DataValidator.standardName(this.role.getRoleName())));
        Role checkRole = this.roleService.getRoleByName(this.role.getRoleName());


        if (checkRole == null) {
            return;
        }
        if (role.getRoleId() == null && checkRole.getRoleName().toLowerCase().compareTo(this.role.getRoleName().toLowerCase()) == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("role_name_enter_exists"));
        } else if (this.role.getRoleId() != null && checkRole.getRoleId().intValue() != this.role.getRoleId().intValue()
                && checkRole.getRoleName().toLowerCase().compareTo(this.role.getRoleName().toLowerCase()) == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("role_name_enter_exists"));
        }

    }

    public void activeRole() {
        if (this.role.getStatus().intValue() == StatusDefine.activate) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("role_notification_alreadyActive", role.getRoleName()));
            return;
        }
        this.role.setStatus(StatusDefine.activate);
        this.roleService.saveEntity(role);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("role_notification_active", role.getRoleName()));
    }

    public void inactiveRole() {
        if (this.role.getStatus().intValue() == StatusDefine.inactive) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("role_notification_alreadyInactive", role.getRoleName()));
            return;
        }
        this.role.setStatus(StatusDefine.inactive);
        this.roleService.saveEntity(role);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("role_notification_inactive", role.getRoleName()));
    }

    public void distroyRole() {
        if (this.role.getStatus().intValue() == StatusDefine.destroy) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("role_notification_alreadyDestroy", role.getRoleName()));
            return;
        }
        this.role.setStatus(StatusDefine.destroy);
        this.roleService.saveEntity(role);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("role_notification_destroy", role.getRoleName()));
    }

    public void onPermissionOnRoleRowSelect(SelectEvent event) {
        selectedRolePermissionDto = (RolePermissionDto) event.getObject();
        this.selectionPermissionsInRoleList.add(selectedRolePermissionDto);
    }

    public void onPermissionOnRoleRowUnselect(UnselectEvent event) {
        selectedRolePermissionDto = (RolePermissionDto) event.getObject();
        this.selectionPermissionsInRoleList.remove(selectedRolePermissionDto);
        selectedRolePermissionDto = null;
    }

    public void searchPermissionInRole() {
        this.permissionInRoleDataModel.getFilters().put(IRolePermissionServiceDto.PERMISSION_CODE, this.permissionInData.getPermissionCode());
        this.permissionInRoleDataModel.getFilters().put(IRolePermissionServiceDto.PERMISSION_NAME, this.permissionInData.getPermissionName());
        this.permissionInRoleDataModel.getFilters().put(IRolePermissionServiceDto.ROLE_PERMISSION_STATUS, this.permissionInData.getStatus().toString());
    }

    public void deletePermissionInRole() {
        RolePermission rolePermission = getRolePermissionOnMenuClick();
        this.rolePermissionService.removeEntity(rolePermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_delete_success"));
    }

    public void inActivePermissionInRole() {
        RolePermission rolePermission = getRolePermissionOnMenuClick();
        if (rolePermission.getStatus() == StatusDefine.inactive) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyInactive", selectionPermissionsInRoleList.get(0).getPermissionName()));
            return;
        }
        rolePermission.setStatus(StatusDefine.inactive);
        this.rolePermissionService.saveEntity(rolePermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_inactive", selectionPermissionsInRoleList.get(0).getPermissionName()));
    }

    public void changeExtensionPermissionMode(AjaxBehaviorEvent event) {
        RolePermissionDto rolePermissionDto = (RolePermissionDto) event.getComponent().getAttributes().get("permissionRoleDto");
        RolePermission rolePermission = this.cloneFromDto(rolePermissionDto);
        this.rolePermissionService.saveEntity(rolePermission);
    }

    public void activePermissionInRole() {
        RolePermission rolePermission = getRolePermissionOnMenuClick();
        if (rolePermission.getStatus() == StatusDefine.activate) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyActive", selectionPermissionsInRoleList.get(0).getPermissionName()));
            return;
        }
        rolePermission.setStatus(StatusDefine.activate);
        this.rolePermissionService.saveEntity(rolePermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_active", selectionPermissionsInRoleList.get(0).getPermissionName()));

    }

    private RolePermission getRolePermissionOnMenuClick() {
        RolePermissionDto rolePermissionDto = selectionPermissionsInRoleList.get(0);
        return cloneFromDto(rolePermissionDto);
    }

    private RolePermission cloneFromDto(RolePermissionDto rolePermissionDto) {
        RolePermission rolePermission = new RolePermission();

        rolePermission.setRolePermissionId(rolePermissionDto.getRolePermissionId());
        rolePermission.setRoleId(rolePermissionDto.getRoleId());
        rolePermission.setPermissionId(rolePermissionDto.getPermissionId());

        rolePermission.setAllowDelete(rolePermissionDto.getAllowDelete());
        rolePermission.setAllowEdit(rolePermissionDto.getAllowEdit());
        rolePermission.setAllowView(rolePermissionDto.getAllowView());

        rolePermission.setStatus(rolePermissionDto.getStatus());
        return rolePermission;
    }

    public void viewDetailRole() {
        ControllerUtils.getRequestMap().put(RESTORE_ROLE_ON_REQUEST, role);
    }

    public void closeAddPerToRolePopup() {
        this.dilogViewPermission = "false";
    }

    public void closeDetailRole() {
//       ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        this.role = new Role();
        this.dilogControl = "false";
    }

    public LazyDataSupportMapFilter<Role> getDataModelRole() {
        return dataModelRole;
    }

    public void setDataModelRole(LazyDataSupportMapFilter<Role> dataModelRole) {
        this.dataModelRole = dataModelRole;
    }

    public IRoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(IRoleService roleService) {
        this.roleService = roleService;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getSearchData() {
        return searchData;
    }

    public void setSearchData(Role searchData) {
        this.searchData = searchData;
    }

    public String getDilogControl() {
        return dilogControl;
    }

    public void setDilogControl(String dilogControl) {
        this.dilogControl = dilogControl;
    }


    public List<SelectItem> getRoleStatusList() {
        return roleStatusList;
    }

    public void setRoleStatusList(List<SelectItem> roleStatusList) {
        this.roleStatusList = roleStatusList;
    }

    public List<SelectItem> getRoleTypeList() {
        return roleTypeList;
    }

    public void setRoleTypeList(List<SelectItem> roleTypeList) {
        this.roleTypeList = roleTypeList;
    }

    public String getLeftFlag() {


        return leftFlag;
    }

    public void setLeftFlag(String leftFlag) {
        this.leftFlag = leftFlag;
    }

    public IPermissionService getPermissionService() {
        return permissionService;
    }

    public void setPermissionService(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public List<RolePermissionDto> getSelectionPermissionsInRoleList() {
        return selectionPermissionsInRoleList;
    }

    public void setSelectionPermissionsInRoleList(List<RolePermissionDto> selectionPermissionsInRoleList) {
        this.selectionPermissionsInRoleList = selectionPermissionsInRoleList;
    }

    public RolePermissionDto getSelectedRolePermissionDto() {
        return selectedRolePermissionDto;
    }

    public void setSelectedRolePermissionDto(RolePermissionDto selectedRolePermissionDto) {
        this.selectedRolePermissionDto = selectedRolePermissionDto;
    }

    public LazyDataSupportMapFilter<RolePermissionDto> getPermissionInRoleDataModel() {
        return permissionInRoleDataModel;
    }

    public void setPermissionInRoleDataModel(LazyDataSupportMapFilter<RolePermissionDto> permissionInRoleDataModel) {
        this.permissionInRoleDataModel = permissionInRoleDataModel;
    }

    public LazyDataSupportMapFilter<RolePermissionDto> getPermissionNotInRoleDataList() {
        return permissionNotInRoleDataList;
    }

    public void setPermissionNotInRoleDataList(LazyDataSupportMapFilter<RolePermissionDto> permissionNotInRoleDataList) {
        this.permissionNotInRoleDataList = permissionNotInRoleDataList;
    }

    public List<RolePermissionDto> getPermissionInRoleSelects() {

        return permissionInRoleSelects;
    }

    public void setPermissionInRoleSelects(List<RolePermissionDto> permissionInRoleSelects) {
        this.permissionInRoleSelects = permissionInRoleSelects;
    }

    public String getDilogViewPermission() {
        return dilogViewPermission;
    }

    public void setDilogViewPermission(String dilogViewPermission) {
        this.dilogViewPermission = dilogViewPermission;
    }

    public IRolePermissionServiceDto getRolePermissionServiceDto() {
        return rolePermissionServiceDto;
    }

    public void setRolePermissionServiceDto(IRolePermissionServiceDto rolePermissionServiceDto) {
        this.rolePermissionServiceDto = rolePermissionServiceDto;
    }

    public IRolePermissionService getRolePermissionService() {
        return rolePermissionService;
    }

    public void setRolePermissionService(IRolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    public Permission getPermissionInData() {
        return permissionInData;
    }

    public void setPermissionInData(Permission permissionInData) {
        this.permissionInData = permissionInData;
    }

    public Permission getPermissionNotInData() {
        return permissionNotInData;
    }

    public void setPermissionNotInData(Permission permissionNotInData) {
        this.permissionNotInData = permissionNotInData;
    }
}
