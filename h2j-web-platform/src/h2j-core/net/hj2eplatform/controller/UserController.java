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
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.dto.UserPermissionDto;
import net.hj2eplatform.core.exception.MySQLDuplicateRecordException;
import net.hj2eplatform.iservices.IGenerateService;
import net.hj2eplatform.iservices.IUserPermissionServiceDto;
import net.hj2eplatform.iservices.IRoleService;
import net.hj2eplatform.models.Role;
import net.hj2eplatform.models.UserPermission;
import net.hj2eplatform.models.UserRole;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.StatusDefine;
import net.hj2eplatform.core.utils.SystemDefine;
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
@Controller(ControllerName.USER_CONTROLLER)
@Scope("view")
public class UserController implements Serializable {

    private LazyDataSupportMapFilter<UserPermissionDto> permissionInUserDataModel;
    private LazyDataSupportMapFilter<UserPermissionDto> permissionNotInUserDataList;
    private UserPermissionDto userPermissionSelection;
    @Autowired
    private IUserPermissionServiceDto userPermissionServiceDto;
    @Autowired
    private IGenerateService service;
    @Autowired
    private IRoleService roleService;
    private HumanDto human;
    private String dilogViewPermission = "false";
    private List<SelectItem> userPermissionStatus;
    private List<SelectItem> permissionStatus;
    private UserPermissionDto[] userPermissionDtoSelection;
    private UserPermissionDto permissionInData;
    private UserPermissionDto permissionNotInData;
    private List<Role> userRoleList;
    public final static String ROLE4INSERT = "ROLE4INSERT";
    public final static String ROLE4REMOVE = "ROLE4REMOVE";

    @PostConstruct
    public void init() {
        this.userPermissionStatus = this.service.getParramItems(SystemDefine.USER_GROUP_CODE, SystemDefine.USER_PERMISSON_CODE_STATUS);
        this.permissionStatus = this.service.getParramItems(SystemDefine.PERMISSION_GROUP_CODE, SystemDefine.PERMISSION_CODE_STATUS);
        permissionInData = new UserPermissionDto();
        human = new HumanDto();
        permissionNotInData = new UserPermissionDto();
        initPermissionInUser();
        initPermissionNotInUser();

    }

    private void initPermissionInUser() {
        Map<String, String> userPermission = new HashMap<String, String>();
        userPermission.put(IUserPermissionServiceDto.QUERY_PERMISSION_USER_TYPE, IUserPermissionServiceDto.TYPE_PERMISSION_IN_USER);

        if (this.human.getUserId() != null) {
            userPermission.put(IUserPermissionServiceDto.USER_ID, this.human.getUserId().toString());
        }
        if (this.human.getOrganizationId() != null) {
            userPermission.put(IUserPermissionServiceDto.ORG_ID, this.human.getOrganizationId().toString());
        }

        permissionInUserDataModel = new LazyDataSupportMapFilter<UserPermissionDto>(userPermissionServiceDto);
        permissionInUserDataModel.setFilters(userPermission);
    }

    public List<Role> completeRole4User(String query) {
        return roleService.getRole4Complete(query, this.human.getUserId());
    }

    public void closeAddPerToRolePopup() {
        dilogViewPermission = "false";
    }

    public void roleOnSelect4User(SelectEvent event) {
        Role role = (Role) event.getObject();
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(service.getSequence(SystemDefine.SEQUENCE_USER_ROLE_ID).longValue());
        userRole.setUserId(this.human.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRole.setStatus(StatusDefine.activate);
        this.service.saveEntity(userRole);
    }

    public void roleOnUnselect4User(UnselectEvent event) {
        Role role = (Role) event.getObject();
        roleService.removeUserRole(this.human.getUserId(), role.getRoleId());
    }

    private void initPermissionNotInUser() {
        Map<String, String> userPermission = new HashMap<String, String>();
        userPermission.put(IUserPermissionServiceDto.QUERY_PERMISSION_USER_TYPE, IUserPermissionServiceDto.TYPE_PERMISSION_NOT_IN_USER);
        if (this.human.getUserId() != null) {
            userPermission.put(IUserPermissionServiceDto.USER_ID, this.human.getUserId().toString());
        }
        if (this.human.getOrganizationId() != null) {
            userPermission.put(IUserPermissionServiceDto.ORG_ID, this.human.getOrganizationId().toString());
        }

        permissionNotInUserDataList = new LazyDataSupportMapFilter<UserPermissionDto>(userPermissionServiceDto);
        permissionNotInUserDataList.setFilters(userPermission);
    }

    public void addPermissionToUser(ActionEvent actionEvent) {
        System.out.println("human" + human.getFullName());
        try {
            for (UserPermissionDto dto : userPermissionDtoSelection) {
                System.out.println("dto " + dto);
                UserPermission userPermission = new UserPermission();
                userPermission.setUserPermissionId(this.userPermissionServiceDto.getSequence(SystemDefine.SEQUENCE_USER_PERMISSION_ID).longValue());
                userPermission.setPermissionId(dto.getPermissionId());
                userPermission.setUserId(this.human.getUserId());
                userPermission.setAllowDelete(dto.getAllowDelete());
                userPermission.setAllowEdit(dto.getAllowEdit());
                userPermission.setAllowView(dto.getAllowView());
                userPermission.setStatus(StatusDefine.activate);
                this.service.saveEntity(userPermission);
            }
            initPermissionNotInUser();
            this.userPermissionDtoSelection = null;
        } catch (MySQLDuplicateRecordException ed) {
            System.out.println("user refresh page");
        } catch (Exception exv) {
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }

    }

    public void setHuman4InitPermission(HumanDto human) {
        this.human = human;
        this.userRoleList = roleService.getRole4User(this.human.getUserId());
        this.initPermissionInUser();
        this.initPermissionNotInUser();
    }

    public void startAddPermissionToUser() {
        this.dilogViewPermission = "true";
    }

    public void searchPermissionInUser() {
        this.permissionInUserDataModel.getFilters().put(IUserPermissionServiceDto.PERMISSION_CODE, this.permissionInData.getPermissionCode());
        this.permissionInUserDataModel.getFilters().put(IUserPermissionServiceDto.PERMISSION_NAME, this.permissionInData.getPermissionName());
        if (this.permissionInData.getStatus() != null) {
            this.permissionInUserDataModel.getFilters().put(IUserPermissionServiceDto.USER_PERMISSION_STATUS, this.permissionInData.getStatus().toString());
        }
    }

    public void deletePermissionInUser() {
        UserPermission userPermission = this.cloneFromDto(userPermissionSelection);
        this.service.removeEntity(userPermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_delete_success"));
    }

    public void inActivePermissionInUser() {
        UserPermission userPermission = this.cloneFromDto(userPermissionSelection);
        if (userPermission.getStatus() == StatusDefine.inactive) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyInactive", userPermissionSelection.getPermissionName()));
            return;
        }
        userPermission.setStatus(StatusDefine.inactive);
        this.service.saveEntity(userPermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_inactive", userPermissionSelection.getPermissionName()));
    }

    public void changeExtensionPermissionMode(AjaxBehaviorEvent event) {
        UserPermissionDto userPermissionDto = (UserPermissionDto) event.getComponent().getAttributes().get("permissionUserDto");
        UserPermission userPermission = this.cloneFromDto(userPermissionDto);
        this.service.saveEntity(userPermission);
    }

    public void activePermissionInUser() {
        UserPermission userPermission = this.cloneFromDto(userPermissionSelection);
        if (userPermission.getStatus() == StatusDefine.activate) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyActive", userPermissionSelection.getPermissionName()));
            return;
        }
        userPermission.setStatus(StatusDefine.activate);
        this.service.saveEntity(userPermission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_active", userPermissionSelection.getPermissionName()));

    }

    public void searchPermissionNotInUser() {
        this.permissionNotInUserDataList.getFilters().put(IUserPermissionServiceDto.PERMISSION_CODE, this.permissionNotInData.getPermissionCode());
        this.permissionNotInUserDataList.getFilters().put(IUserPermissionServiceDto.PERMISSION_NAME, this.permissionNotInData.getPermissionName());
        this.permissionNotInUserDataList.getFilters().put(IUserPermissionServiceDto.PERMISSION_STATUS, this.permissionNotInData.getStatus().toString());
    }

    private UserPermission cloneFromDto(UserPermissionDto userPermissionDto) {
        UserPermission userPermission = new UserPermission();

        userPermission.setUserPermissionId(userPermissionDto.getUserPermissionId());
        userPermission.setUserId(userPermissionDto.getUserId());
        userPermission.setPermissionId(userPermissionDto.getPermissionId());
        userPermission.setAllowDelete(userPermissionDto.getAllowDelete());
        userPermission.setAllowEdit(userPermissionDto.getAllowEdit());
        userPermission.setAllowView(userPermissionDto.getAllowView());
        userPermission.setStatus(userPermissionDto.getStatus());
        return userPermission;
    }

    public List<SelectItem> getUserPermissionStatus() {
        return userPermissionStatus;
    }

    public void setUserPermissionStatus(List<SelectItem> userPermissionStatus) {
        this.userPermissionStatus = userPermissionStatus;
    }

    public List<SelectItem> getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(List<SelectItem> permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public UserPermissionDto getPermissionInData() {
        return permissionInData;
    }

    public void setPermissionInData(UserPermissionDto permissionInData) {
        this.permissionInData = permissionInData;
    }

    public LazyDataSupportMapFilter<UserPermissionDto> getPermissionInUserDataModel() {
        return permissionInUserDataModel;
    }

    public void setPermissionInUserDataModel(LazyDataSupportMapFilter<UserPermissionDto> permissionInUserDataModel) {
        this.permissionInUserDataModel = permissionInUserDataModel;
    }

    public LazyDataSupportMapFilter<UserPermissionDto> getPermissionNotInUserDataList() {
        return permissionNotInUserDataList;
    }

    public void setPermissionNotInUserDataList(LazyDataSupportMapFilter<UserPermissionDto> permissionNotInUserDataList) {
        this.permissionNotInUserDataList = permissionNotInUserDataList;
    }

    public UserPermissionDto getUserPermissionSelection() {
        return userPermissionSelection;
    }

    public void setUserPermissionSelection(UserPermissionDto userPermissionSelection) {
        this.userPermissionSelection = userPermissionSelection;
    }

    public IUserPermissionServiceDto getUserPermissionServiceDto() {
        return userPermissionServiceDto;
    }

    public void setUserPermissionServiceDto(IUserPermissionServiceDto userPermissionServiceDto) {
        this.userPermissionServiceDto = userPermissionServiceDto;
    }

    public IGenerateService getService() {
        return service;
    }

    public void setService(IGenerateService service) {
        this.service = service;
    }

    public HumanDto getHuman() {
        return human;
    }

    public void setHuman(HumanDto human) {
        this.human = human;
    }

    public UserPermissionDto[] getUserPermissionDtoSelection() {
        return userPermissionDtoSelection;
    }

    public void setUserPermissionDtoSelection(UserPermissionDto[] userPermissionDtoSelection) {
        this.userPermissionDtoSelection = userPermissionDtoSelection;
    }

    public String getDilogViewPermission() {
        return dilogViewPermission;
    }

    public void setDilogViewPermission(String dilogViewPermission) {
        this.dilogViewPermission = dilogViewPermission;
    }

    public UserPermissionDto getPermissionNotInData() {
        return permissionNotInData;
    }

    public void setPermissionNotInData(UserPermissionDto permissionNotInData) {
        this.permissionNotInData = permissionNotInData;
    }

    public List<Role> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<Role> userRoleList) {
        this.userRoleList = userRoleList;
    }

}
