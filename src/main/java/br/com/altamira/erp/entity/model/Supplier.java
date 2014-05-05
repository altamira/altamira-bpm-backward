/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findById", query = "SELECT s FROM Supplier s WHERE s.id = :id"),
    @NamedQuery(name = "Supplier.findByName", query = "SELECT s FROM Supplier s WHERE s.name = :name")})
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SupplierSequence", sequenceName = "SUPPLIER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SupplierSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME", columnDefinition = "nvarchar2(50)")
    private String name;
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<QuotationItemQuote> quotationItemQuote;
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<SupplierPriceList> supplierPriceList;
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<PurchaseOrder> purchaseOrder;
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<SupplierStandard> supplierStandard;
    @OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<PurchasePlanningItem> purchasePlanningItem;

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
    public Set<QuotationItemQuote> getQuotationItemQuote() {
        return quotationItemQuote;
    }

    public void setQuotationItemQuote(
            Set<QuotationItemQuote> quotationItemQuote) {
        this.quotationItemQuote = quotationItemQuote;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierPriceList> getSupplierPriceList() {
        return supplierPriceList;
    }

    public void setSupplierPriceListSet(
            Set<SupplierPriceList> supplierPriceList) {
        this.supplierPriceList = supplierPriceList;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchaseOrder> getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(Set<PurchaseOrder> purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierStandard> getSupplierStandard() {
        return supplierStandard;
    }

    public void setSupplierStandard(Set<SupplierStandard> supplierStandard) {
        this.supplierStandard = supplierStandard;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchasePlanningItem> getPurchasePlanningItem() {
        return purchasePlanningItem;
    }

    public void setPurchasePlanningItem(
            Set<PurchasePlanningItem> purchasePlanningItem) {
        this.purchasePlanningItem = purchasePlanningItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Supplier[ id=" + id + " ]";
    }

}
