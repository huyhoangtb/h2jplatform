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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import net.hj2eplatform.core.component.LazyObject;

/**
 *
 * @author Huy Hoang
 */
@Entity
@Table(name = "location")
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l")})
public class Location extends LazyObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "img_id")
    private Long imgId;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "location_code")
    private String locationCode;
    @Column(name = "location_name")
    private String locationName;
    @Column(name = "status")
    private Integer status;
    @Column(name = "comments")
    private String comments;
    @Lob
    @Column(name = "flag")
    private byte[] flag;
    @Column(name = "path")
    private String path;
    @Column(name = "type")
    private Integer type;
    @Column(name = "order_show")
    private Integer orderShow;
    @Lob
    @Column(name = "sapo")
    protected String sapo;
    @Lob
    @Column(name = "detail")
    protected String detail;
 @Column(name = "meta_url")
    protected String metaUrl;
    @Column(name = "meta_description")
    protected String metaDescription;
    @Column(name = "meta_title")
    protected String metaTitle;
    @Column(name = "meta_keyword")
    protected String metaKeyword;

    public String getSapo() {
        return sapo;
    }

    public void setSapo(String sapo) {
        this.sapo = sapo;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Location() {
    }

    public Location(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getImgId() {
        return imgId;
    }

    public void setImgId(Long imgId) {
        this.imgId = imgId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public byte[] getFlag() {
        return flag;
    }

    public void setFlag(byte[] flag) {
        this.flag = flag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLocationTypeName() {
        if (type == null) {
            return "Địa danh du lịch";
        }
        switch (type) {
            case 1:
                return "Quốc gia";
            case 2:
                return "Tỉnh thành";
            case 3:
                return "Quận huyện";
            case 4:
                return "Phường, xã";
            default:
                return "Địa danh du lịch";
        }
    }

    public Integer getOrderShow() {
        return orderShow;
    }

    public void setOrderShow(Integer orderShow) {
        this.orderShow = orderShow;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationId != null ? locationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.locationId == null && other.locationId != null) || (this.locationId != null && !this.locationId.equals(other.locationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.quanxa.models.Location[ locationId=" + locationId + " ]";
    }

    @Override
    public String getObjectKey() {
        if (locationId == null) {
            return null;
        }
        return locationId.toString();
    }
}
