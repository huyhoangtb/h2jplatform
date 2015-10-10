/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import net.hj2eplatform.models.Organization;

/**
 *
 * @author Huy Hoang
 */
public class OrgSearchDto {

    private Organization organization;
    private String orgName;
    private Long nationalId;
    private Long provinceId;
    private Long trustType;
    private Long orgType;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public Long getTrustType() {
        return trustType;
    }

    public void setTrustType(Long trustType) {
        this.trustType = trustType;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Long getOrgType() {
        return orgType;
    }

    public void setOrgType(Long orgType) {
        this.orgType = orgType;
    }

}
