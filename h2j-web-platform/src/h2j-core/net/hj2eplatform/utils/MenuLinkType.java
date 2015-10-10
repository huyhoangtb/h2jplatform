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
public enum MenuLinkType {

    UNKNOW(-1, "Chọn kiểu menu"), URL_LINK(0, "Liên kết URL"), TOUR_GROUP_LINK(11, "Liên kết nhóm tour")
    ,TOUR(1, "Liên kết tour"), ARTICLE(3, "Liên kết bài viết"), ARTICLE_CATEGORY(4, "Liên kết danh mục bài viết"),
    CONTACT_US(21, "Trang liên hệ"),CUSTOMIZE_TOUR(25, "Tour tự tạo"), PLANCE_VISIT_TYPE_IN_COUNTRY(22, "Tour trong nước"),
    PLANCE(23, "Địa danh"), TOUR_METHOD_PRIVATE(24, "Hình thức đi tour riêng"), TOUR_METHOD_GROUP(26, "Hình thức đi tour ghép")
    , PLANCE_VISIT_TYPE_OUT_COUNTRY(27, "Tour quốc tế"), TOUR_DISCOUNT_CUSTOMER(28, "Tour khuyến mại");
    private Integer type;
    private String name;
    private static List<SelectItem> items;

    private MenuLinkType(Integer type, String name) {
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
            items.add(new SelectItem(UNKNOW.type, UNKNOW.getName()));
            items.add(new SelectItem(URL_LINK.type, URL_LINK.getName()));
            items.add(new SelectItem(TOUR_GROUP_LINK.type, TOUR_GROUP_LINK.getName()));
            items.add(new SelectItem(TOUR.type, TOUR.getName()));
            items.add(new SelectItem(ARTICLE.type, ARTICLE.getName()));
            items.add(new SelectItem(ARTICLE_CATEGORY.type, ARTICLE_CATEGORY.getName()));
            items.add(new SelectItem(CONTACT_US.type, CONTACT_US.getName()));
            items.add(new SelectItem(CUSTOMIZE_TOUR.type, CUSTOMIZE_TOUR.getName()));
            items.add(new SelectItem(PLANCE_VISIT_TYPE_IN_COUNTRY.type, PLANCE_VISIT_TYPE_IN_COUNTRY.getName()));
            items.add(new SelectItem(PLANCE_VISIT_TYPE_OUT_COUNTRY.type, PLANCE_VISIT_TYPE_OUT_COUNTRY.getName()));
            items.add(new SelectItem(PLANCE.type, PLANCE.getName()));
            items.add(new SelectItem(TOUR_METHOD_PRIVATE.type, TOUR_METHOD_PRIVATE.getName()));
            items.add(new SelectItem(TOUR_METHOD_GROUP.type, TOUR_METHOD_GROUP.getName()));
            items.add(new SelectItem(TOUR_DISCOUNT_CUSTOMER.type, TOUR_DISCOUNT_CUSTOMER.getName()));
        }

        return items;
    }
    
    public static String toName(Integer type) {
        for(SelectItem item : toSelectItem()) {
            int value = ((Integer)item.getValue()).intValue();
            if(value == type) {
                return item.getLabel();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
