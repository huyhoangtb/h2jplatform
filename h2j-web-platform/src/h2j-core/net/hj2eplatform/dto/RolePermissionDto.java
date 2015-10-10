/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import net.hj2eplatform.core.models.PermissionDto;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author HuyHoang
 */
@Entity
public class RolePermissionDto extends PermissionDto {

    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_permission_id")
    private Long rolePermissionId;

    public Long getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(Long rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String getObjectKey() {
        StringBuilder str = new StringBuilder();
        str.append(this.permissionId.toString());
        if (roleId != null) {
            str.append(" ").append(this.roleId.toString());
        }
        return str.toString();
    }
}
