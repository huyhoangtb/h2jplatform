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
public enum PartnerContractType {

    SUB_PARTNER(0, "Đối tác không có hợp đồng, chỉ được mua và bán sản phẩm của nhà cung cấp khác"), 
    PRODUCT_INTRODUCTION_PARTNER(1, "Đối tác giới thiệu sản phẩm, được nhập sản phẩm, xem đơn hàng và mua sản phẩm của nhà cung cấp khác"),
    PRODUCT_INTRODUCTION_PARTNER_TEST_SOFTWARE(2, "Đối tác giới thiệu sản phẩm, dùng thử 15 ngày chi phí 200k."),
    CONTRACT_PARTNER_NO1(3, "Đối tác có hợp đồng 500k/tháng + 5k/đơn hàng."),
    CONTRACT_PARTNER_NO2(4, "Đối tác có hợp đồng dịch vụ 2.000.000 vnđ/tháng/doanh nghiệp dưới 35 người"),
    CONTRACT_PARTNER_NO3(5, "Đối tác có hợp đồng dịch vụ 3.200.000 vnđ/tháng/doanh nghiệp dưới 60 người"),
    CONTRACT_PARTNER_NO4(6, "Liên hệ nếu tổng số lượng nhân viên lớn hơn 60 người");
    private Integer type;
    private String name;
    public static List<SelectItem> items;

    private PartnerContractType(Integer type, String name) {
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
            items.add(new SelectItem(SUB_PARTNER.name, SUB_PARTNER.getName()));
            items.add(new SelectItem(PRODUCT_INTRODUCTION_PARTNER.name, PRODUCT_INTRODUCTION_PARTNER.getName()));
            items.add(new SelectItem(CONTRACT_PARTNER_NO1.name, CONTRACT_PARTNER_NO1.getName()));
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
