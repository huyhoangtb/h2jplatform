/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.hj2eplatform.core.component.LazyObject;
import net.hj2eplatform.dto.LocationComObj;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;

/**
 *
 * @author HuyHoang
 */
@Entity
@Table(name = "organization")
@NamedQueries({
    @NamedQuery(name = "Organization.findAll", query = "SELECT o FROM Organization o")})
public class Organization extends LazyObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "organization_id")
    protected Long organizationId;
    @Column(name = "organization_code")
    protected String organizationCode;
    @Column(name = "organization_name")
    protected String organizationName;
    @Column(name = "logo_img_id")
    protected Long logoImgId;
    @Column(name = "logo_url")
    protected String logoUrl;
    @Column(name = "enlish_name")
    protected String enlishName;
    @Column(name = "short_name")
    protected String shortName;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "bank_number")
    private String bankNumber;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "bank_holder")
    private String bankHolder;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "invoice_address")
    private String invoiceAddress;
    @Column(name = "nationnal_id")
    protected Long nationnalId;
    @Column(name = "group_partner_id")
    private Long groupPartnerId = 0L;
//    @Column(name = "group_id")
//    private Integer groupId;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "district_id")
    protected Long districtId;
    @Column(name = "street_id")
    protected Long streetId;
    @Column(name = "address")
    protected String address;
    @Column(name = "phone")
    protected String phone;
    @Column(name = "email_address")
    protected String emailAddress;
    @Column(name = "fax")
    protected String fax;
    @Column(name = "website")
    protected String website;
    @Column(name = "root_id")
    protected Long rootId;
    @Column(name = "path")
    protected String path;
    @Column(name = "about")
    private String about;
    @Column(name = "founded_on_date")
    @Temporal(TemporalType.DATE)
    private Date foundedOnDate;
    @Column(name = "trust_type")
    private Long trustType;
    @Column(name = "branch_overview")
    private String branchOverview;
    @Column(name = "org_type")
    protected Integer orgType;
    @Column(name = "org_sub_type")
    protected Integer orgSubType;
    @Column(name = "org_trans_group")
    protected Integer orgTransGroup;
    @Column(name = "last_trans")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastTrans;
    @Column(name = "total_trans")
    protected Long totalTrans = 0L;
    @Column(name = "total_amount_trans")
    private Double totalAmountTrans = 0D;

    @Column(name = "contract_type")
    protected Integer contractType;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;
    @Column(name = "contract_date_from")
    @Temporal(TemporalType.DATE)
    protected Date contractDateFrom;
    @Column(name = "contract_date_to")
    @Temporal(TemporalType.DATE)
    protected Date contractDateTo;
    @Column(name = "created_staff")
    protected Long createdStaff;
    @Column(name = "modified_staff")
    protected Long modifiedStaff;
    @Column(name = "travel_permits")
    protected String travelPermits;
    @Column(name = "product_specific")
    protected Long productSpecific;
    @Column(name = "total_staff_about")
    protected Long totalStaffAbout;
    @Column(name = "meta_url")
    protected String metaUrl;
    @Column(name = "meta_description")
    protected String metaDescription;
    @Column(name = "meta_title")
    protected String metaTitle;
    @Column(name = "meta_keyword")
    protected String metaKeyword;
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private List<Organization> organizationList;
    @JoinColumn(name = "parent_id", referencedColumnName = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization parentId;
    @Transient
    private LocationComObj locationComObj;
    @Transient
    public final static int IN_ORGANIZATION = 0;
    @Transient
    public final static int IN_ORGANIZATION_AND_SUB = 1;
    @Transient
    public final static int IN_ROOT = 2;
    @Transient
    public final static int IN_ALL = 3;

    public Organization() {
    }

    public Organization(Long organizationId) {
        this.organizationId = organizationId;
    }

    public LocationComObj getLocationComObj() {
        if (locationComObj == null) {
            locationComObj = new LocationComObj();
            locationComObj.init();
            locationComObj.setNationalSelected(locationComObj.getLocation(this.nationnalId));
            locationComObj.setProvinceSelected(locationComObj.getLocation(this.provinceId));
            locationComObj.setDistrictSelected(locationComObj.getLocation(this.districtId));
        }
        return locationComObj;
    }

    public void pullLocation() {
        Location location = locationComObj.getNationalSelected();
        if (location != null) {
            this.setNationnalId(location.getLocationId());
        }
        location = locationComObj.getProvinceSelected();
        if (location != null) {
            this.setProvinceId(location.getLocationId());
        }
        location = locationComObj.getDistrictSelected();
        if (location != null) {
            this.setDistrictId(location.getLocationId());
        }
        location = locationComObj.getStreetSelected();
        if (location != null) {
            this.setStreetId(location.getLocationId());
        }
    }

    public void pullLogo() {
        ResourceManagerController resourceManagerController = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
        if (resourceManagerController.getMyResource() != null && resourceManagerController.getMyResource().getResourceId() != null) {
            this.setLogoImgId(resourceManagerController.getMyResource().getResourceId());
            this.setLogoUrl(resourceManagerController.getMyResource().getImageUrl());
        }
    }

    public String getMetaUrl() {
        return metaUrl;
    }

    public void setMetaUrl(String metaUrl) {
        this.metaUrl = metaUrl;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public void setLocationComObj(LocationComObj locationComObj) {
        this.locationComObj = locationComObj;
    }

    public Boolean isRoot() {
        if (this.organizationId != null && rootId != null && this.organizationId.longValue() == rootId.longValue()) {
            return true;
        }
        return false;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getWebsite() {
        return website;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getLogoImgId() {
        return logoImgId;
    }

    public void setLogoImgId(Long logoImgId) {
        this.logoImgId = logoImgId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getFoundedOnDate() {
        return foundedOnDate;
    }

    public void setFoundedOnDate(Date foundedOnDate) {
        this.foundedOnDate = foundedOnDate;
    }

    public Long getTrustType() {
        return trustType;
    }

    public void setTrustType(Long trustType) {
        this.trustType = trustType;
    }

    public String getBranchOverview() {
        return branchOverview;
    }

    public void setBranchOverview(String branchOverview) {
        this.branchOverview = branchOverview;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getContractDateFrom() {
        return contractDateFrom;
    }

    public void setContractDateFrom(Date contractDateFrom) {
        this.contractDateFrom = contractDateFrom;
    }

    public Date getContractDateTo() {
        return contractDateTo;
    }

    public void setContractDateTo(Date contractDateTo) {
        this.contractDateTo = contractDateTo;
    }

    public Long getCreatedStaff() {
        return createdStaff;
    }

    public void setCreatedStaff(Long createdStaff) {
        this.createdStaff = createdStaff;
    }

    public Long getModifiedStaff() {
        return modifiedStaff;
    }

    public void setModifiedStaff(Long modifiedStaff) {
        this.modifiedStaff = modifiedStaff;
    }

    public Long getGroupPartnerId() {
        return groupPartnerId;
    }

    public void setGroupPartnerId(Long groupPartnerId) {
        this.groupPartnerId = groupPartnerId;
    }

    public String getEnlishName() {
        return enlishName;
    }

    public void setEnlishName(String enlishName) {
        this.enlishName = enlishName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Long getNationnalId() {
        return nationnalId;
    }

    public void setNationnalId(Long nationnalId) {
        this.nationnalId = nationnalId;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrgType() {
        return orgType;
    }

    public void setOrgType(Integer orgType) {
        this.orgType = orgType;
    }

    public Integer getOrgTransGroup() {
        return orgTransGroup;
    }

    public void setOrgTransGroup(Integer orgTransGroup) {
        this.orgTransGroup = orgTransGroup;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public Organization getParentId() {
        return parentId;
    }

    public void setParentId(Organization parentId) {
        this.parentId = parentId;
    }

    public Date getLastTrans() {
        return lastTrans;
    }

    public void setLastTrans(Date lastTrans) {
        this.lastTrans = lastTrans;
    }

    public Long getTotalTrans() {
        return totalTrans;
    }

    public void setTotalTrans(Long totalTrans) {
        this.totalTrans = totalTrans;
    }

    public Double getTotalAmountTrans() {
        return totalAmountTrans;
    }

    public void setTotalAmountTrans(Double totalAmountTrans) {
        this.totalAmountTrans = totalAmountTrans;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getOrgSubType() {
        return orgSubType;
    }

    public void setOrgSubType(Integer orgSubType) {
        this.orgSubType = orgSubType;
    }

    public String getBankHolder() {
        return bankHolder;
    }

    public void setBankHolder(String bankHolder) {
        this.bankHolder = bankHolder;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getTravelPermits() {
        return travelPermits;
    }

    public void setTravelPermits(String travelPermits) {
        this.travelPermits = travelPermits;
    }

    public Long getProductSpecific() {
        return productSpecific;
    }

    public void setProductSpecific(Long productSpecific) {
        this.productSpecific = productSpecific;
    }

    public Long getTotalStaffAbout() {
        return totalStaffAbout;
    }

    public void setTotalStaffAbout(Long totalStaffAbout) {
        this.totalStaffAbout = totalStaffAbout;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organizationId != null ? organizationId.hashCode() + 3 : 34);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organization)) {
            return false;
        }
        Organization other = (Organization) object;
        if ((this.organizationId == null && other.organizationId != null) || (this.organizationId != null && !this.organizationId.equals(other.organizationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.hj2eplatform.models.Organization[ organizationId=" + organizationId + " ]";
    }

    @Override
    public String getObjectKey() {
        if (this.organizationId == null) {
            return null;
        }
        return this.organizationId.toString();
    }

    @Override
    public Integer getStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
