/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import net.hj2eplatform.core.models.SecurityObject;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")})
public class Users extends SecurityObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    protected Long userId;
    @Basic(optional = false)
    @Column(name = "organization_id")
    protected Long organizationId;
    @Column(name = "human_id")
    protected Long humanId;
    @Column(name = "social_id")
    protected String socialId;
    @Column(name = "transfer_source")
    protected String transferSource;
    @Column(name = "social_password")
    protected String socialPassword;
    @Column(name = "username")
    protected String username;
    @Column(name = "password")
    protected String password;
    @Column(name = "mandatory_reset_pass")
    protected Boolean mandatoryResetPass;
    @Column(name = "login_fail_counter")
    protected Integer loginFailCounter;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastLoginTime;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    protected Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    protected Date endDate;
    @Column(name = "create_user_id")
    protected Long createUserId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;
    @Column(name = "status")
    protected Integer status;
    @Column(name = "is_admin")
    protected Integer isAdmin;
    @Column(name = "note")
    protected String note;
    protected transient String retypePassword;
    public final static int TYPE_CUSTOMER = 1;
    public final static int TYPE_USER = 2;
    public final static int TYPE_AS_ADMIN = 3;
    public final static int TYPE_SUPPER_ADMIN = -1;

    public Users() {
    }

    public Users(Long userId) {
        this.userId = userId;
    }

    public Users(Long userId, long organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
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

    public Long getHumanId() {
        return humanId;
    }

    public String getSocialPassword() {
        return socialPassword;
    }

    public void setSocialPassword(String socialPassword) {
        this.socialPassword = socialPassword;
    }

    public void setHumanId(Long belongId) {
        this.humanId = belongId;
    }

    public String getTransferSource() {
        return transferSource;
    }

    public void setTransferSource(String transferSource) {
        this.transferSource = transferSource;
    }
    
    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
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

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
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

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.Users[ userId=" + userId + " ]";
    }
}
