/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import java.io.Serializable;
import java.math.BigInteger;
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
import net.hj2eplatform.core.component.LazyObject;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "system_resource")
@NamedQueries({
    @NamedQuery(name = "SystemResource.findAll", query = "SELECT s FROM SystemResource s")})
public class SystemResource extends LazyObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "resource_id")
    private Long resourceId;
    @Column(name = "org_id")
    private Long orgId;
    @Column(name = "root_org_id")
    private Long rootOrgId;
    @Column(name = "folder_path")
    private String folderPath;

    @Column(name = "file_real_name")
    private String fileRealName;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "meta_url")
    /* 
     * truong nay su dung trong truong hop lay anh tren mạng
     */
    private String metaUrl;
    @Column(name = "meta_alt")
    private String metaAlt;
    @Column(name = "meta_title")
    private String metaTitle;
    @Column(name = "type")
    private String type;
    @Column(name = "extension")
    private String extension;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_user")
    private BigInteger createUser;

    public SystemResource() {
    }

    public SystemResource(Long resourceId) {
        this.resourceId = resourceId;
    }
    public String getImageUrl() {
        return new StringBuilder(this.folderPath).append("/").append(this.fileRealName).toString();
    }
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    public Long getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(Long rootOrgId) {
        this.rootOrgId = rootOrgId;
    }

    public String getFolderPath() {
        return folderPath;
    }
    public String getFolderPathAsParam() {
        return folderPath.replace("/", "@h2j@");
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMetaUrl() {
        return metaUrl;
    }

    public void setMetaUrl(String metaUrl) {
        this.metaUrl = metaUrl;
    }

    public String getMetaAlt() {
        return metaAlt;
    }

    public void setMetaAlt(String metaAlt) {
        this.metaAlt = metaAlt;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigInteger getCreateUser() {
        return createUser;
    }

    public void setCreateUser(BigInteger createUser) {
        this.createUser = createUser;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resourceId != null ? resourceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemResource)) {
            return false;
        }
        SystemResource other = (SystemResource) object;
        if ((this.resourceId == null && other.resourceId != null) || (this.resourceId != null && !this.resourceId.equals(other.resourceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.hj2eplatform.models.SystemResource[ resourceId=" + resourceId + " ]";
    }

    @Override
    public String getObjectKey() {
        return this.resourceId == null ? null : resourceId.toString();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    
}
