/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER_PRICE_LIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupplierPriceList.findAll", query = "SELECT s FROM SupplierPriceList s"),
    @NamedQuery(name = "SupplierPriceList.findById", query = "SELECT s FROM SupplierPriceList s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierPriceList.findByChangeDate", query = "SELECT s FROM SupplierPriceList s WHERE s.changeDate = :changeDate"),
    @NamedQuery(name = "SupplierPriceList.findByPrice", query = "SELECT s FROM SupplierPriceList s WHERE s.price = :price")})
public class SupplierPriceList implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
    @Basic(optional = false)
    @Column(name = "PRICE")
    private BigDecimal price;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "MATERIAL", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Material material;

    public SupplierPriceList() {
    }

    public SupplierPriceList(Long id) {
        this.id = id;
    }

    public SupplierPriceList(Long id, Date changeDate, BigDecimal price) {
        this.id = id;
        this.changeDate = changeDate;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
        if (!(object instanceof SupplierPriceList)) {
            return false;
        }
        SupplierPriceList other = (SupplierPriceList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierPriceList[ id=" + id + " ]";
    }
    
}
