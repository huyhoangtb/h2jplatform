/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.utils;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Huy Hoang
 */
public class StatusDefine {
    public static final int isDefault=-1;
    public static final int activate=1;
    public static final int inactive=2;
    public static final int destroy=3;
    public static final String ACTIVE_LABLE=ResourceMessages.getResource("status_active");
    public static final String INACTIVE_LABLE=ResourceMessages.getResource("status_inactive");
    public static final String DESTROY_LABLE=ResourceMessages.getResource("status_destroy");
    public static  List<SelectItem> getNormalStatus() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(activate, ACTIVE_LABLE));
        items.add(new SelectItem(inactive, INACTIVE_LABLE));
        return items;
    }
}
