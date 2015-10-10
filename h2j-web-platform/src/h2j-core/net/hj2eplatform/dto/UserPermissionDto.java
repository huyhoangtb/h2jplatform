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
public class UserPermissionDto extends PermissionDto {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_permission_id")
    private Long userPermissionId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Long userPermissionId) {
        this.userPermissionId = userPermissionId;
    }
    
    @Override
    public String getObjectKey() {
        StringBuilder str = new StringBuilder();
        str.append(this.permissionId.toString());
        if (userId != null) {
            str.append(" ").append(this.userId.toString());
        }
        System.out.println("str: " + str);
        return str.toString();
    }
    
}
