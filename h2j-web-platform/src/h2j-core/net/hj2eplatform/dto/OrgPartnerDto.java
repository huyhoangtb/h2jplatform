/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.hj2eplatform.core.component.LazyObject;
import net.hj2eplatform.models.Location;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.SystemResource;

/**
 *
 * @author HuyHoang
 */
@Entity
public class OrgPartnerDto extends LazyObject {

    private Location visitPlance;
    @Id
    @Basic(optional = false)
    @Column(name = "organization_id")
    private Long organizationId = -1L;
    @Column(name = "group_partner_id")
    private Long groupPartnerId = 0L;
    @Column(name = "organization_code")
    private String organizationCode;
    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "logo_img_id")
    protected Long logoImgId;
    @Column(name = "enlish_name")
    private String enlishName;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "invoice_address")
    private String invoiceAddress;
    @Column(name = "contract_type")
    protected Integer contractType;
    @Column(name = "contract_date_from")
    @Temporal(TemporalType.DATE)
    protected Date contractDateFrom;
    @Column(name = "contract_date_to")
    @Temporal(TemporalType.DATE)
    protected Date contractDateTo;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "bank_number")
    private String bankNumber;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "bank_holder")
    private String bankHolder;

    @Column(name = "nationnal_id")
    private Long nationnalId;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "district_id")
    private Long districtId;
    @Column(name = "street_id")
    private Long streetId;
    @Column(name = "address")
    private String address;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "fax")
    private String fax;
    @Column(name = "website")
    private String website;
    @Column(name = "root_id")
    private Long rootId = 0L;
    @Column(name = "path")
    private String path;
    @Column(name = "org_trans_group")
    protected Integer orgTransGroup;
    @Column(name = "org_type")
    private Integer orgType;
    @Column(name = "last_trans")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTrans;
    @Column(name = "total_trans")
    private Long totalTrans = 0L;
    @Column(name = "total_amount_trans")
    private Double totalAmountTrans = 0D;
    @Column(name = "full_name")
    private String deputyName;
    @Column(name = "about")
    private String about;
    @Temporal(TemporalType.DATE)
    @Column(name = "founded_on_date")
    private Date foundedOnDate;
    @Column(name = "trust_type")
    private Long trustType;
    @Column(name = "branch_overview")
    private String branchOverview;
    @Column(name = "total_staff_about")
    private Long totalStaffAbout;
    @Column(name = "logo_url")
    protected String logoUrl;
    @Column(name = "org_sub_type")
    protected Integer orgSubType;
    @Column(name = "travel_permits")
    private String travelPermits;
    @Column(name = "product_specific")
    private Long productSpecific;
    @Column(name = "location_name")
    private String locationName;
    @Column(name = "folder_path")
    private String folderPath;
    @Column(name = "meta_url")
    protected String metaUrl;
    @Column(name = "meta_description")
    protected String metaDescription;
    @Column(name = "meta_title")
    protected String metaTitle;
    @Column(name = "meta_keyword")
    protected String metaKeyword;
    private transient SystemResource orgLogo;
//     @Column(name = "group_id")
//    private Integer groupId;
    @Transient
    private LocationComObj locationComObj;

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

    public Organization cloneOrg() {
        Organization org = new Organization();
        org.setAddress(address);
        org.setBankHolder(bankHolder);
        org.setBankName(bankName);
        org.setBankNumber(bankNumber);
        org.setDistrictId(districtId);
        org.setEmailAddress(emailAddress);
        org.setEnlishName(enlishName);
        org.setFax(fax);
        org.setAbout(about);
        org.setContractType(contractType);
        org.setContractDateFrom(contractDateFrom);
        org.setContractDateTo(contractDateTo);
        org.setInvoiceAddress(invoiceAddress);
        org.setLastTrans(lastTrans);
        org.setLocationComObj(locationComObj);
        org.setLogoImgId(logoImgId);
        org.setNationnalId(nationnalId);
        org.setOrgType(orgType);
        org.setOrganizationCode(organizationCode);
        org.setOrganizationId(organizationId);
        org.setOrganizationName(organizationName);
        org.setGroupPartnerId(groupPartnerId);
        org.setPath(path);
        org.setPhone(phone);
        org.setProvinceId(provinceId);
        org.setRootId(rootId);
        org.setShortName(shortName);
        org.setStreetId(streetId);
        org.setTaxCode(taxCode);
        org.setTotalAmountTrans(totalAmountTrans);
        org.setTotalTrans(totalTrans);
        org.setWebsite(website);
        org.setTotalStaffAbout(totalStaffAbout);
        org.setAbout(about);
        org.setFoundedOnDate(foundedOnDate);
        org.setTrustType(trustType);
        org.setBranchOverview(branchOverview);
        org.setTravelPermits(travelPermits);
        org.setProductSpecific(productSpecific);
        org.setMetaDescription(metaDescription);
        org.setMetaKeyword(metaKeyword);
        org.setMetaTitle(metaTitle);
        org.setMetaUrl(metaUrl);
        org.setLogoUrl(logoUrl);
        org.setOrgSubType(orgSubType);
        org.setOrgTransGroup(orgTransGroup);
//        org.setGroupId(groupId);
        return org;
    }

    public void setLocationComObj(LocationComObj locationComObj) {
        this.locationComObj = locationComObj;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Integer getOrgTransGroup() {
        return orgTransGroup;
    }

    public void setOrgTransGroup(Integer orgTransGroup) {
        this.orgTransGroup = orgTransGroup;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Long getGroupPartnerId() {
        return groupPartnerId;
    }

    public Integer getOrgSubType() {
        return orgSubType;
    }

    public void setOrgSubType(Integer orgSubType) {
        this.orgSubType = orgSubType;
    }

    public void setGroupPartnerId(Long groupPartnerId) {
        this.groupPartnerId = groupPartnerId;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getDeputyName() {
        return deputyName;
    }

    public void setDeputyName(String deputyName) {
        this.deputyName = deputyName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getBankHolder() {
        return bankHolder;
    }

    public void setBankHolder(String bankHolder) {
        this.bankHolder = bankHolder;
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

    public Long getTotalStaffAbout() {
        return totalStaffAbout;
    }

    public void setTotalStaffAbout(Long totalStaffAbout) {
        this.totalStaffAbout = totalStaffAbout;
    }

    public String getTravelPermits() {
        return travelPermits;
    }

    public void setTravelPermits(String travelPermits) {
        this.travelPermits = travelPermits;
    }

    public String getObjectKey() {
        if (this.organizationId == null) {
            return null;
        }
        return this.organizationId.toString();

    }

    public Long getProductSpecific() {
        if (productSpecific == null && this.visitPlance != null && this.visitPlance.getLocationId() != null) {
            return this.visitPlance.getLocationId();
        }
        return productSpecific;
    }

    public void setProductSpecific(Long productSpecific) {
        this.productSpecific = productSpecific;
    }

    public Location getVisitPlance() {
        return visitPlance;
    }

    public void setVisitPlance(Location visitPlance) {
        this.visitPlance = visitPlance;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public SystemResource getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(SystemResource orgLogo) {
        this.orgLogo = orgLogo;
    }

    @Override
    public Integer getStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
