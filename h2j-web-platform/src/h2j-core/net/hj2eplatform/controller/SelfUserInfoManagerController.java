package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import net.hj2eplatform.dto.LocationComId;
import net.hj2eplatform.iservices.IGenerateService;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.utils.UserGender;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Nguyen Huy Hoang
 */
@Controller("selfUserInfoManagerController")
@Scope("view")
public class SelfUserInfoManagerController implements Serializable {
    
    final static Logger logger = Logger.getLogger(SelfUserInfoManagerController.class);
    private LocationComId locationComId;
    @Autowired
    private IGenerateService generateService;
    
    @PostConstruct
    public void SeftUserInfoManagerController() {
        locationComId = new LocationComId();
        locationComId.init();
        
        Human human = AuthenticationController.getCurrentHuman();
        if (human == null) {
            return;
        }
        locationComId.setProvinceSelected(human.getProvinceId());
        locationComId.setDistrictSelected(human.getDistrictId());
        locationComId.setNationalSelected(human.getNationalId());
        if (human.getAvatarId() != null) {
            ResourceManagerController resource = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
            resource.loadMyResoure(human.getAvatarId());
        }
        
    }
    
    public void updateUserInfo() {
        Human human = AuthenticationController.getCurrentHuman();
        System.out.println("human: " + human);
        if (human == null) {
            return;
        }
        try {
            if (human.getFullName() == null || human.getFullName().compareTo("") == 0) {
                ControllerUtils.addErrorMessage("Vui lòng nhập họ và tên!");
                return;
            }
            DataValidator.validateEmailAdress(human.getEmailAddress());
            if (this.locationComId.getNationalSelected() == null || this.locationComId.getNationalSelected() <= 0) {
                ControllerUtils.addErrorMessage("Vui lòng cho chúng tôi biết bạn thuộc quốc gia nào!");
                return;
            }
            if (this.locationComId.getProvinceSelected() == null || this.locationComId.getProvinceSelected() <= 0) {
                ControllerUtils.addErrorMessage("Vui lòng cho chúng tôi biết bạn ở tỉnh thành nào!");
                return;
            }
            human.setNationalId(this.locationComId.getNationalSelected());
            human.setProvinceId(this.locationComId.getProvinceSelected());
            human.setDistrictId(this.locationComId.getDistrictSelected());
            ResourceManagerController resource = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
            if (resource.getMyResource() != null) {
                human.setAvatarId(resource.getMyResource().getResourceId());
            }
            this.generateService.saveObject(human);
            ControllerUtils.addSuccessMessage("Bạn đã cập nhật thông tin cá nhân thành công.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public LocationComId getLocationComId() {
        return locationComId;
    }
public List<SelectItem> getGenderList() {
    return UserGender.toSelectItem();
}
    public void setLocationComId(LocationComId locationComId) {
        this.locationComId = locationComId;
    }
    
}
