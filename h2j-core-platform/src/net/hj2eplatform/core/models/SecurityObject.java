/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public abstract class SecurityObject implements UserDetails {

    private static final String VIEW = "_VIEW";
    private static final String DELETE = "_DELETE";
    private static final String EDIT = "_EDIT";
    @Transient
    private List<SecurityPermission> permissionList;

    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityArray = new ArrayList<GrantedAuthority>();
        for (SecurityPermission permission : permissionList) {
            grantedAuthorityArray.add(new GrantedAuthorityImpl(permission.getPermissionCode()));
            if(permission.getFullControl() == null) {
                continue;
            }
            if (!permission.getFullControl()) {
                continue;
            }
            if (permission.getAllowView()) {
                grantedAuthorityArray.add(new GrantedAuthorityImpl(permission.getPermissionCode() + VIEW));
            }
            if (permission.getAllowDelete()) {
                grantedAuthorityArray.add(new GrantedAuthorityImpl(permission.getPermissionCode() + DELETE));
            }
            if (permission.getAllowEdit()) {
                grantedAuthorityArray.add(new GrantedAuthorityImpl(permission.getPermissionCode() + EDIT));
            }
        }
        return grantedAuthorityArray;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<SecurityPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<SecurityPermission> permissionList) {
        this.permissionList = permissionList;
    }
}
