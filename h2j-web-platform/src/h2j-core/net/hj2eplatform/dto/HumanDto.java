/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.hj2eplatform.core.component.LazyObject;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.StatusDefine;
import net.hj2eplatform.utils.UserStatus;
import net.hj2eplatform.utils.WorkingTitleDefine;

/**
 *
 * @author HuyHoang
 */
@Entity
public class HumanDto extends LazyObject implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "human_id")
    private Long humanId;
    @Column(name = "user_id")
    private Long userId;
    @Basic(optional = false)
    @Column(name = "organization_id")
    private Long organizationId;
//    @Column(name = "belong_id")
//    private Long belongId;
    @Column(name = "transfer_source")
    private String transferSource;
    @Column(name = "social_id")
    private String socialId;
    @Column(name = "social_password")
    private String socialPassword;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "mandatory_reset_pass")
    private Boolean mandatoryResetPass;
    @Column(name = "login_fail_counter")
    private Integer loginFailCounter;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "create_user_id")
    private Long createUserId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "status")
    private Integer status = StatusDefine.activate;
    @Column(name = "note")
    private String note;
    @Column(name = "retype_password")
    private String retypePassword;
    @Column(name = "org_root_id")
    private Long orgRootId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "national_id")
    private Long nationalId;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "district_id")
    private Long districtId;
    @Column(name = "street_id")
    private Long streetId;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "tel")
    private String tel;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "possition")
    private String possition;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "cmt")
    private String cmt;
    @Column(name = "cmt_approve_date")
    @Temporal(TemporalType.DATE)
    private Date cmtApproveDate;
    @Column(name = "cmt_issue_plance_id")
    private Long cmtIssuePlanceId;
    @Column(name = "yahoo")
    private String yahoo;
    @Column(name = "skype")
    private String skype;
    @Column(name = "human_status")
    private Integer humanStatus = StatusDefine.activate;
    @Column(name = "comments")
    private String humanComments;
    @Column(name = "user_type")
    private int userType;
    @Column(name = "is_admin")
    private int isAdmin;

    public HumanDto() {
    }

    public Long getHumanId() {
        return humanId;
    }

    public void setHumanId(Long humanId) {
        this.humanId = humanId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getOrgRootId() {
        return orgRootId;
    }

    public void setOrgRootId(Long orgRootId) {
        this.orgRootId = orgRootId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getNationalId() {
        return nationalId;
    }

    public void setNationalId(Long nationalId) {
        this.nationalId = nationalId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
//
//    public Long getBelongId() {
//        return belongId;
//    }
//
//    public void setBelongId(Long belongId) {
//        this.belongId = belongId;
//    }

    public String gettransferSource() {
        return transferSource;
    }

    public void settransferSource(String transferSource) {
        this.transferSource = transferSource;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialPassword() {
        return socialPassword;
    }

    public void setSocialPassword(String socialPassword) {
        this.socialId = socialPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getMandatoryResetPass() {
        return mandatoryResetPass;
    }

    public void setMandatoryResetPass(Boolean mandatoryResetPass) {
        this.mandatoryResetPass = mandatoryResetPass;
    }

    public Integer getLoginFailCounter() {
        return loginFailCounter;
    }

    public void setLoginFailCounter(Integer loginFailCounter) {
        this.loginFailCounter = loginFailCounter;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public List<SelectItem> getWorkingTitleDefaultList() {
        return WorkingTitleDefine.toSelectItem();
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPossition() {
        return possition;
    }

    public void setPossition(String possition) {
        this.possition = possition;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public Date getCmtApproveDate() {
        return cmtApproveDate;
    }

    public void setCmtApproveDate(Date cmtApproveDate) {
        this.cmtApproveDate = cmtApproveDate;
    }

    public Long getCmtIssuePlanceId() {
        return cmtIssuePlanceId;
    }

    public void setCmtIssuePlanceId(Long cmtIssuePlanceId) {
        this.cmtIssuePlanceId = cmtIssuePlanceId;
    }

    public String getYahoo() {
        return yahoo;
    }

    public void setYahoo(String yahoo) {
        this.yahoo = yahoo;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public Integer getHumanStatus() {
        return humanStatus;
    }

    public void setHumanStatus(Integer humanStatus) {
        this.humanStatus = humanStatus;
    }

    public String getHumanComments() {
        return humanComments;
    }

    public void setHumanComments(String humanComments) {
        this.humanComments = humanComments;
    }

    public String getObjectKey() {
        if (this.humanId == null) {
            return null;
        }
        return this.humanId.toString();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HumanDto)) {
            return false;
        }
        HumanDto other = (HumanDto) object;
        if ((this.humanId == null && other.humanId != null) || (this.humanId != null && !this.humanId.equals(other.humanId))) {
            return false;
        }
        return true;
    }

    public Users cloneUser() {
        Users user = new Users();
        user.setUserId(userId);
        user.setUsername(username);
        user.setHumanId(humanId);
        user.setTransferSource(transferSource);
        user.setSocialId(socialId);
        user.setSocialPassword(socialPassword);
        user.setCreateDate(createDate);
        user.setCreateUserId(createUserId);
        user.setEndDate(endDate);
        user.setLastLoginTime(lastLoginTime);
        user.setLoginFailCounter(loginFailCounter);
        user.setMandatoryResetPass(mandatoryResetPass);
        user.setNote(note);
        user.setIsAdmin(isAdmin);
        user.setOrganizationId(organizationId);
        user.setPassword(password);
        user.setRetypePassword(retypePassword);
        user.setStartDate(startDate);
        user.setStatus(status);

        return user;
    }

    public Human cloneHuman() {
        Human human = new Human();
        human.setHumanId(humanId);
        human.setFirstName(firstName);
        human.setLastName(lastName);
        human.setFullName(fullName);
        human.setBirthday(birthday);
        human.setAddress(address);
        human.setAvatarUrl(avatarUrl);
        human.setCmt(cmt);

        human.setCmtApproveDate(cmtApproveDate);
        human.setCmtIssuePlanceId(cmtIssuePlanceId);
        human.setDistrictId(districtId);
        human.setEmailAddress(emailAddress);
        human.setGender(gender);
        human.setNationalId(nationalId);
        human.setOrganizationId(organizationId);
        human.setPhone(phone);
        human.setPossition(possition);
        human.setProvinceId(provinceId);
        human.setOrgRootId(orgRootId);
        human.setSkype(skype);
        human.setYahoo(yahoo);
        human.setStatus(humanStatus);
        human.setStreetId(streetId);
        human.setTel(tel);
        human.setUserType(userType);
        return human;
    }

    @Transient
    public String getStatusHumanName() {
        if (status == null) {
            return UserStatus.UN_CREATE_ACCOUNT.getName();
        }
        return UserStatus.getStatusName(status);
    }

    @Override
    public String getStatusName() {
        if (status == null) {
            return UserStatus.UN_CREATE_ACCOUNT.getName();
        }
        return UserStatus.getStatusName(status);
    }
}
