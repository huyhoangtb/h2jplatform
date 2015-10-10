/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import net.hj2eplatform.iservices.IContactUsService;
import net.hj2eplatform.models.ContactUs;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.SystemConfig;
import net.hj2eplatform.core.utils.SystemDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("contactUsTag")
@Scope("request")
public class ContactUsTag implements Serializable {

    @Autowired
    private IContactUsService contactUsService;
    private ContactUs contactUs;

    @PostConstruct
    private void init() {
        contactUs = new ContactUs();
    }

    public void createContactUs() {
        try {
            if (!validate()) {
                return;
            }
            contactUs.setRandomNumber(contactUsService.generateRandomIdNumber());
            contactUs.setCreatedDate(new Date());
            contactUs.setFromSite(ControllerUtils.getRequest().getServerName());
            contactUs.setLanguagle(SystemConfig.getResource("hj2eplatform.languagle.translateDefault"));
            contactUs.setOrgId(null);//fake
            contactUs.setRootOrgId(null);
            contactUs.setContactId(contactUsService.getSequence(SystemDefine.SEQUENCE_CONTACTUS_ID).longValue());
            contactUs.setStatus(2);//đã gửi thành công chưa liên lạc hay confirm lại
            contactUs.setContactIp(ControllerUtils.getAccessIp());
            contactUsService.saveEntity(contactUs);
            contactUsService.sendEmail(null, contactUs.getContactTitle(), contactUs.getContactDetail());
            ControllerUtils.addSuccessMessage("Bạn vừa gửi thành công liên hệ đến công ty chúng tôi từ địa chỉ ip: " + contactUs.getContactIp() + "; Chung tôi sẽ liên lạc với bạn trong thời gian ngắn nhất.");
        } catch (Exception e) {
           
        }

    }

    public Boolean validate() {
        if (contactUs.getContactDepartment() == null || contactUs.getContactDepartment().trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng chọn bộ phận cần liên hệ.");
            return false;
        }
        if (contactUs.getContactFullname() == null || contactUs.getContactFullname().trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng nhập họ và tên.");
            return false;
        }

        DataValidator.validateEmailAdress(contactUs.getContactEmail());

        if (contactUs.getContactTitle() == null || contactUs.getContactTitle().trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng nhập tiêu đề liên hệ.");
            return false;
        }
        if (contactUs.getContactDetail() == null || contactUs.getContactDetail().trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng nhập nội dung liên hệ.");
            return false;
        }
        return true;
    }

    public ContactUs getContactUs() {
        return contactUs;
    }

    public void setContactUs(ContactUs contactUs) {
        this.contactUs = contactUs;
    }
}