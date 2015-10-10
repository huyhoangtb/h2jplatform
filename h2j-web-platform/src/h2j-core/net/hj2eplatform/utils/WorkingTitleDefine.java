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
public enum WorkingTitleDefine {

    STAFF(1, "Nhân viên"), SALE_STAFF(2, "Nhân viên bán hàng"), OPERATOR_STAFF(3, "Điều hành"),PRODUCTION_MANAGER(21, "Nhân viên QL Sản phẩm"),ACCOUNTING(22, "Kế toán"),
    DRIVER(4, "Lái xe"), GUILDE(5, "Hướng dẫn viên"),
    GENERALINCHIEF(6, "Trưởng phòng"), VICE_CHIEF(7, "Phó phòng"), HOTEL_MANAGER(14, "Quản lý khách sạn"),
    RESTAURANT_MANAGER(15, "Quản lý nhà hàng"), RECEIPTION(16, "Lễ tân"),
    CENTER_DERECTOR(8, "Giám Đốc trung tâm"), VICE_CENTER_DERECTOR(9, "Phó giám đốc trung tâm"),
    DERECTOR(10, "Giám đốc"), VICE_DERECTOR(11, "Phó giám đốc"), GENERATE_DERECTOR(12, "Tổng giám đốc"),
    VICE_GENERATE_DERECTOR(13, "Phó tổng giám đốc");
    private Integer type;
    private String name;
    public static List<SelectItem> items;

    private WorkingTitleDefine(Integer type, String name) {
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
//        items.add(new SelectItem(-1, "Chọn sản phẩm - dịch vụ"));
            items.add(new SelectItem(STAFF.name, STAFF.getName()));
            items.add(new SelectItem(SALE_STAFF.name, SALE_STAFF.getName()));
            items.add(new SelectItem(OPERATOR_STAFF.name, OPERATOR_STAFF.getName()));
            items.add(new SelectItem(PRODUCTION_MANAGER.name, PRODUCTION_MANAGER.getName()));
            items.add(new SelectItem(ACCOUNTING.name, ACCOUNTING.getName()));
            items.add(new SelectItem(DRIVER.name, DRIVER.getName()));
            items.add(new SelectItem(GUILDE.name, GUILDE.getName()));
            items.add(new SelectItem(RECEIPTION.name, RECEIPTION.getName()));
             items.add(new SelectItem(HOTEL_MANAGER.name, HOTEL_MANAGER.getName()));
            items.add(new SelectItem(RESTAURANT_MANAGER.name, RESTAURANT_MANAGER.getName()));
            items.add(new SelectItem(GENERALINCHIEF.name, GENERALINCHIEF.getName()));
            items.add(new SelectItem(VICE_CHIEF.name, VICE_CHIEF.getName()));
            items.add(new SelectItem(CENTER_DERECTOR.name, CENTER_DERECTOR.getName()));
            items.add(new SelectItem(VICE_CENTER_DERECTOR.name, VICE_CENTER_DERECTOR.getName()));
            items.add(new SelectItem(DERECTOR.name, DERECTOR.getName()));
            items.add(new SelectItem(VICE_DERECTOR.name, VICE_DERECTOR.getName()));
            items.add(new SelectItem(GENERATE_DERECTOR.name, GENERATE_DERECTOR.getName()));
            items.add(new SelectItem(VICE_GENERATE_DERECTOR.name, VICE_GENERATE_DERECTOR.getName()));
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
