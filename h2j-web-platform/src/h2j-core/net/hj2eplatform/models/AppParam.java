/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "app_param")
@NamedQueries({
    @NamedQuery(name = "AppParam.findAll", query = "SELECT a FROM AppParam a")})
public class AppParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "app_param_id")
    private Long appParamId;
    @Column(name = "group_code")
    private String groupCode;
    @Column(name = "param_code")
    private String paramCode;
    @Column(name = "param_name")
    private String paramName;
    @Column(name = "param_value")
    private String paramValue;
    @Column(name = "show_order")
    private Integer showOrder;
    @Column(name = "status")
    private Integer status;
    @Column(name = "comments")
    private String comment;

    public AppParam() {
    }

    public AppParam(Long appParamId) {
        this.appParamId = appParamId;
    }

    public Long getAppParamId() {
        return appParamId;
    }

    public void setAppParamId(Long appParamId) {
        this.appParamId = appParamId;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appParamId != null ? appParamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppParam)) {
            return false;
        }
        AppParam other = (AppParam) object;
        if ((this.appParamId == null && other.appParamId != null) || (this.appParamId != null && !this.appParamId.equals(other.appParamId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.AppParam[ appParamId=" + appParamId + " ]";
    }
    
}
