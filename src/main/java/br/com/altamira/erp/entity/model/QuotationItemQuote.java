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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "QUOTATION_ITEM_QUOTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuotationItemQuote.findAll", query = "SELECT q FROM QuotationItemQuote q"),
    @NamedQuery(name = "QuotationItemQuote.findById", query = "SELECT q FROM QuotationItemQuote q WHERE q.id = :id"),
    @NamedQuery(name = "QuotationItemQuote.findByWeight", query = "SELECT q FROM QuotationItemQuote q WHERE q.weight = :weight"),
    @NamedQuery(name = "QuotationItemQuote.findByPrice", query = "SELECT q FROM QuotationItemQuote q WHERE q.price = :price"),
    @NamedQuery(name = "QuotationItemQuote.findByStandard", query = "SELECT q FROM QuotationItemQuote q WHERE q.standard = :standard")})
public class QuotationItemQuote implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "QuotationItemQuoteSequence", sequenceName = "QUOTATION_ITEM_QUOTE_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QuotationItemQuoteSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Basic(optional = false)
    @Column(name = "PRICE")
    private BigDecimal price;
    @Basic(optional = false)
    @Column(name = "STANDARD")
    private String standard;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "QUOTATION_ITEM", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private QuotationItem quotationItem;
    @OneToMany(/*cascade = CascadeType.REFRESH,*/ mappedBy = "quotationItemQuote", fetch = FetchType.LAZY)
    private Set<SupplierInStock> supplierInStockSet;

    public QuotationItemQuote() {
    }

    public QuotationItemQuote(Long id) {
        this.id = id;
    }

    public QuotationItemQuote(Long id, BigDecimal weight, BigDecimal price,
            String standard) {
        this.id = id;
        this.weight = weight;
        this.price = price;
        this.standard = standard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public QuotationItem getQuotationItem() {
        return quotationItem;
    }

    public void setQuotationItem(QuotationItem quotationItem) {
        this.quotationItem = quotationItem;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierInStock> getSupplierInStockSet() {
        return supplierInStockSet;
    }

    public void setSupplierInStockSet(Set<SupplierInStock> supplierInStockSet) {
        this.supplierInStockSet = supplierInStockSet;
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
        if (!(object instanceof QuotationItemQuote)) {
            return false;
        }
        QuotationItemQuote other = (QuotationItemQuote) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.QuotationItemQuote[ id=" + id
                + " ]";
    }

}
