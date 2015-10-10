/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

/**
 *
 * @author HuyHoang
 */
public enum DomainType {

    ADMINISTRATOR(1), CUSTOMER_SITE(2);
    private Integer type;

    private DomainType(Integer type) {
        this.type = type;
    }

    public final int toInteger() {
        return this.type;
    }

    public final String toString() {
        return this.type.toString();
    }

    public final static String toName(Integer type) {
        switch (type.intValue()) {
            case 1:
                return "Site Admin của hệ thống";
            case 2:
                return "Site giới thiệu sản phẩm dịch vụ";
        }
        return "Khách lẻ";
    }

    public final String toName() {
        return toName(type);
    }
}
