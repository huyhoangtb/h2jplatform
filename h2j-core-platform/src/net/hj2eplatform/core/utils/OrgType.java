/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

/**
 *
 * @author HuyHoang
 */
public enum OrgType {
    // fix type org va staff de de phan biet
    ORG_H2J_CENTER(1), ORG_CUSTOMER(2), SUPPLIER(3), AGENCY(4);
//    , TYPE_RETAIL_CUSTOMER(3), TYPE_DEPUTY_STAFF(1), TYPE_STAFF(2);
    private Integer type;
    private OrgType(Integer type) {
        this.type = type;
    }
    public int toInteger() {
        return this.type;
    }
    public String toString() {
        return this.type.toString();
    }
}
