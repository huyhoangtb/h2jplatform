/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import net.hj2eplatform.utils.LanguagleBaseEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("languagleContext")
@Scope("session")
public class LanguagleContext implements Serializable {

    private List<String> selectedLanguagleAvaiable;
    private LanguagleBaseEntity languagle;

    @PostConstruct 
    private void init() {
        languagle = new LanguagleBaseEntity();
        languagle.instance();
        selectedLanguagleAvaiable = new ArrayList<String>();
        String str = SystemConfig.getResource("hj2eplatform.languagle.translateDefault");
        String[] stra = str.split(",");
        for (String st : stra) {
            selectedLanguagleAvaiable.add(st);
        }
    }

    public static String getCurrentLanguagleValue() {
        LanguagleContext controller = ControllerUtils.getBean(ControllerName.LANGUAGLE_CONTEXT);
        return controller.languagle.getCurrentLanguagle();
    }

    public void addTranslateArticle() {
        List<SelectItem> avaiableLanguagle = new ArrayList<SelectItem>();
        for (String str : selectedLanguagleAvaiable) {
            avaiableLanguagle.add(LocationController.getSelectItem(str));
            languagle.setAvaiableLanguagle(avaiableLanguagle);
        }
    }

    public List<SelectItem> getCountryList() {
        return LocationController.instalLocationListItem();
    }

    public List<String> getSelectedLanguagleAvaiable() {
        return selectedLanguagleAvaiable;
    }

    public void setSelectedLanguagleAvaiable(List<String> selectedLanguagleAvaiable) {
        this.selectedLanguagleAvaiable = selectedLanguagleAvaiable;
    }

    public LanguagleBaseEntity getLanguagle() {
        return languagle;
    }

    public void setLanguagle(LanguagleBaseEntity languagle) {
        this.languagle = languagle;
    }
}
