/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "COMPANY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findById", query = "SELECT c FROM Company c WHERE c.id = :id"),
    @NamedQuery(name = "Company.findByName", query = "SELECT c FROM Company c WHERE c.name = :name"),
    @NamedQuery(name = "Company.findByAlias", query = "SELECT c FROM Company c WHERE c.alias = :alias"),
    @NamedQuery(name = "Company.findByFederalTaxId", query = "SELECT c FROM Company c WHERE c.federalTaxId = :federalTaxId"),
    @NamedQuery(name = "Company.findByStateTaxId", query = "SELECT c FROM Company c WHERE c.stateTaxId = :stateTaxId"),
    @NamedQuery(name = "Company.findByCityTaxId", query = "SELECT c FROM Company c WHERE c.cityTaxId = :cityTaxId")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME", columnDefinition = "nvarchar2(50)")
    private String name;
    @Size(max = 20)
    @Column(name = "ALIAS", columnDefinition = "nvarchar2(20)")
    private String alias;
    @Size(max = 20)
    @Column(name = "FEDERAL_TAX_ID", columnDefinition = "nvarchar2(20)")
    private String federalTaxId;
    @Size(max = 20)
    @Column(name = "STATE_TAX_ID", columnDefinition = "nvarchar2(20)")
    private String stateTaxId;
    @Size(max = 20)
    @Column(name = "CITY_TAX_ID", columnDefinition = "nvarchar2(20)")
    private String cityTaxId;
    @JoinColumn(name = "BILLING_ADDRESS", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private LocationAddress billingAddress;
    @JoinColumn(name = "INVOICE_ADDRESS", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private LocationAddress invoiceAddress;
    @JoinColumn(name = "LOCATION_ADDRESS", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private LocationAddress locationAddress;
    @JoinColumn(name = "SHIPPING_ADDRESS", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private LocationAddress shippingAddress;

    public Company() {
    }

    public Company(BigDecimal id) {
        this.id = id;
    }

    public Company(BigDecimal id, String name) {
        this.id = id;
        this.name = name;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFederalTaxId() {
        return federalTaxId;
    }

    public void setFederalTaxId(String federalTaxId) {
        this.federalTaxId = federalTaxId;
    }

    public String getStateTaxId() {
        return stateTaxId;
    }

    public void setStateTaxId(String stateTaxId) {
        this.stateTaxId = stateTaxId;
    }

    public String getCityTaxId() {
        return cityTaxId;
    }

    public void setCityTaxId(String cityTaxId) {
        this.cityTaxId = cityTaxId;
    }

    public LocationAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(LocationAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public LocationAddress getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(LocationAddress invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public LocationAddress getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(LocationAddress locationAddress) {
        this.locationAddress = locationAddress;
    }

    public LocationAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(LocationAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Company[ id=" + id + " ]";
    }
    
}
