/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public enum DayOfWeek {

    All(0), Sunday(1), Monday(2), Tuesday(3), Wednesday(4), Thursday(5), Friday(6), Saturday(7);
    private Integer type;

    private DayOfWeek(Integer type) {
        this.type = type;
    }

    public int toInteger() {
        return this.type;
    }

    public String toString() {
        return this.type.toString();
    }
    
    

    public boolean checkDay(int dayOfWeek) {
        if (type.intValue() == dayOfWeek) {
            return true;
        }
        return false;
    }
}
