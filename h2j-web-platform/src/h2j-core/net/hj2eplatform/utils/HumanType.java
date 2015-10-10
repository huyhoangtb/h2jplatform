/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

/**
 *
 * @author HuyHoang
 */
public enum HumanType {
    // fix type org va staff de de phan biet
     DEPUTY_STAFF(1), STAFF(2), RETAIL_CUSTOMER(3);
    private Integer type;
    private HumanType(Integer type) {
        this.type = type;
    }
    public int toInteger() {
        return this.type;
    }
    public String toString() {
        return this.type.toString();
    }
}
