/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

/**
 *
 * @author Nguyen Huy Hoang
 */
public enum DefinePermission {

    PER_PRODUCT_INTRODUCTION("PER_PRODUCT_INTRODUCTION"), PER_SUB_PARTNER("PER_SUB_PARTNER"), ROLE_AS_ADMIN("ROLE_AS_ADMIN"), ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"), PER_SALE_ALL("PER_SALE_ALL"), PER_PRODUCTION_ALL("PER_PRODUCTION_ALL"), PER_OPERATOR_ALL("PER_OPERATOR_ALL"), PER_ACCOUNT_ALL("PER_ACCOUNT_ALL"), ROLE_MARKETING_ONLINE("ROLE_MARKETING_ONLINE"), CUSTOMER("CUSTOMER");
    private String permission;

    private DefinePermission(String permission) {
        this.permission = permission;
    }

    public String toString() {
        return this.permission;
    }

    public boolean compare(DefinePermission permission) {
        if (this.permission.compareTo(permission.toString()) == 0) {
            return true;
        }
        return false;
    }
}
