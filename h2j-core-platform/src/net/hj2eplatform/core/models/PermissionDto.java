/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import net.hj2eplatform.core.component.LazyObject;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
@Entity
public class PermissionDto extends LazyObject implements Serializable  {

    @Id
    @Column(name = "permission_id")
    protected Long permissionId;
    @Column(name = "organization_id")
    protected Long organizationId;
    @Column(name = "permission_name")
    protected String permissionName;
    @Column(name = "permission_code")
    protected String permissionCode;
     @Column(name = "full_control")
    private Boolean fullControl;
    @Column(name = "type")
    protected Integer type;
    @Column(name = "status")
    protected Integer status;
    @Column(name = "comments")
    protected String comments;
    @Column(name = "allow_view")
    protected Boolean allowView;
    @Column(name = "allow_delete")
    protected Boolean allowDelete;
    @Column(name = "allow_edit")
    protected Boolean allowEdit;
   

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Boolean getFullControl() {
        return fullControl;
    }

    public void setFullControl(Boolean fullControl) {
        this.fullControl = fullControl;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getAllowView() {
        return allowView;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public Boolean getAllowEdit() {
        return allowEdit;
    }

    public Boolean isAllowView() {
        return allowView;
    }

    public void setAllowView(Boolean allowView) {
        this.allowView = allowView;
    }

    public Boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public String getObjectKey() {

        return this.permissionId.toString();
    }
    
}
