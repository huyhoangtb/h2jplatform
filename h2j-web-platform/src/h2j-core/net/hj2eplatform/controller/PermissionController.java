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
import javax.faces.model.SelectItem;
import net.hj2eplatform.dto.PermissionLazyDataModel;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IPermissionService;
import net.hj2eplatform.models.Permission;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.StatusDefine;
import net.hj2eplatform.core.utils.SystemDefine;
import net.hj2eplatform.iservices.IGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author HuyHoang
 */
@Controller(ControllerName.PERMISION_CONTROLLER)
@Scope("view")
public class PermissionController implements Serializable {

    private PermissionLazyDataModel permissionLazyDataModel;
    @Autowired
    private IPermissionService permissionService;
      @Autowired
    private IGenerateService generateService;
    private Permission permission;
    private Permission searchData;
    private String dilogControl = "false";
    private List<SelectItem> permissionStatusList;
    private List<SelectItem> permissionStyleList;

    @PostConstruct
    public void init() {
        this.permissionLazyDataModel = new PermissionLazyDataModel(permissionService);
        String perId = ControllerUtils.getRequestParameter("perId");
        if (perId != null) {
            try {
                permission = permissionService.loadEntity(Permission.class, Long.valueOf(perId));
            } catch (Exception ex) {
            }
        }
        if (permission == null) {
            permission = new Permission();
            permission.setStatus(StatusDefine.activate);
        }

        searchData = new Permission();
        this.permissionStatusList = this.generateService.getParramItems(SystemDefine.PERMISSION_GROUP_CODE, SystemDefine.PERMISSION_CODE_STATUS);
        this.permissionStyleList = this.generateService.getParramItems(SystemDefine.PERMISSION_GROUP_CODE, SystemDefine.PERMISSION_CODE_STYLE);
    }

    public String onStartCreatedNewPermission() {
        permission = new Permission();
        permission.setStatus(StatusDefine.activate);
        return "edit_permission_admin";
    }

    public void deletePermission() {
        try {
            deletePublicPermission();
        } catch (Exception ex) {
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("permission_notification_delete_not_success"));
        }
    }

    public void deletePublicPermission() {
        this.permissionService.removeEntity(permission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_delete_success"));
        this.permission = new Permission();
        this.dilogControl = "false";
    }

    public void searchPermission() {
        Map filter = new HashMap<String, String>();
        filter.put(PermissionLazyDataModel.PERMISSION_NAME, this.searchData.getPermissionName());
        filter.put(PermissionLazyDataModel.PERMISSION_CODE, this.searchData.getPermissionCode());
        if (this.searchData.getStatus() != -1) {
            filter.put(PermissionLazyDataModel.PERMISSION_STATUS, this.searchData.getStatus().toString());
        }
        this.permissionLazyDataModel.setFilters(filter);
    }

    public String savePermission() {
        try {
            validatePermission();
            boolean update = true;
            if (this.permission.getPermissionId() == null) {
                update = false;
                this.permission.setPermissionId(this.permissionService.getSequence(SystemDefine.SEQUENCE_PERMISSION_ID).longValue());
            }

            AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
            if (authenticationController.getOrganization() != null) {
                permission.setOrganizationId(authenticationController.getOrganization().getOrganizationId());
            }
//            if (this.permission.getModuleId() == -1) {
//                this.permission.setModuleId(null);
//            }
            this.permissionService.saveEntity(permission);
            if (update) {
                ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_update_success"));
            } else {
                ControllerUtils.addSuccessMessage(ResourceMessages.getResource("permission_notification_create_success"));
//                this.permission = new Permission();
                return ControllerUtils.forwardToPage("/app/administrator/core/permission/edit").append("&perId=").append(this.permission.getPermissionId()).toString();
            }
            
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
        return null;
    }

    public void validatePermission() {

        if (this.permission.getPermissionName() == null || this.permission.getPermissionName().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("permission_not_enter_name"));
        }
        if (this.permission.getStatus() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("permission_not_enter_status"));
        }
        if (this.permission.getType() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("permission_not_enter_style"));
        }

        this.permission.setPermissionName(DataValidator.deleteSpace(DataValidator.standardName(this.permission.getPermissionName())));
        Permission per = null;


        per = this.permissionService.getPermissionByName(this.permission.getPermissionName());
        if (per != null) {
            if (this.permission.getPermissionId() == null && per.getPermissionName().toLowerCase().compareTo(this.permission.getPermissionName().toLowerCase()) == 0) {
                throw new ValidateInputException(ResourceMessages.getResource("permission_exist_permission_name"));
            } else if (this.permission.getPermissionId() != null && per.getPermissionId().intValue() != this.permission.getPermissionId().intValue()
                    && per.getPermissionName().toLowerCase().compareTo(this.permission.getPermissionName().toLowerCase()) == 0) {
                throw new ValidateInputException(ResourceMessages.getResource("permission_exist_permission_name"));
            }
        }


        if (this.permission.getPermissionCode() == null || this.permission.getPermissionCode().trim().compareTo("") == 0) {
            this.permission.setPermissionCode(this.permissionService.getSequenceCode(SystemDefine.SEQUENCE_PERMISSION_CODE));
        } else {
            this.permission.setPermissionCode(this.permission.getPermissionCode().trim().toUpperCase());
            per = this.permissionService.getPermissionByCode(this.permission.getPermissionCode());
            if (per != null) { // ton tai code truoc do
                if (this.permission.getPermissionId() == null && per.getPermissionCode().compareTo(this.permission.getPermissionCode()) == 0) {
                    // them moi thi duong nhien la trung ma
                    throw new ValidateInputException(ResourceMessages.getResource("permission_exist_permission_code"));
                } else if (this.permission.getPermissionId() != null && per.getPermissionId().intValue() != this.permission.getPermissionId().intValue()
                        && per.getPermissionCode().compareTo(this.permission.getPermissionName()) == 0) {
                    // truong hop upDate thi chi chung ma neu id bang nhau
                    throw new ValidateInputException(ResourceMessages.getResource("permission_exist_permission_code"));
                }
            }
        }

    }

    public void activePermission() {
        if (this.permission.getStatus().intValue() == StatusDefine.activate) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyActive", permission.getPermissionName()));
            return;
        }
        this.permission.setStatus(StatusDefine.activate);
        this.permissionService.saveEntity(permission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_active", permission.getPermissionName()));
    }

    public void inactivePermission() {
        if (this.permission.getStatus().intValue() == StatusDefine.inactive) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyInactive", permission.getPermissionName()));
            return;
        }
        this.permission.setStatus(StatusDefine.inactive);
        this.permissionService.saveEntity(permission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_inactive", permission.getPermissionName()));
    }

    public void distroyPermission() {
        if (this.permission.getStatus().intValue() == StatusDefine.destroy) {
            ControllerUtils.addAlertMessage(ResourceMessages.getString("permission_notification_alreadyDestroy", permission.getPermissionName()));
            return;
        }
        this.permission.setStatus(StatusDefine.destroy);
        this.permissionService.saveEntity(permission);
        ControllerUtils.addSuccessMessage(ResourceMessages.getString("permission_notification_destroy", permission.getPermissionName()));
    }

    public void viewDetailPermission() {
        this.dilogControl = "true";
    }

    public void closeDetailPermission() {
//       ControllerUtils.getRequest().getSession().setAttribute(ORGANIZATION_OBJ, null);
        this.permission = new Permission();
        this.dilogControl = "false";
    }

    public PermissionLazyDataModel getPermissionLazyDataModel() {
        return permissionLazyDataModel;
    }

    public void setPermissionLazyDataModel(PermissionLazyDataModel permissionLazyDataModel) {
        this.permissionLazyDataModel = permissionLazyDataModel;
    }

    public IPermissionService getPermissionService() {
        return permissionService;
    }

    public void setPermissionService(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Permission getSearchData() {
        return searchData;
    }

    public List<SelectItem> getPermissionStyleList() {
        return permissionStyleList;
    }

    public void setPermissionStyleList(List<SelectItem> permissionStyleList) {
        this.permissionStyleList = permissionStyleList;
    }

    public void setSearchData(Permission searchData) {
        this.searchData = searchData;
    }

    public String getDilogControl() {
        return dilogControl;
    }

    public void setDilogControl(String dilogControl) {
        this.dilogControl = dilogControl;
    }

    public List<SelectItem> getPermissionStatusList() {
        return permissionStatusList;
    }

    public void setPermissionStatusList(List<SelectItem> permissionStatusList) {
        this.permissionStatusList = permissionStatusList;
    }
}
