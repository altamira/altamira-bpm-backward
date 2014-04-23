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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER_IN_STOCK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupplierInStock.findAll", query = "SELECT s FROM SupplierInStock s"),
    @NamedQuery(name = "SupplierInStock.findById", query = "SELECT s FROM SupplierInStock s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierInStock.findByWidth", query = "SELECT s FROM SupplierInStock s WHERE s.width = :width"),
    @NamedQuery(name = "SupplierInStock.findByLength", query = "SELECT s FROM SupplierInStock s WHERE s.length = :length"),
    @NamedQuery(name = "SupplierInStock.findByWeight", query = "SELECT s FROM SupplierInStock s WHERE s.weight = :weight")})
public class SupplierInStock implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SupplierInStockSequence", sequenceName = "SUPPLIER_IN_STOCK_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SupplierInStockSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "WIDTH")
    private BigDecimal width;
    @Basic(optional = false)
    @Column(name = "LENGTH")
    private BigDecimal length;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @JoinColumn(name = "QUOTATION_ITEM_QUOTE", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private QuotationItemQuote quotationItemQuote;

    public SupplierInStock() {
    }

    public SupplierInStock(Long id) {
        this.id = id;
    }

    public SupplierInStock(Long id, BigDecimal width, BigDecimal length,
            BigDecimal weight) {
        this.id = id;
        this.width = width;
        this.length = length;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public QuotationItemQuote getQuotationItemQuote() {
        return quotationItemQuote;
    }

    public void setQuotationItemQuote(QuotationItemQuote quotationItemQuote) {
        this.quotationItemQuote = quotationItemQuote;
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
        if (!(object instanceof SupplierInStock)) {
            return false;
        }
        SupplierInStock other = (SupplierInStock) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierInStock[ id=" + id
                + " ]";
    }

}
