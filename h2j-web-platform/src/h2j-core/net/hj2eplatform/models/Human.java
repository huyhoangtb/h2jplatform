/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.hj2eplatform.core.component.LazyObject;
import net.hj2eplatform.utils.WorkingTitleDefine;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "human")
@NamedQueries({
    @NamedQuery(name = "Human.findAll", query = "SELECT s FROM Human s")})
public class Human extends LazyObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "human_id")
    private Long humanId;
    @Basic(optional = false)
    @Column(name = "organization_id")
    private Long organizationId;
    @Column(name = "org_root_id")
    private Long orgRootId;
    @Column(name = "avatar_id")
    private Long avatarId;
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
    @Column(name = "cmt_issue_date")
    @Temporal(TemporalType.DATE)
    private Date cmtIssueDate;
    @Column(name = "cmt_issue_plance_id")
    private Long cmtIssuePlanceId;
    @Column(name = "yahoo")
    private String yahoo;
    @Column(name = "skype")
    private String skype;
    @Column(name = "status")
    private Integer status;
    @Column(name = "comments")
    private String comments;
    @Column(name = "user_type")
    private Integer userType;
    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "modified_date")
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    @Column(name = "create_staff_id")
    private Long createStaffId;
    @Column(name = "modified_staff_id")
    private Long modifiedStaffId;
    private transient Human suggetStaff;
    private transient Integer numberOfRow;
    private transient String signSequence;//chuoi nay dung khi converter từ suggest

    public Human() {
    }

    public String getObjectKey() {
        if (this.humanId == null) {
            return null;
        }
        return this.humanId.toString();
    }

    public Integer getNumberOfRow() {
        return numberOfRow;
    }

    public void setNumberOfRow(Integer numberOfRow) {
        this.numberOfRow = numberOfRow;
    }

    public String getSignSequence() {
        return signSequence;
    }


    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public void setSignSequence(String signSequence) {
        this.signSequence = signSequence;
    }

    public Human(Long humanId) {
        this.humanId = humanId;
    }

    public Human(Long humanId, long organizationId) {
        this.humanId = humanId;
        this.organizationId = organizationId;
    }

    public List<SelectItem> getWorkingTitleDefaultList() {
        return WorkingTitleDefine.toSelectItem();
    }

    public Long getHumanId() {
        return humanId;
    }

    public Human getSuggetHuman() {
        return suggetStaff;
    }

    public void setSuggetHuman(Human suggetStaff) {
        this.suggetStaff = suggetStaff;
    }

    public void setHumanId(Long humanId) {
        this.humanId = humanId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrgRootId() {
        return orgRootId;
    }

    public void setOrgRootId(Long orgRootId) {
        this.orgRootId = orgRootId;
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

    public String getFullName() {
        return fullName;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getStreetId() {
        return streetId;
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

    public Date getCmtIssueDate() {
        return cmtIssueDate;
    }

    public void setCmtIssueDate(Date cmtIssueDate) {
        this.cmtIssueDate = cmtIssueDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getModifiedStaffId() {
        return modifiedStaffId;
    }

    public void setModifiedStaffId(Long modifiedStaffId) {
        this.modifiedStaffId = modifiedStaffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (humanId != null ? humanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Human)) {
            return false;
        }
        if (this.humanId == null) {
            return super.equals(object);
        }
        Human other = (Human) object;
        if ((this.humanId == null && other.humanId != null) || (this.humanId != null && !this.humanId.equals(other.humanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.Staff[ humanId=" + humanId + " ]";
    }
}
