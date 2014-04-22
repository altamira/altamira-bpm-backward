/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findById", query = "SELECT s FROM Supplier s WHERE s.id = :id"),
    @NamedQuery(name = "Supplier.findByName", query = "SELECT s FROM Supplier s WHERE s.name = :name")})
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME", columnDefinition="nvarchar2(50)")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<QuotationItemQuote> quotationItemQuoteSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<SupplierPriceList> supplierPriceListSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<SupplierContact> supplierContactSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<PurchaseOrder> purchaseOrderSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<SupplierStandard> supplierStandardSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<PurchasePlanningItem> purchasePlanningItemSet;

    public Supplier() {
    }

    public Supplier(Long id) {
        this.id = id;
    }

    public Supplier(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    @JsonIgnore
    public Set<QuotationItemQuote> getQuotationItemQuoteSet() {
        return quotationItemQuoteSet;
    }

    public void setQuotationItemQuoteSet(Set<QuotationItemQuote> quotationItemQuoteSet) {
        this.quotationItemQuoteSet = quotationItemQuoteSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierPriceList> getSupplierPriceListSet() {
        return supplierPriceListSet;
    }

    public void setSupplierPriceListSet(Set<SupplierPriceList> supplierPriceListSet) {
        this.supplierPriceListSet = supplierPriceListSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierContact> getSupplierContactSet() {
        return supplierContactSet;
    }

    public void setSupplierContactSet(Set<SupplierContact> supplierContactSet) {
        this.supplierContactSet = supplierContactSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchaseOrder> getPurchaseOrderSet() {
        return purchaseOrderSet;
    }

    public void setPurchaseOrderSet(Set<PurchaseOrder> purchaseOrderSet) {
        this.purchaseOrderSet = purchaseOrderSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierStandard> getSupplierStandardSet() {
        return supplierStandardSet;
    }

    public void setSupplierStandardSet(Set<SupplierStandard> supplierStandardSet) {
        this.supplierStandardSet = supplierStandardSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchasePlanningItem> getPurchasePlanningItemSet() {
        return purchasePlanningItemSet;
    }

    public void setPurchasePlanningItemSet(Set<PurchasePlanningItem> purchasePlanningItemSet) {
        this.purchasePlanningItemSet = purchasePlanningItemSet;
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
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Supplier[ id=" + id + " ]";
    }
    
}
