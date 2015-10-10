/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author HuyHoang
 */
public enum UserGender {

    UNKNOW(-1, "Chọn giới tính"), MALE(1, "Nam"), FEMALE(2, "Nữ");
    private Integer type;
    private String name;
    private static List<SelectItem> items;

    private UserGender(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public final int toInteger() {
        return this.type;
    }

    public final String toString() {
        return this.type.toString();
    }

    public static String toName(Integer type) {
        if(type == null) {
            return "Không xác định";
        }
        if(type.intValue() == MALE.toInteger()) {
            return MALE.getName();
        }
        if(type.intValue() == FEMALE.toInteger()) {
            return FEMALE.getName();
        }
        return "Không xác định";
    }
    
    public static List<SelectItem> toSelectItem() {
        if (items == null) {
            items = new ArrayList<SelectItem>();
            items.add(new SelectItem(MALE.type, MALE.getName()));
            items.add(new SelectItem(FEMALE.type, FEMALE.getName()));
        }

        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
