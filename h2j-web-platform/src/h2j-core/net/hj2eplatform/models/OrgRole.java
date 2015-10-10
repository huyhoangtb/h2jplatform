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
@Table(name = "org_role")
@NamedQueries({
    @NamedQuery(name = "OrgRole.findAll", query = "SELECT o FROM OrgRole o")})
public class OrgRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "org_role_id")
    private Long orgRoleId;
    @Column(name = "organization_id")
    private Long organizationId;
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "allow_view")
    private Boolean allowView;
    @Column(name = "allow_delete")
    private Boolean allowDelete;
    @Column(name = "allow_edit")
    private Boolean allowEdit;
    @Column(name = "status")
    private Integer status;

    public OrgRole() {
    }

    public OrgRole(Long orgRoleId) {
        this.orgRoleId = orgRoleId;
    }

    public Long getOrgRoleId() {
        return orgRoleId;
    }

    public void setOrgRoleId(Long orgRoleId) {
        this.orgRoleId = orgRoleId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgRoleId != null ? orgRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgRole)) {
            return false;
        }
        OrgRole other = (OrgRole) object;
        if ((this.orgRoleId == null && other.orgRoleId != null) || (this.orgRoleId != null && !this.orgRoleId.equals(other.orgRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.OrgRole[ orgRoleId=" + orgRoleId + " ]";
    }
}
