/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.faces.model.SelectItem;
import javax.persistence.Transient;
import net.hj2eplatform.controller.LocationController;
import net.hj2eplatform.core.utils.SystemConfig;

/**
 *
 * @author HuyHoang
 */
public class LanguagleBaseEntity implements Serializable {

    @Transient
    private List<SelectItem> avaiableLanguagle = new ArrayList<SelectItem>();
    @Transient
    private String currentLanguagle = SystemConfig.getResource("hj2eplatform.languagle.default");
    @Transient
    private Locale showLocale;

    public  void instance() {
        
        List<SelectItem> avaiableLanguagle = new ArrayList<SelectItem>();
        String str = SystemConfig.getResource("hj2eplatform.languagle.translateDefault");
        String[] stra = str.split(",");
        for (String st : stra) {
            avaiableLanguagle.add(LocationController.getSelectItem(st));
        }
        this.setAvaiableLanguagle(avaiableLanguagle);
        if (this.showLocale == null) {
            this.showLocale = new Locale(SystemConfig.getResource("hj2eplatform.languagle.default"));
        }
    }

    public void setAvaiableLanguagle(List<SelectItem> avaiableLanguagle) {
        this.avaiableLanguagle = avaiableLanguagle;
    }

    public String getCurrentLanguagle() {
        return currentLanguagle;
    }

    public void setCurrentLanguagle(String currentLanguagle) {
        this.currentLanguagle = currentLanguagle;
    }

    public Locale getShowLocale() {
        return showLocale;
    }

    public void setShowLocale(Locale showLocale) {
        this.showLocale = showLocale;
    }
    public Boolean getCheckDefaultLanguagle() {
        return  (SystemConfig.getResource("hj2eplatform.languagle.default").compareTo(currentLanguagle) == 0);
    }

    public List<SelectItem> getAvaiableLanguagle() {
        return avaiableLanguagle;
    }
    
}
