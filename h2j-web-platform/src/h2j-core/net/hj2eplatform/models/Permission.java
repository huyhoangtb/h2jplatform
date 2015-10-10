/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import net.hj2eplatform.core.models.BaseEntity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "permission")
@NamedQueries({
    @NamedQuery(name = "Permission.findAll", query = "SELECT p FROM Permission p")})
public class Permission extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "permission_id")
    private Long permissionId;
    @Column(name = "organization_id")
    private Long organizationId;
//    @Column(name = "module_id")
//    private Long moduleId;
    @Column(name = "permission_name")
    private String permissionName;
    @Column(name = "permission_code")
    private String permissionCode;
    @Column(name = "full_control")
    private Boolean fullControl;
    @Column(name = "type")
    private Integer type = TYPE_CUSTOM;
    @Column(name = "status")
    private Integer status;
    @Column(name = "comments")
    private String comments;

    public final static int TYPE_CUSTOM = 2;
    public final static int TYPE_SYSTEM = 1;
  

    public Permission() {
    }

    public Permission(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

//    public Long getModuleId() {
//        return moduleId;
//    }
//
//    public void setModuleId(Long moduleId) {
//        this.moduleId = moduleId;
//    }

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

    public Boolean getFullControl() {
        return fullControl;
    }

    public void setFullControl(Boolean fullControl) {
        this.fullControl = fullControl;
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

    public String getShortComments() {
        if (comments != null && comments.length() > 200) {
            return comments.substring(0, 200) + "...";
        }
        return comments;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permissionId != null ? permissionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission) object;
        if ((this.permissionId == null && other.permissionId != null) || (this.permissionId != null && !this.permissionId.equals(other.permissionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.Permission[ permissionId=" + permissionId + " ]";
    }

    @Override
    public String getObjectKey() {
        if(this.permissionId == null) {
            return null;
        }
        return this.permissionId.toString();
    }


}
