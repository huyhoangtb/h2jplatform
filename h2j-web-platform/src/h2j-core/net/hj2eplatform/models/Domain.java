/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Huy Hoang
 */
@Entity
@Table(name = "domain")
@NamedQueries({
    @NamedQuery(name = "Domain.findAll", query = "SELECT d FROM Domain d")})
public class Domain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "domain_id")
    private Long domainId;
    @Column(name = "domain_org_id")
    private Long domainOrgId;
    @Size(max = 255)
    @Column(name = "domain_name")
    private String domainName;
    @Column(name = "template_id")
    private Long templateId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "domain_type")
    private Integer domainType;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_staff")
    private Long createStaff;
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "modified_staff")
    private Long modifiedStaff;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Size(max = 255)
    @Column(name = "default_languagle")
    private String defaultLanguagle;
    @Size(max = 255)
    @Column(name = "supported_languagle_list")
    private String supportedLanguagleList;
    @Size(max = 500)
    @Column(name = "note")
    private String note;

    public Domain() {
    }

    public Domain(Long domainId) {
        this.domainId = domainId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getDomainOrgId() {
        return domainOrgId;
    }

    public void setDomainOrgId(Long domainOrgId) {
        this.domainOrgId = domainOrgId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDomainType() {
        return domainType;
    }

    public void setDomainType(Integer domainType) {
        this.domainType = domainType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Long createStaff) {
        this.createStaff = createStaff;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getModifiedStaff() {
        return modifiedStaff;
    }

    public void setModifiedStaff(Long modifiedStaff) {
        this.modifiedStaff = modifiedStaff;
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

    public String getDefaultLanguagle() {
        return defaultLanguagle;
    }

    public void setDefaultLanguagle(String defaultLanguagle) {
        this.defaultLanguagle = defaultLanguagle;
    }

    public String getSupportedLanguagleList() {
        return supportedLanguagleList;
    }

    public void setSupportedLanguagleList(String supportedLanguagleList) {
        this.supportedLanguagleList = supportedLanguagleList;
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
        hash += (domainId != null ? domainId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domain)) {
            return false;
        }
        Domain other = (Domain) object;
        if ((this.domainId == null && other.domainId != null) || (this.domainId != null && !this.domainId.equals(other.domainId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.hj2eplatform.models.Domain[ domainId=" + domainId + " ]";
    }
    
}
