/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

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
@Table(name = "org_permission")
@NamedQueries({
    @NamedQuery(name = "OrgPermission.findAll", query = "SELECT o FROM OrgPermission o")})
public class OrgPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "org_permission_id")
    private Long orgPermissionId;
    @Column(name = "allow_view")
    private Boolean allowView;
    @Column(name = "allow_delete")
    private Boolean allowDelete;
    @Column(name = "allow_edit")
    private Boolean allowEdit;
    @Column(name = "permission_id")
    private Boolean permissionId;
    @Column(name = "organization_id")
    private Boolean organizationId;
 
    public OrgPermission() {
    }

    public OrgPermission(Long orgPermissionId) {
        this.orgPermissionId = orgPermissionId;
    }

    public Long getOrgPermissionId() {
        return orgPermissionId;
    }

    public void setOrgPermissionId(Long orgPermissionId) {
        this.orgPermissionId = orgPermissionId;
    }

    public Boolean getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Boolean permissionId) {
        this.permissionId = permissionId;
    }

    public Boolean getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Boolean organizationId) {
        this.organizationId = organizationId;
    }

   
    public Boolean getAllowView() {
        return allowView;
    }

    public void setAllowView(Boolean allowView) {
        this.allowView = allowView;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Boolean getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgPermissionId != null ? orgPermissionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgPermission)) {
            return false;
        }
        OrgPermission other = (OrgPermission) object;
        if ((this.orgPermissionId == null && other.orgPermissionId != null) || (this.orgPermissionId != null && !this.orgPermissionId.equals(other.orgPermissionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.OrgPermission[ orgPermissionId=" + orgPermissionId + " ]";
    }
}
