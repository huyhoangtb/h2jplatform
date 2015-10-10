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
public enum UserStatus {

    UNKNOW(-1, "Chọn trạng thái"), ACTIVE(1, "Hoạt động"), INACTIVE(0, "Ngừng hoạt động"), UN_CREATE_ACCOUNT(-2, "TK chưa Hiệu lực");
    private Integer type;
    private String name;
    private static List<SelectItem> items;

    private UserStatus(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public final int toInteger() {
        return this.type;
    }

    public final String toString() {
        return this.type.toString();
    }

    public static List<SelectItem> toSelectItem() {
        if (items == null) {
            items = new ArrayList<SelectItem>();
            items.add(new SelectItem(ACTIVE.type, ACTIVE.getName()));
            items.add(new SelectItem(INACTIVE.type, INACTIVE.getName()));
        }

        return items;
    }
    public static String getStatusName(int status) {
        for(SelectItem item : toSelectItem()) {
            if(((Integer)item.getValue()).intValue() == status) {
                return item.getLabel();
            }
        }
        return "trạng thái ? " + status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
