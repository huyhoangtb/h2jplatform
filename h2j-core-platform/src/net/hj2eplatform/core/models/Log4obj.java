/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.models;

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
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
@Entity
@Table(name = "log4obj")
@NamedQueries({
    @NamedQuery(name = "Log4obj.findAll", query = "SELECT l FROM Log4obj l")})
public class Log4obj implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "log4obj_id")
    private Long log4objId;
    @Column(name = "org_id")
    private Long orgId;
    @Column(name = "org_root_id")
    private Long orgRootId;
    @Column(name = "log4obj_code")
    private String log4objCode;
    @Column(name = "log4obj_table")
    private String log4objTable;
    @Column(name = "log4obj_table_id")
    private String log4objTableId;
    @Column(name = "log4obj_table_field")
    private String log4objTableField;
    @Column(name = "current_value")
    private String currentValue;
    @Column(name = "new_value")
    private String newValue;
    @Column(name = "log4obj_level")
    private Integer log4objLevel;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_user")
    private String createUser;
    @Column(name = "log_msg")
    private String logMsg;
    @Column(name = "status")
    private Integer status;
    @Column(name = "comments")
    private String comments;
    public static final transient Integer LOG_INFO = 1;
    public static final transient Integer LOG_WARNING = 2;
    public static final transient Integer LOG_ERROR = 3;
    public static final transient Integer LOG_FATAL = 4;

    public Log4obj() {
    }

    public Log4obj(Long log4objId) {
        this.log4objId = log4objId;
    }

    public Long getLog4objId() {
        return log4objId;
    }

    public void setLog4objId(Long log4objId) {
        this.log4objId = log4objId;
    }

    public String getLog4objCode() {
        return log4objCode;
    }

    public void setLog4objCode(String log4objCode) {
        this.log4objCode = log4objCode;
    }

    public String getLog4objTable() {
        return log4objTable;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getOrgRootId() {
        return orgRootId;
    }

    public void setOrgRootId(Long orgRootId) {
        this.orgRootId = orgRootId;
    }

    public void setLog4objTable(String log4objTable) {
        this.log4objTable = log4objTable;
    }

    public String getLog4objTableId() {
        return log4objTableId;
    }

    public void setLog4objTableId(String log4objTableId) {
        this.log4objTableId = log4objTableId;
    }

    public String getLog4objTableField() {
        return log4objTableField;
    }

    public void setLog4objTableField(String log4objTableField) {
        this.log4objTableField = log4objTableField;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Integer getLog4objLevel() {
        return log4objLevel;
    }

    public void setLog4objLevel(Integer log4objLevel) {
        this.log4objLevel = log4objLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

  
    public String getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(String logMsg) {
        this.logMsg = logMsg;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (log4objId != null ? log4objId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log4obj)) {
            return false;
        }
        Log4obj other = (Log4obj) object;
        if ((this.log4objId == null && other.log4objId != null) || (this.log4objId != null && !this.log4objId.equals(other.log4objId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.hj2eplatform.models.Log4obj[ log4objId=" + log4objId + " ]";
    }
}
